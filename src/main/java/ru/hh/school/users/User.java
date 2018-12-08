package ru.hh.school.users;

public class User {

  private Integer id;
  private String firstName;
  private String lastName;

  private User(Integer id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  static User existing(int id, String firstName, String lastName) {
    return new User(id, firstName, lastName);
  }

  public static User newUser(String firstName, String lastName) {
    return new User(null, firstName, lastName);
  }

  void setId(int id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  void setFirstName(String name) {
    this.firstName = name;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      '}';
  }

  //region secret part
  // ToDo: реализовать equals() и hashCode()
  // как мы это сделаем?
  //endregion

}
