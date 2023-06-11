package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class mainController {
    private final DataSource dataSource;

    @Autowired
    public mainController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String adminlogin() {
        // model.addAttribute("user", model);

        return "admin/adminlogin";
    }

    @PostMapping("/adminlogin")
    public String dashboard(HttpSession session, @ModelAttribute("staff") users staff, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            final var resultSet = statement
                    .executeQuery("SELECT name, password, role FROM staff");

            String returnPage = "";

            while (resultSet.next()) {
                String username = resultSet.getString("name");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                if (username.equals(staff.getUsr()) && password.equals(staff.getPwd())) {
                    session.setAttribute("usr", staff.getUsr());
                    session.setAttribute("role", role);
                    returnPage = "redirect:/adminmainmenu";
                    break;
                } else {
                    returnPage = "admin/adminlogin";
                }
            }
            return returnPage;

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminlogin";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("succesfully logout");
        return "/";
    }

    @GetMapping("/adminmainmenu")
    public String showDashboard(HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("usr") != null) {
            if (session.getAttribute("role").equals("admin")) {
                return "admin/adminmainmenu";
            } else {
                return "therapist/adminmainmenu";
            }
        } else {
            System.out.println("Session expired or invalid...");
            return "redirect:/";
        }

    }

    @GetMapping("/patient")
    public String patient() {
        // model.addAttribute("user", model);
        return "admin/patient";
    }

    @GetMapping("/admission")
    public String admission() {
        // model.addAttribute("user", model);
        return "admin/admission";
    }

    // @GetMapping("/adminmainmenu")
    // public String greetingForm(Model model, @RequestParam("usr") String usr,
    // @RequestParam("pwd") String pwd) {
    // User user = userRepository.findByUsername(usr);
    // return "admin/adminmainmenu";
    // }

    // @PostMapping("/adminmainmenu")
    // public String greetingSubmit(@ModelAttribute user staff, Model model) {
    // model.addAttribute("staff", staff);
    // System.out.println("Staff data-------- : " + staff);
    // return "admin/adminmainmenu";
    // }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(mainController.class, args);
    }
}
