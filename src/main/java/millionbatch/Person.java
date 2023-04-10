package millionbatch;

public class Person {

  private int id;
  private String firstName;

  public Person() {
  }

  public Person(int id, String firstName) {
    this.id = id;
    this.firstName = firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFirstName() {
    return firstName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "id: " + id + ", firstName: " + firstName;
  }

}