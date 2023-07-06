package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties.Reactive.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.drug;
import com.heroku.java.model.patient;
import com.heroku.java.model.users;

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

    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

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

    // Read Acoount
    @GetMapping("/view-account")
    public String showAccounts(HttpSession session, users usrs, Model model) {

        // if (session.getAttribute("name") != null) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();

            final var resultSet = statement.executeQuery("SELECT id, name, password, role FROM staff ORDER BY id;");

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

    // @GetMapping("/update-account")
    // public String updateaccount() {
    // // model.addAttribute("user", model);
    // return "admin/update-account";
    // }
    @GetMapping("/update-account")
    public String showUpdateAccountForm(@ModelAttribute("users") users users, @RequestParam("id") int userid,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM staff WHERE id = ?";
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

    // Update Account
    @PostMapping("/update-account")
    public String updateAccountString(HttpSession session, @ModelAttribute("users") users user, Model model,
            @RequestParam("id") int usrid) {
        int id = user.getId();
        String usr = user.getUsr();
        String pwd = user.getPwd();
        String role = user.getRole();
        try (
                Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE staff SET id=?, name=? , password=?, role=? WHERE id=?";
            final var statement = connection.prepareStatement(sql);
            // String fullname = customer.getFullname();
            // String address = customer.getAddress();
            // String phonenum = customer.getPhonenum();
            // String icnumber = customer.getIcnumber();
            // Date licensecard = customer.getLicensecard();
            // String password = customer.getPassword();

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

    // delete Account
    @GetMapping("/delete-account")
    public String DeleteAccount(@ModelAttribute("users") users users, @RequestParam("id") int userid, Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM staff WHERE id=?";
            ;
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("succesfully logout");
        return "redirect:/";
    }

    @GetMapping("/adminmainmenu")
    public String showDashboard(HttpSession session) {
        // // Check if user is logged in
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
        // return "admin/adminmainmenu";
    }
     @GetMapping("/assigntherapist")
    public String assigntherapist() {
        return "admin/assigntherapist";
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

    @GetMapping("/register-patient")
    public String registerP() {
        // model.addAttribute("user", model);
        return "admin/register-patient";
    }

    @PostMapping("/register-patient")
    public String adminregisterPatient(HttpSession session, @ModelAttribute("admin-patient") patient patient,
            drug drugs) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO patient(patientname, patientsex, patientaddress, patientdate, patientstatus, patientdob, patientphoneno, patientbloodtype, patientic) VALUES(?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);
            String sql2 = "INSERT INTO drug (drugtype) VALUES(?)";
            final var statement2 = connection.prepareStatement(sql2);
            statement.setString(1, patient.getPName());
            statement.setString(2, patient.getPSex());
            statement.setString(3, patient.getPAddress());
            statement.setDate(4, patient.getPDate());
            statement.setString(5, patient.getPStatus());
            statement.setDate(6, patient.getPDOB());
            statement.setString(7, patient.getPPhoneNo());
            statement.setString(8, patient.getPBloodType());
            statement.setString(9, patient.getPIc());
            statement.executeUpdate();

            statement2.setString(1, drugs.getDrugtype());
            statement2.executeUpdate();

            connection.close();

            return "redirect:/register-patient";

        } catch (SQLException sqe) {
            System.out.println("error = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
        }
        return "admin/adminmainmenu";

    }

    @GetMapping("/add-account")
    public String addeaccount() {
        // model.addAttribute("user", model);
        return "admin/add-account";
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
