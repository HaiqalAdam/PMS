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
        return "admin/admissionIn";
    }

}
