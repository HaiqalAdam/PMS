package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.drug;

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
    public String registerPatient(HttpSession session, @ModelAttribute("patient") patient reg_patient, drug drugs) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO patient(patientname, patientsex, patientaddress, patientdate, patientstatus, patientdob, patientphoneno, patientbloodtype, patientic) VALUES(?,?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, reg_patient.getPName());
            statement.setString(2, reg_patient.getPSex());
            statement.setString(3, reg_patient.getPAddress());
            statement.setDate(4, reg_patient.getPDate());
            statement.setString(5, reg_patient.getPStatus());
            statement.setDate(6, reg_patient.getPDOB());
            statement.setString(7, reg_patient.getPPhoneNo());
            statement.setString(8, reg_patient.getPBloodType());
            statement.setString(9, reg_patient.getPIc());

            String sql2 = "INSERT INTO drug(drugtype) VALUES(?)";
            final var statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, drugs.getDrugtype());

            statement.executeUpdate();

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