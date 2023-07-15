package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.drug;
import com.heroku.java.model.patient;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
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

    @GetMapping("/admissionOut")
    public String admissionOut() {
        return "admin/admissionOut";
    }

    @GetMapping("/admissionIn")
    public String admission(Model model) {
        List<patient> patientList = new ArrayList<>();
        model.addAttribute("patientList", patientList);
        return "admin/admissionIn";
    }

    // @PostMapping("/admit")
    // public String admitPatient(@RequestParam("id")Integer pID){
    // try {
    // Connection connection = dataSource.getConnection();
    // fi

    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    @PostMapping("/admissionIn")
    public String admissionAdmin(HttpSession session, @ModelAttribute("adminAdmission") patient ptns, Model model) {
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.prepareStatement(
                    "SELECT patientname, patientic FROM patient WHERE patientic = ?");
            statement.setString(1, ptns.getPIc());
            final var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String pName = resultSet.getString("patientname");
                String pIC = resultSet.getString("patientic");
                ptns.setPName(pName);
                ptns.setPIc(pIC);
                System.out.println("name = " + pName);
                System.out.println("ic = " + pIC);
            } else {
                ptns.setPName(resultSet.getString("patientname"));
                ptns.setPIc(resultSet.getString("patientic"));
            }

            List<patient> patientList = new ArrayList<>();
            patientList.add(ptns); // Add the ptns object to the patientList

            model.addAttribute("adminAdmission", ptns); // Add the ptns object to the model
            model.addAttribute("patientList", patientList); // Add the patientList to the model

            return "admin/admissionIn";
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
        return "admin/admissionIn";
    }

}
