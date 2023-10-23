package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }
    public void createUsersTable() {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users" +
                "(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(45), lastname VARCHAR(45), age INT)")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Во время создания таблицы возникло исключение: " + e);
        }
    }
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Во время создания таблицы возникло исключение: " + e);
        }
    }
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, lastname, age) VALUES(?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setLong(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.err.println("Во время операции возникло исключение: " + e);
        }
    }
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM USERS WHERE id=?")) {
            preparedStatement.setLong(1, id);
        } catch (SQLException e) {
            System.out.println("Во время удаление по id возникло исключение: " + e);
        }
    }
    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        try (Connection connection = Util.getConnection(); Statement statement= connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id, name, lastname, age FROM users");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setAge(resultSet.getByte("AGE"));
                listUser.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Во время выводы таблицы возникло исключение: " + e);
        }
        return listUser;
    }
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Во время создания таблицы возникло исключение: " + e);
        }
    }
}
