package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.heroku.java.model.users;

public class UserDAO {
    private Connection connection;

    public UserDAO(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public users login(String username, String password) {
        String sql = "SELECT * FROM employee WHERE name = ? AND password = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                int id = resultSet.getInt("id");

                users user = new users();
                user.setUsr(username);
                user.setPwd(password);
                user.setRole(role);
                user.setId(id);

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
