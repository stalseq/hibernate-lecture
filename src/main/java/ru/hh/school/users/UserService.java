package ru.hh.school.users;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class UserService {

  private final SessionFactory sessionFactory;
  private final UserDao userDao;

  public UserService(SessionFactory sessionFactory, UserDao userDao) {
    this.sessionFactory = sessionFactory;
    this.userDao = userDao;
  }

  public Set<User> getAll() {
    return inTransaction(userDao::getAll);
  }

  public void deleteAll() {
    inTransaction(userDao::deleteAll);
  }

  public void saveNew(User user) {
    inTransaction(() -> userDao.saveNew(user));
  }

  public Optional<User> getBy(int userId) {
    return inTransaction(() -> userDao.getBy(userId));
  }

  public void deleteBy(int userId) {
    inTransaction(() -> userDao.deleteBy(userId));
  }

  public void update(User user) {
    inTransaction(() -> userDao.update(user));
  }

  public void changeFullName(int userId, String firstName, String lastName) {
    inTransaction(() -> {
      userDao.getBy(userId)
        .stream()
        .peek(user -> user.setFirstName(firstName))
        .forEach(user -> user.setLastName(lastName));
      // хибер отслеживает изменения сущностей и выполняет sql update перед коммитом транзакции
    });
  }

  private <T> T inTransaction(Supplier<T> supplier) {
    Optional<Transaction> transaction = beginTransaction();
    try {
      T result = supplier.get();
      transaction.ifPresent(Transaction::commit);
      return result;
    } catch (RuntimeException e) {
      transaction
        .filter(Transaction::isActive)
        .ifPresent(Transaction::rollback);
      throw e;
    }
  }

  private void inTransaction(Runnable runnable) {
    inTransaction(() -> {
      runnable.run();
      return null;
    });
  }

  private Optional<Transaction> beginTransaction() {
    Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
    if (transaction.isActive()) {
      return Optional.empty();
    }
    transaction.begin();
    return Optional.of(transaction);
  }

}
