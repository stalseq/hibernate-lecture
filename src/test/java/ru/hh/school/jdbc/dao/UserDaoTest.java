package ru.hh.school.jdbc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class UserDaoTest {

  private static UserDao userDao;

  @BeforeClass
  public static void setUpDatasource() {
    PGSimpleDataSource ds = new PGSimpleDataSource();
    ds.setUser("hh");
    ds.setPassword("123");
    ds.setUrl("jdbc:postgresql://localhost:5432/hh");
    userDao = new UserDaoJdbcImpl(ds);
  }

  @Before
  public void cleanUpDb() {
    userDao.deleteAll();
  }

  @Test
  public void getAllUsersShouldReturnEmptySet() {
    assertEquals(Collections.emptySet(), userDao.getAll());
  }

  @Test
  public void saveNewUserShouldInsertDbRow() {
    User user = User.newUser("John", "Lennon");
    userDao.saveNew(user);
    assertEquals(Set.of(user), userDao.getAll());
  }

  @Test(expected = IllegalArgumentException.class)
  public void savingOfExistingUserShouldBePrevented() {
    User user = User.newUser("John", "Lennon");
    userDao.saveNew(user);
    userDao.saveNew(user);

    fail();
  }

  @Test
  public void getByIdShouldReturnUserIfRowExists() {
    User user = User.newUser("John", "Lennon");
    userDao.saveNew(user);

    Optional<User> extractedUser = userDao.getBy(user.getId());

    assertTrue(extractedUser.isPresent());
    assertEquals(user, extractedUser.get());
  }


  @Test
  public void getByIdShouldReturnEmptyIfRowDoesntExist() {
    assertFalse(userDao.getBy(-1).isPresent());
  }

  @Test
  public void deleteUserShouldDeleteDbRow() {
    User user = User.newUser("John", "Lennon");
    userDao.saveNew(user);

    Optional<User> extractedUser = userDao.getBy(user.getId());
    assertTrue(extractedUser.isPresent());

    userDao.deleteBy(user.getId());

    extractedUser = userDao.getBy(user.getId());
    assertFalse(extractedUser.isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateShouldThrowExceptionForNewUsers() {
    User user = User.newUser("John", "Lennon");
    userDao.update(user);

    fail();
  }

  @Test
  public void updateShouldUpdateDbRowOfExistingUser() {
    User user = User.newUser("Ringo", "Lennon");
    userDao.saveNew(user);

    user.setFirstName("John");
    userDao.update(user);

    assertEquals(
      "John",
      userDao.getBy(user.getId()).map(User::getFirstName).orElse(null)
    );
  }
}
