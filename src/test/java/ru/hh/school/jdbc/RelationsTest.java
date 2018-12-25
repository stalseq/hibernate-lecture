package ru.hh.school.jdbc;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelationsTest {

  private static DataSource dataSource;
  private static SessionFactory sessionFactory;
  private static TransactionHelper transactionHelper;

  private static List<Integer> expectedUserIds;

  @BeforeClass
  public static void setUpTestSuit() {
    dataSource = createDataSource();
    sessionFactory = createSessionFactory();
    transactionHelper = new TransactionHelper(sessionFactory);
  }

  private static DataSource createDataSource() {
    PGSimpleDataSource ds = new PGSimpleDataSource();
    ds.setUser("hh");
    ds.setPassword("123");
    ds.setUrl("jdbc:postgresql://localhost:5432/hh");
    return ds;
  }

  private static SessionFactory createSessionFactory() {
    Metadata metadata = new MetadataSources()
      .addAnnotatedClass(User.class)
      .addAnnotatedClass(Resume.class)
      .buildMetadata();

    return metadata.buildSessionFactory();
  }

  @Before
  public void prepareTestData() {
    TestHelper.clearResumes(dataSource);
    TestHelper.clearUsers(dataSource);
    expectedUserIds = TestHelper.insertTestData(dataSource);
  }

  @Test
  public void fetchAssociatedEntitiesWithJdbc() {
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement(
        "SELECT * " +
          "FROM hhuser AS u " +
          "JOIN resume AS r ON u.user_id = r.user_id " +
          "WHERE u.user_id BETWEEN ? AND ? "
      )) {
        statement.setInt(1, expectedUserIds.get(0));
        statement.setInt(2, expectedUserIds.get(expectedUserIds.size() - 1));

        try (ResultSet resultSet = statement.executeQuery()) {
          List<User> users = toUsers(resultSet);
          assertEquals(expectedUserIds.size(), users.size());
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<User> toUsers(ResultSet resultSet) throws SQLException {
    Map<Integer, User> userMap = new LinkedHashMap<>();
    while (resultSet.next()) {
      Integer userId = resultSet.getInt(1);
      User user = userMap.get(userId);
      if (user == null) {
        user = new User();
        user.setId(userId);
        user.setFirstName(resultSet.getString(2));
        user.setLastName(resultSet.getString(3));
        userMap.put(userId, user);
      }
      Resume resume = new Resume();
      resume.setId(resultSet.getInt(4));
      resume.setDescription(resultSet.getString(5));
      user.addResume(resume);
    }
    return new ArrayList<>(userMap.values());
  }


  @Test
  public void fetchAssociatedEntitiesWithHibernate() {
    List<User> users = transactionHelper.inTransaction(this::fetchUsersWithResumes);

    assertEquals(expectedUserIds.size(), users.size());
  }

  private List<User> fetchUsersWithResumes() {
    return sessionFactory.getCurrentSession().createQuery(
      "select distinct u " +
        "from User u " +
        "join fetch u.resumes " +
        "where u.id between :lo and :hi", User.class)
      .setParameter("lo", expectedUserIds.get(0))
      .setParameter("hi", expectedUserIds.get(expectedUserIds.size() - 1))
      .list();
  }


}
