package ru.hh.school.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
import java.util.Set;

public class UserDao {

  private final SessionFactory sessionFactory;

  public UserDao(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public Set<User> getAll() {
    // ToDo: implement
    return null;
  }

  //region already implemented methods

  public void saveNew(User user) {
    session().persist(user);
    // или session().save() / session.saveOrUpdate()
    // save() вставляет новую строчку в БД, даже если у параметра User уже есть id
    // persist() при этом кинет исключение
  }

  public Optional<User> getBy(int id) {
    return Optional.ofNullable(
      session().get(User.class, id)
      // или session().load()
      // load() бросает исключение, если такой сущности не существует
    );
  }

  public void deleteBy(int id) {
    // jpa2.1 criteria builder

    CriteriaBuilder builder = session().getCriteriaBuilder();

    var query = builder.createCriteriaDelete(User.class);
    query.where(
      builder.equal(query.from(User.class).get("id"), id)
    );

    session().createQuery(query).executeUpdate();
  }

  public void deleteAll() {
    session().createQuery("delete from User").executeUpdate();
  }

  public void update(User user) {
    session().update(user);
    // или session.merge()
    // update() бросает исключение, если в сессии уже есть пользователь с таким id
    // merge() не бросает исключений
  }

  //endregion

  private Session session() {
    return sessionFactory.getCurrentSession();
    // можно использовать sessionFactory.openSession()
    // но надо обязательно закрывать сессию session.close();
  }
}
