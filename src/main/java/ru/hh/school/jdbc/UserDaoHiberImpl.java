package ru.hh.school.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
import java.util.Set;

public class UserDaoHiberImpl implements UserDao {

  private final SessionFactory sessionFactory;

  public UserDaoHiberImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Set<User> getAll() {
    return Set.copyOf(
      session().createQuery("from User", User.class).list()
    );
  }

  //region already implemented methods

  @Override
  public void saveNew(User user) {
    session().persist(user);
    // или save(user):
    // save() сохраняет detached объект в новой строчке бд и присваивает новый id
    // persist() бросает исключение, если у сущности есть id
  }

  @Override
  public void update(User user) {
    session().update(user);
    // или session.merge()
    // merge() не бросает исключений
    // update() бросает исключение, если в сессии уже есть пользователь с таким id
  }

  @Override
  public Optional<User> getBy(int id) {
    return Optional.ofNullable(
      session().get(User.class, id)
      // или session().load(), но load бросает исключение, если такой сущности не существует
    );
  }

  @Override
  public void deleteBy(int id) {
    CriteriaBuilder builder = session().getCriteriaBuilder();

    var query = builder.createCriteriaDelete(User.class);
    query.where(
      builder.equal(query.from(User.class).get("id"), id)
    );

    session().createQuery(query).executeUpdate();
  }

  @Override
  public void deleteAll() {
    session().createQuery("delete from User").executeUpdate();
  }

  //endregion

  private Session session() {
    return sessionFactory.getCurrentSession();
    // можно использовать sessionFactory.openSession()
    // но надо обязательно закрывать сессию session.close();
  }
}
