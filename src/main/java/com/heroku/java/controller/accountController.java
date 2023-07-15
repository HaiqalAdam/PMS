package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.users;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class accountController {
  private final DataSource dataSource;

  @Autowired
  public accountController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/register-staff")
  public String regStaff() {
    return "admin/register-staff";
  }

  // CREATE ACCOUNT
  @GetMapping("/add-account")
  public String addeaccount() {
    // model.addAttribute("user", model);
    return "admin/add-account";
  }

  @PostMapping("/add-account")
  public String Addaccount(HttpSession session, @ModelAttribute("add-account") users staff) {
    try (Connection connection = dataSource.getConnection()) {
      String sql = "INSERT INTO employee(name, password, role, managerid) VALUES (?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(sql);

      String name = staff.getUsr();
      String password = staff.getPwd();
      String roles = staff.getRole();

      if (name.equals("") || password.equals("")) {
        return "redirect:/add-account";
      } else {
        statement.setString(1, name);
        statement.setString(2, password);
        statement.setString(3, roles);
        statement.setInt(4, (int) session.getAttribute("id"));
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
          int parentId = generatedKeys.getInt(1);

          if (roles.equals("therapist")) {
            String therapistSql = "INSERT INTO therapist (id) VALUES (?)";
            PreparedStatement therapistStatement = connection.prepareStatement(therapistSql);
            therapistStatement.setInt(1, parentId);
            therapistStatement.executeUpdate();
          } else if (roles.equals("staff")) {
            String staffSql = "INSERT INTO staff (id) VALUES (?)";
            PreparedStatement staffStatement = connection.prepareStatement(staffSql);
            staffStatement.setInt(1, parentId);
            staffStatement.executeUpdate();
          }

          connection.close();
          return "redirect:/view-account";
        } else {
          connection.close();
          return "redirect:/";
        }
      }
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      sqe.printStackTrace();
      return "redirect:/";
    } catch (Exception e) {
      System.out.println("E message: " + e.getMessage());
      return "redirect:/";
    }
  }

  // READ ACCOUNT
  @GetMapping("/view-account")
  public String showAccounts(HttpSession session, users usrs, Model model) {

    // if (session.getAttribute("name") != null) {
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT id, name, password, role FROM employee ORDER BY id;");

      // int row = 0;
      ArrayList<users> users = new ArrayList<>();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String usr = resultSet.getString("name");
        String pwd = resultSet.getString("password");
        String role = resultSet.getString("role");
        users user = new users(id, usr, pwd, role);
        users.add(user);
      }
      model.addAttribute("users", users);
      // connection.close();
      return "admin/view-account";

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "index";
    }
  }

  // UPDATE ACCOUNT
  @GetMapping("/update-account")
  public String showUpdateAccountForm(@ModelAttribute("users") users users, @RequestParam("id") int userid,
      Model model) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "SELECT * FROM employee WHERE id = ?";
      final var statement = connection.prepareStatement(sql);
      statement.setInt(1, userid);

      final var resultSet = statement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String usr = resultSet.getString("name");
        String pwd = resultSet.getString("password");
        String role = resultSet.getString("role");
        users user = new users(id, usr, pwd, role);
        model.addAttribute("users", user);
      }
      return "admin/update-account";
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "index";
    }
  }

  @PostMapping("/update-account")
  public String updateAccountString(HttpSession session, @ModelAttribute("users") users user, Model model,
      @RequestParam("id") int usrid) {
    int id = user.getId();
    String usr = user.getUsr();
    String pwd = user.getPwd();
    String role = user.getRole();
    try (
        Connection connection = dataSource.getConnection()) {
      String sql = "UPDATE employee SET id=?, name=? , password=?, role=? WHERE id=?";
      final var statement = connection.prepareStatement(sql);

      statement.setInt(1, id);
      statement.setString(2, usr);
      statement.setString(3, pwd);
      statement.setString(4, role);
      statement.setInt(5, usrid);

      statement.executeUpdate();

      return "redirect:/view-account";

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      System.out.println("error");
      return "redirect:/adminmainmenu";
    }
  }

  // DELETE ACCOUNT
  @GetMapping("/delete-account")
  public String DeleteAccount(@ModelAttribute("users") users users, @RequestParam("id") int userid, Model model) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "DELETE FROM employee WHERE id=?;";
      final var statement = connection.prepareStatement(sql);
      statement.setInt(1, userid);

      // execute delete
      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0) {
        // Deletion successful
        return "redirect:/view-account";
      } else {
        // No rows affected, account not found
        return "account-not-found";
      }
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "index";
    }

  }

}
