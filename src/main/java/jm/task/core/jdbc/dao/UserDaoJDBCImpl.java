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
//        if (checkDataBase()) {
//            return;
//        }
        Connection connection = null;
        PreparedStatement statement = null;
        String createTableSQL;
        try {
            createTableSQL = "CREATE TABLE users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(45) NOT NULL, last_name VARCHAR(45) NOT NULL, age INT NOT NULL, PRIMARY KEY (id));";
            connection = Util.getConnection();
            statement = connection.prepareStatement(createTableSQL);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
//        if (!checkDataBase()) {
//            return;
//        }
        Connection connection = null;
        PreparedStatement statement = null;
        String dropTableSQL;
        try {
            dropTableSQL = "DROP TABLE users;"; //todo почему он на все строки ругается?
            connection = Util.getConnection();
            statement = connection.prepareStatement(dropTableSQL);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = null;
        PreparedStatement statement = null;
        String saveUserSQL;
        try {
            saveUserSQL = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?);";
            connection = Util.getConnection();
            statement = connection.prepareStatement(saveUserSQL);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println(name + " was added to users.");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { //todo нужно ли каждый раз закрывать соединение | statement и как это правильно делать
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }



    public User saveUser(String name, String lastName, byte age, int varForOverload) { // todo перегрузил метод, чтобы он действительно создвал юзера и знал его id
        User user = new User(name, lastName, age);
        Connection connection = null;
        PreparedStatement statement = null;
        Statement statementForGetId = null;
        String saveUserSQL;
        try {
            saveUserSQL = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?);";
            connection = Util.getConnection();
            statement = connection.prepareStatement(saveUserSQL);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();

            String getUserId = "SELECT * FROM users ORDER BY ID DESC LIMIT 1";
            statementForGetId = Util.getConnection().createStatement();
            ResultSet resultSet = statementForGetId.executeQuery(getUserId);
            while (resultSet.next()) {
                user.setId((long) resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (statementForGetId != null) {
                    statementForGetId.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }

    public void removeUserById(long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String removeUserSQL;
        try {
            removeUserSQL = "DELETE FROM users  WHERE id = ?;";
            connection = Util.getConnection();
            statement = connection.prepareStatement(removeUserSQL);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            Statement statement = Util.getConnection().createStatement();
            String getAllUsersSQL = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(getAllUsersSQL);
            while (resultSet.next()) {
                long id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                byte age = (byte) resultSet.getInt("age");
                list.add(new User(name, lastName, age));
            }
            Util.getConnection().close(); //todo нужно ли каждый раз закрывать соединение | statement и как это правильно делать
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        Connection connection = null;
        PreparedStatement statement = null;
        String cleanTableSQL;
        try {
            cleanTableSQL = "TRUNCATE TABLE users;";
            connection = Util.getConnection();
            statement = connection.prepareStatement(cleanTableSQL);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean checkDataBase() { //todo не могу понять почему не работает Вчегда возвращает true
        Connection connection = null;
        try {
            connection = Util.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, "users", null);
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }
}
