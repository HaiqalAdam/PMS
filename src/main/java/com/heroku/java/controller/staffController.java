package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.model.drug;
import com.heroku.java.model.patient;

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
    public String staffmainmenu(Model model) {
        try {
            Connection connection2 = dataSource.getConnection();
            final var statement2 = connection2.createStatement();
            final var resultSet2 = statement2.executeQuery(
                    "SELECT COUNT(*) AS count FROM patient;");
            resultSet2.next();
            int count2 = resultSet2.getInt("count");

            model.addAttribute("patientCount", count2);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return "staff/staffmainmenu";
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
    public String registerPatient(HttpSession session, @ModelAttribute("patient") patient patient, drug drugs) {
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