package com.heroku.java;

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

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @GetMapping("/admission")
    public String admission() {
        return "admin/admission";
    }

    @PostMapping("/admission")
    public String admissionAdmin(HttpSession session, @ModelAttribute("adminAdmission") patient patient) {
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.prepareStatement(
                    "SELECT patientname FROM patient JOIN admission ON (patient.patientid = admission.patientid) WHERE patientic =?");
            // statement.setInt(1, patientid);
            final var resultSet = statement.executeQuery();

            statement.setString(1, patient.getPName());
            statement.setString(2, patient.getPIc());
            statement.executeUpdate();

            return "redirect:/admin/admission";

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

}
