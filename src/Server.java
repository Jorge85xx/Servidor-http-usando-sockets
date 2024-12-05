import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {

  private final int port;
  private final Alunos alunos = new Alunos();

  public Server() {
      this.port = 12345;
  }

  public Server(int port) {
      this.port = port;
  }

  public void start() throws IOException {

      try (ServerSocket server = new ServerSocket(this.port, 2048, InetAddress.getByName("127.0.0.1"))) {
          System.out.println("Servidor iniciado na porta " + this.port);

          while (true) {
              new Thread(new ClientProcessor(server.accept(), this.alunos)).start();
          }
      } 
  }

}

