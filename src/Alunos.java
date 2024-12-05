import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Alunos {
  
  public Map<Integer, String> alunos = new HashMap<>();
  private int counter = 0;

  public synchronized int incrementAndGet() {
      this.counter++;
      return this.counter;
  }

  public String addAluno() {
    int id = incrementAndGet() - 1; 
    String nome = generateRandomName(); 
    
    synchronized (this) {
        alunos.put(id, nome); 
    }
    return "ID: " + id + ", Nome: " + nome;
  }

  public boolean delAluno(int id) {
      synchronized (this) {
          if (alunos.containsKey(id)) {
              alunos.remove(id);
              return true;
          }
      }
      return false;
  }

  public String getAlunoById(int id) {
    synchronized (this) {
      if (alunos.containsKey(id)) {
          String nome = alunos.get(id);
          return "ID: " + id + " -> Nome: " + nome;
      } else {
          return null;
      }
  }

}


  public static String generateRandomName() {
    String[] firstNames = {
      "Frodo", "Gandalf", "Aragorn", "Legolas", "Gimli", "Samwise", "Gollum", "Galadriel", "Saruman", "Boromir", 
      "Tony", "Steve", "Natasha", "Thor", "Bruce", "Clint", "Peter", "Wanda", "Stephen", "Carol",
      "Bruce", "Clark", "Diana", "Barry", "Arthur", "Hal", "John", "Oliver", "Kara", "Jason",
      "Sebastian", "Mia", "Keith", "Catherine", "Greg", "Tom", 
      "Woody", "Buzz", "Jessie", "Rex", "Mr. Potato Head", "Slinky Dog", "Bo Peep", "Woody", "Forky", "Lotso", 
      "John", "Hans", "Karl", "Theo", "Sergeant", "Ellis", "Alexander" 
  };
  
  String[] lastNames = {
      "Baggins", "Gandalf", "Aragorn", "Legolas", "Greenleaf", "Son of Gloin", "Gamgee", "Sauron", "Elrond", "Boromir",
      "Stark", "Rogers", "Romanoff", "Odinson", "Banner", "Barton", "Parker", "Maximoff", "Strange", "Danvers", 
      "Wayne", "Kent", "Prince", "Allen", "Curry", "Jordan", "Queen", "Kara", "Todd", "Grayson", 
      "La La", "Stone", "Chazelle", "Damien", "Gosling", "Roberts", 
      "Anderson", "Buzz", "Woody", "Dinosaur", "Potatohead", "Peep", "Forky", "Rex", "Spencer", "DiCaprio", 
      "McClane", "Gruber", "Ellis", "Williams", "Nakatomi", "Stauffer", "Silva", "Oliveira", "Santos", "Souza",
      "Pereira", "Costa", "Almeida", "Ferreira", "Rodrigues", "Barros"};
    Random random = new Random();
    
    String firstName = firstNames[random.nextInt(firstNames.length)];
    String lastName = lastNames[random.nextInt(lastNames.length)];
    
    return firstName + " " + lastName;
}


}
