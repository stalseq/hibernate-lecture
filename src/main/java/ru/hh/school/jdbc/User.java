package ru.hh.school.jdbc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hhuser")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer id;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @OneToMany(mappedBy = "user")
  private List<Resume> resumes = new ArrayList<>();

  User() {}

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

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void addResume(Resume resume) {
    resumes.add(resume);
    resume.setUser(this);
  }

  public List<Resume> getResumes() {
    return resumes;
  }

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
