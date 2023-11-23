package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.ATP;
import com.heroku.java.model.drug;
import com.heroku.java.model.patient;
import com.heroku.java.model.therapist;
import com.heroku.java.model.users;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;

import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class admissionController {
    private final DataSource dataSource;

    @Autowired
    public admissionController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/staff-admission")
    public String staffAdmission() {
        return "staff/staff-admission";
    }

    @PostMapping("/staff-admission")
    public String admissionStaff(HttpSession session,
            @RequestParam("pName") String pName, ATP atp, patient p, Model model) {
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.prepareStatement(
                    "SELECT * FROM patient WHERE patientname like ?");
            statement.setString(1, "%" + pName + "%");
            final var resultSet = statement.executeQuery();

            ArrayList<patient> patients = new ArrayList<>();
            while (resultSet.next()) {
                String patientName = resultSet.getString("patientname");
                int pid = resultSet.getInt("patientid");
                System.out.println("name = " + patientName);

                patient patient2 = new patient(pid, patientName);
                patients.add(patient2);
                model.addAttribute("adminAdmission", patients); // Add the ptns object to the model
            }

        } catch (SQLException sqe) {
            System.out.println("error = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
        }
        return "staff/staff-admission";
    }

    @GetMapping("/staff-admissionOut")
    public String staffAdmissioOutn() {
        return "staff/staff-admissionOut";
    }

    @GetMapping("/admissionOut")
    public String admissionOut() {
        return "admin/admissionOut";
    }

    @PostMapping("/admissionOut")
    public String admissionOutPatient(HttpSession session,
            @RequestParam("pName") String pName, ATP atp, Model model, @RequestParam("tName") String tName) {
        try {
            Connection connection = dataSource.getConnection();
            String insertadmissionout = "INSERT INTO admission(admissionoutdate) VALUES(?)";
            PreparedStatement ps = connection.prepareStatement(insertadmissionout);
            ps.setTimestamp(1, new Timestamp(atp.getAdmissionOut().getTime()));

        } catch (Exception e) {
            // TODO: handle exception
        }

        return "admin/admissionOut";
    }

    @GetMapping("/admissionIn")
    public String admission() {
        return "admin/admissionIn";
    }

    @PostMapping("/admissionIn")
    public String admissionAdmin(HttpSession session,
            @RequestParam("pName") String pName, ATP atp, patient p, Model model) {
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.prepareStatement(
                    "SELECT * FROM patient WHERE patientname like ?");
            statement.setString(1, "%" + pName + "%");
            final var resultSet = statement.executeQuery();

            ArrayList<patient> patients = new ArrayList<>();
            while (resultSet.next()) {
                String patientName = resultSet.getString("patientname");
                int pid = resultSet.getInt("patientid");
                System.out.println("name = " + patientName);

                patient patient2 = new patient(pid, patientName);
                patients.add(patient2);
                model.addAttribute("adminAdmission", patients);

                Connection connection2 = dataSource.getConnection();
                final var statement2 = connection2.createStatement();
                final var resultSet2 = statement2.executeQuery(
                        "SELECT e.id, t.* FROM employee e JOIN therapist t ON (e.id = t.id) ORDER BY e.id;");

                ArrayList<therapist> therapistList = new ArrayList<>();
                while (resultSet2.next()) {
                    int id = resultSet2.getInt("id");
                    String tName = resultSet2.getString("therapistname");
                    System.out.println("tName: " + tName);
                    therapist therapists = new therapist(id, null, null, tName);
                    therapistList.add(therapists);
                }
                model.addAttribute("therapist", therapistList);
                return "admin/admissionIn";
                // Add the ptns object to the model
            }

            Connection connection2 = dataSource.getConnection();
            final var statement2 = connection2.createStatement();
            final var rs = statement2.executeQuery("SELECT therapistname FROM therapist");

            List<String> therapistNames = new ArrayList<>();
            while (rs.next()) {
                therapistNames.add(rs.getString("therapistname"));
            }

            model.addAttribute("therapistNames", therapistNames);

        } catch (SQLException sqe) {
            System.out.println("error = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
        }
        return "admin/admissionIn";
    }

    // @PostMapping("/assignTherapist")
    // public String assignTherapist(HttpSession session,
    // @RequestParam("pName") String pName, ATP atp, Model model) {
    // try {
    // Connection connection = dataSource.getConnection();
    // String insertadmission = "INSERT INTO admission(admissionindate,patientid,id)
    // VALUES(?,?,?)";
    // PreparedStatement ps = connection.prepareStatement(insertadmission);
    // ps.setTimestamp(1, new Timestamp(atp.getAdmissionIn().getTime()));
    // ps.setInt(2, atp.getPatientid());
    // ps.setInt(3, atp.getId());
    // ps.executeQuery();

    // return "admin/admissionIn";

    @PostMapping("/assignTherapist")
    public String assignTherapist(
            HttpSession session,
            @RequestParam("pName") String pName,
            @ModelAttribute ATP atp,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String insertadmission = "INSERT INTO admission(admissionindate,patientid,id) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertadmission);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, atp.getPatientid());
            ps.setInt(3, atp.getId());
            ps.executeUpdate(); // Use executeUpdate() for INSERT operation

            return "admin/admissionIn";

        } catch (SQLException sqe) {
            System.out.println("error = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
        }
        return "admin/adminmainmenu";
    }

    // @PostMapping("/selectTherapist")
    // public String selectTherapist(HttpSession session, ATP atp, Model model) {
    // try {
    // Connection connection2 = dataSource.getConnection();
    // final var statement = connection2.createStatement();
    // final var rs = statement.executeQuery("SELECT therapistname FROM therapist");

    // List<String> therapistNames = new ArrayList<>();
    // while (rs.next()) {
    // therapistNames.add(rs.getString("therapistname"));
    // }

    // model.addAttribute("therapistNames", therapistNames);

    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // return null;

    // }

}
