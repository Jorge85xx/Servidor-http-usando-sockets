import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientProcessor implements Runnable {

    private final Socket socket;
    private final Alunos alunos;

    public ClientProcessor(Socket socket, Alunos alunos) {
        this.socket = socket;
        this.alunos = alunos;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run() {
        try {
            System.out.println("Novo cliente conectado: " + this.socket.getInetAddress().getHostAddress() + " at port " + this.socket.getPort());
            try (PrintWriter out = new PrintWriter(this.socket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {

                String requestLine = in.readLine();
                if (requestLine != null && !requestLine.isEmpty()) {
                    System.out.println("solicitação: " + requestLine);
                    String[] parts = requestLine.split(" ");
                    String arg = "";

                    if (parts.length > 1) {
                        arg = parts[1];
                        System.out.println("URL solicitada: " + arg);
                    }

                    if (requestLine.startsWith("GET")) {
                        handleGetRequest(out, arg);
                    } else if (requestLine.startsWith("POST")) {
                        handlePostRequest(out, arg);
                    } else if (requestLine.startsWith("DELETE")) {
                        handleDeleteRequest(out, arg);
                    } else {
                        sendHtmlResponse(out, "Método não suportado", "red", 405);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public void handleGetRequest(PrintWriter out, String arg) {
        if (arg.contains("/aluno")) {
            try {
                String[] id = arg.split("/");
                if (id.length > 1) {
                    int id_aluno = Integer.parseInt(id[2]); 
                    String aluno = alunos.getAlunoById(id_aluno);
                    if (aluno != null) {
                        sendHtmlResponse(out, aluno, "green", 200);
                    } else {
                        sendHtmlResponse(out, "Aluno não encontrado...", "red", 404);
                    }
                }
            } catch (NumberFormatException e) {
                sendHtmlResponse(out, "ID do aluno inválido. Por favor, forneça um número válido.", "red", 400);
            } catch(ArrayIndexOutOfBoundsException e){
                sendHtmlResponse(out, "ID do aluno não digitado, impossivel achar ele assim. Por favor, forneça um número válido.", "red", 400);
            }
        }
    }

    public void handlePostRequest(PrintWriter out, String arg) {
        if (arg.equalsIgnoreCase("/aluno") || arg.equalsIgnoreCase("/aluno/")) {
            String aluno = alunos.addAluno(); 
            sendHtmlResponse(out, aluno, "blue", 201);
        } else {
            sendHtmlResponse(out, "URL inválida. Tente novamente usando /aluno.", "red", 400);
        }
    }

    public void handleDeleteRequest(PrintWriter out, String arg) {
        if (arg.contains("/aluno/")) {
            try {
                String[] id = arg.split("/");
                if (id.length > 1) {
                    int id_aluno = Integer.parseInt(id[2]);
                    boolean success = alunos.delAluno(id_aluno); 
                    if (success) {
                        sendHtmlResponse(out, "Aluno deletado com sucesso.", "blue", 200);
                    } else {
                        sendHtmlResponse(out, "Aluno não encontrado para ser removido.", "red", 404);
                    }
                }
            } catch (NumberFormatException e) {
                sendHtmlResponse(out, "URL inválida. O ID do aluno não foi especificado para exclusão entao fica dificil excluir, tente /aluno/ID substitua id pelo número.", "red", 400);
            }
        } else {
            sendHtmlResponse(out, "URL inválida para exclusão.", "red", 400);
        }
    }

    public void sendHtmlResponse(PrintWriter out, String content, String color, int statusCode) {
        String statusMessage = getStatusMessage(statusCode);
        String responseBody = "<html><body style='color:" + color + ";'>"
                + "<h3>" + content + "</h3>"
                + "</body></html>";

        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length);
        out.println();
        out.println(responseBody);
        out.flush();
    }

    private String getStatusMessage(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            default -> "Internal Server Error";
        };
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
