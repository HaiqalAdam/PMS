package com.heroku.java.controller;

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
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
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

    // CREATE PATIENT
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
            String sql = "INSERT INTO patient(patientname, patientic, patientsex, patientaddress, patientdate, patientstatus, patientdob, patientphoneno, patientbloodtype) VALUES(?,?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);
            String sql2 = "INSERT INTO drug (drugtype) VALUES(?)";
            final var statement2 = connection.prepareStatement(sql2);
            statement.setString(1, patient.getPName());
            statement.setString(2, patient.getPIc());
            statement.setString(3, patient.getPSex());
            statement.setString(4, patient.getPAddress());
            statement.setDate(5, patient.getPDate());
            statement.setString(6, patient.getPStatus());
            statement.setDate(7, patient.getPDOB());
            statement.setString(8, patient.getPPhoneNo());
            statement.setString(9, patient.getPBloodType());
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

    // READ PATIENT
    @GetMapping("/patient")
    public String showPatient(HttpSession session, patient ptns, Model model) {

        // if (session.getAttribute("name") != null) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();

            final var resultSet = statement.executeQuery(
                    "SELECT patientid, patientname, patientic, patientsex, patientaddress, patientdate, patientstatus, patientdob, patientphoneno, patientbloodtype FROM patient ORDER BY patientid;");

            // int row = 0;
            ArrayList<patient> patient = new ArrayList<>();
            while (resultSet.next()) {
                int pId = resultSet.getInt("patientid");
                String pName = resultSet.getString("patientname");
                String pIc = resultSet.getString("patientic");
                String pSex = resultSet.getString("patientsex");
                String pAddress = resultSet.getString("patientaddress");
                Date pDate = resultSet.getDate("patientdate");
                String pStatus = resultSet.getString("patientstatus");
                Date pDOB = resultSet.getDate("patientdob");
                String pPhoneNo = resultSet.getString("patientphoneno");
                String pBloodType = resultSet.getString("patientbloodtype");

                patient patients = new patient(pId, pName, pIc, pSex, pAddress, pDate, pStatus, pDOB, pPhoneNo,
                        pBloodType);
                patient.add(patients);

            }
            model.addAttribute("patient", patient);
            // connection.close();
            return "admin/patient";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "index";
        }
    }

    // UPDATE PATIENT
    @GetMapping("/update-patient")
    public String showUpdatePatient(@ModelAttribute("patient") patient patient, @RequestParam("id") int ptnid, Model model) {
         try {
        Connection connection = dataSource.getConnection();
        String sql = "SELECT * FROM patient WHERE patientid = ?";
        final var statement = connection.prepareStatement(sql);
        statement.setInt(1, ptnid);

            final var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int pId = resultSet.getInt("patientid");
                String pName = resultSet.getString("patientname");
                String pIc = resultSet.getString("patientic");
                String pSex = resultSet.getString("patientsex");
                String pAddress = resultSet.getString("patientaddress");
                Date pDate = resultSet.getDate("patientdate");
                String pStatus = resultSet.getString("patientstatus");
                Date pDOB = resultSet.getDate("patientdob");
                String pPhoneNo = resultSet.getString("patientphoneno");
                String pBloodType = resultSet.getString("patientbloodtype");

                patient patients = new patient(pId, pName, pIc, pSex, pAddress, pDate, pStatus, pDOB, pPhoneNo,
                        pBloodType);
                model.addAttribute("patient", patients);
            }
            return "admin/update-patient";
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "index";
        }
    }

     @PostMapping("/update-patient") 
    public String updatePatient(HttpSession session, @ModelAttribute("patient") patient patients , Model model, @RequestParam("pId") int ptnsid) { 
        int pId = patients.getPId();
        String pName = patients.getPName();
        String pIc = patients.getPIc();
        String pSex = patients.getPSex();
        String pAddress = patients.getPAddress();
        Date pDate = patients.getPDate();
        String pStatus = patients.getPStatus();
        Date pDOB = patients.getPDOB();
        String pPhoneNo = patients.getPPhoneNo();
        String pBloodType = patients.getPBloodType();
            try (
            Connection connection = dataSource.getConnection()) { 
            String sql = "UPDATE patient SET patientid = ?, patientname = ?, patientic = ?, patientsex = ?, patientaddress = ?, patientdate = ?, patientstatus = ?, patientdob = ?, patientphoneno = ?, patientbloodtype = ? WHERE patientid=?";
            final var statement = connection.prepareStatement(sql);

            statement.setInt(1, pId);
            statement.setString(2, pName);
            statement.setString(3, pIc);
            statement.setString(4, pSex);
            statement.setString(5, pAddress);
            statement.setDate(6, pDate);
            statement.setString(7, pStatus);
            statement.setDate(8, pDOB);
            statement.setString(9, pPhoneNo);
            statement.setString(10, pBloodType);
            statement.setInt(11,ptnsid);

            statement.executeUpdate();
                 
            return "redirect:/patient";  
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/adminmainmenu"; 
        } 
    }


    //Delete Patient
    @GetMapping("/delete-patient")
    public String DeletePatient(@ModelAttribute("patient") patient patient, @RequestParam("pId") int ptnid, Model model) {
         try {
        Connection connection = dataSource.getConnection();
        String sql = "DELETE FROM patient WHERE patientid=?;";
        final var statement = connection.prepareStatement(sql);
        statement.setInt(1, ptnid);

       //execute delete
       int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0) {
            // Deletion successful
            return "redirect:/patient";
        } else {
            // No rows affected, account not found
            return "account-not-found";
        }
        }catch (Throwable t) {
        System.out.println("message : " + t.getMessage());
        return "redirect:/adminmainmenu";
      }
    
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
