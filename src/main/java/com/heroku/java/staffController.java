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
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class staffController {
    private final DataSource dataSource;

    @Autowired
    public staffController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/staffmainmenu")
    public String staffmainmenu() {
        // model.addAttribute("user", model);
        return "staff/staffmainmenu";
    }

    @GetMapping("/staff-admission")
    public String staffadmission() {
        // model.addAttribute("user", model);
        return "staff/staff-admission";
    }

    @GetMapping("/staff-patient")
    public String staffpatient() {
        // model.addAttribute("user", model);
        return "staff/staff-patient";
    }

    @GetMapping("/staff-register-patient")
    public String staff_register_patient() {
        // model.addAttribute("user", model);
        return "staff/staff-register-patient";
    }

    @PostMapping("/staff-register-patient")
    public String registerPatient(HttpSession session, @ModelAttribute("patient") patient reg_patient) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO patient(patientname, patientsex, patientaddress, patientdate, patientstatus, patientdob, patientphoneno, patientbloodtype) VALUES(?,?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String patientname = reg_patient.getPName();
            String patientsex = reg_patient.getPSex();
            String patientaddress = reg_patient.getPAddress();
            Date patientdate = reg_patient.getPDate();
            String patientstatus = reg_patient.getPStatus();
            Date patientdob = reg_patient.getPDOB();
            String patientphoneno = reg_patient.getPPhoneNo();
            String patientbloodtype = reg_patient.getPBloodType();

            statement.setString(1, patientname);
            statement.setString(2, patientsex);
            statement.setString(3, patientaddress);
            statement.setDate(4, patientdate);
            statement.setString(5, patientstatus);
            statement.setDate(6, patientdob);
            statement.setString(7, patientphoneno);
            statement.setString(8, patientbloodtype);

            connection.close();

            return "redirect:/staff-register-patient";

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
        return "staff/staffmainmenu";

    }

    @GetMapping("/staff-update-patient")
    public String staff_update_patient() {
        // model.addAttribute("user", model);
        return "staff/staff-update-patient";
    }

}