# Gerenciador de Alunos com Java

## Sobre o Projeto

Este projeto é um sistema de gerenciamento de alunos desenvolvido em **Java**, utilizando **sockets** e **threads** para comunicação e processamento simultâneo de requisições. O sistema suporta operações de consulta, exclusão e inclusão de alunos, com respostas formatadas em HTML.

---

## Requisitos

### Requisitos Funcionais

1. **Consulta de Aluno**  
   - **Endpoint:** `GET /aluno/{id}`  
   - **Caso 1:**  
     - Quando existir um aluno com o ID fornecido, o servidor:  
       - Retorna uma resposta `HTTP/1.1 200 OK`.  
       - Inclui os dados do aluno formatados em HTML.  
   - **Caso 2:**  
     - Quando não existir um aluno com o ID fornecido, o servidor:  
       - Retorna uma resposta `HTTP/1.1 404 NOT FOUND`.  
       - Inclui uma mensagem de erro apropriada formatada como HTML.

2. **Exclusão de Aluno**  
   - **Endpoint:** `DELETE /aluno/{id}`  
   - Quando existir um aluno com o ID fornecido:  
     - O servidor excluira permanentemente o aluno do banco de dados.  
     - Novas consultas ao mesmo ID retornaram o status de não existência (`HTTP/1.1 404 NOT FOUND`).  
     - O servidor retornara uma mensagem apropriada formatada como HTML.

3. **Criação de Aluno**  
   - **Endpoint:** `POST /aluno`  
   - O servidor criara um novo aluno com dados gerados aleatoriamente e garantira que:  
     - O ID do novo aluno seja único e não terá IDs previamente excluídos.  
     - O retorno será uma mensagem apropriada formatada como HTML.

---

### Requisitos Não-Funcionais

1. **Tratamento de Concorrência**  
   - Operações de inclusão e exclusão de alunos serão gerenciadas com **threads**, garantindo o tratamento adequado de concorrência.  

