package ru.hh.school.jdbc;

import java.util.Optional;
import java.util.Set;

public interface UserDao {

  void saveNew(User user);

  Set<User> getAll();

  Optional<User> getBy(int id);

  void update(User user);

  void deleteBy(int id);

  void deleteAll();

}
