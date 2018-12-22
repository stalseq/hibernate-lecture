package ru.hh.school.jdbc.dao;

import java.util.Objects;

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

  //region getters and setters
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

  public void setFirstName(String name) {
    this.firstName = name;
  }
  //endregion


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) &&
      Objects.equals(firstName, user.firstName) &&
      Objects.equals(lastName, user.lastName);
  }

  @Override
  public int hashCode() {
    return 17;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      '}';
  }
}
