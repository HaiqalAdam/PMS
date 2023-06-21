package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties.Reactive.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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
    public String adminlogin(HttpSession session) {
        if (session.getAttribute("usr") != null) {
            return "admin/adminmainmenu";
        } else {
            return "admin/adminlogin";
        }
    }

    @PostMapping("/adminlogin")
    public String dashboard(HttpSession session, @ModelAttribute("staff") users staff, users admin, users therapist,
            Model model) {
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
                } else if (username.equals(admin.getUsr()) && password.equals(admin.getPwd())) {
                    session.setAttribute("usr", admin.getUsr());
                    session.setAttribute("role", role);
                    returnPage = "redirect:/adminmainmenu";
                    break;
                } else if (username.equals(therapist.getUsr()) && password.equals(therapist.getPwd())) {
                    session.setAttribute("usr", therapist.getUsr());
                    session.setAttribute("role", role);
                    returnPage = "redirect:/dashboard-therapist";
                    break;
                }

                else {
                    returnPage = "admin/adminlogin";
                }
            }
            return returnPage;

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminlogin";
        }

    }

    /**
     * @param session
     * @param staff
     * @param model
     * @return
     */
    @PostMapping("/add-account")
    public String Addaccount(HttpSession session, @ModelAttribute("add-account") users staff) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO staff(name, password, role) VALUES (?,?,?)";
            final var statement = connection.prepareStatement(sql);

            statement.setString(1, staff.getUsr());
            statement.setString(2, staff.getPwd());
            statement.setString(3, staff.getRole());
            statement.executeUpdate();

            // debug
            // System.out.println("name" + staff.getUsr());
            // System.out.println("password" + staff.getPwd());
            // System.out.println("role" + staff.getRole());
            connection.close();

            return "redirect:/view-account";

        }

        catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();

            return "redirect:/";
        } catch (Exception e) {
            System.out.println("E message : " + e.getMessage());
            return "redirect:/";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("succesfully logout");
        return "redirect:/";
    }

    @GetMapping("/adminmainmenu")
    public String showDashboard(HttpSession session) {
        // // Check if user is logged in
        // if (session.getAttribute("usr") != null) {
        // if (session.getAttribute("role").equals("admin")) {
        // return "admin/adminmainmenu";
        // } else {
        // return "therapist/adminmainmenu";
        // }
        // } else {
        // System.out.println("Session expired or invalid...");
        // return "redirect:/";
        // }
        return "admin/adminmainmenu";
    }

    @GetMapping("/patient")
    public String patient() {
        // model.addAttribute("user", model);
        return "admin/patient";
    }

    @GetMapping("/therapist")
    public String therapist() {
        // model.addAttribute("user", model);
        return "admin/therapist";
    }

    @GetMapping("/register-therapist")
    public String registerT() {
        // model.addAttribute("user", model);
        return "admin/register-therapist";
    }

    @GetMapping("/update-therapist")
    public String updateT() {
        // model.addAttribute("user", model);
        return "admin/update-therapist";
    }

    @GetMapping("/admission")
    public String admission() {
        // model.addAttribute("user", model);
        return "admin/admission";
    }

    @GetMapping("/register-patient")
    public String registerP() {
        // model.addAttribute("user", model);
        return "admin/register-patient";
    }

    @GetMapping("/update-patient")
    public String updateP() {
        // model.addAttribute("user", model);
        return "admin/update-patient";
    }

    @GetMapping("/add-account")
    public String account() {
        // model.addAttribute("user", model);
        return "admin/add-account";
    }

    @GetMapping("/view-account")
    public String viewaccount() {
        // model.addAttribute("user", model);
        return "admin/view-account";
    }

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
