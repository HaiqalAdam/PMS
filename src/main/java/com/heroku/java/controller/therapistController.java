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
import com.heroku.java.model.drug_usage;
import com.heroku.java.model.patient;
import com.heroku.java.model.patientAdmission;

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
public class therapistController {
    private final DataSource dataSource;

    @Autowired
    public therapistController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/dashboard-therapist")
    public String dashboardTherapist(Model model) {
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
        return "therapist/dashboard-therapist";
    }

    @GetMapping("/therapist-progression")
    public String therapist_progression(HttpSession session, patient ptns, drug_usage drug_usage, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();

            final var resultSet = statement.executeQuery(
                    "SELECT * FROM patient ORDER BY patientid;");

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

            final var statement2 = connection.createStatement();

            final var resultSet2 = statement2.executeQuery(
                    "SELECT * FROM drug_usage ORDER BY drugid;");

            // int row = 0;
            ArrayList<drug_usage> Drug = new ArrayList<>();
            while (resultSet2.next()) {
                int dId = resultSet2.getInt("drugid");
                int pId = resultSet2.getInt("patientid");

                // System.out.println("drug ID :" + dId);
                // System.out.println("patient ID :" + pId);

                drug_usage drugs = new drug_usage(dId, pId);
                Drug.add(drugs);
            }
            model.addAttribute("drug_usage", Drug);
            // connection.close();
            return "therapist/therapist-progression";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "therapist/dashboard-therapist";
        }

    }

    @GetMapping("/therapist-patient")
    public String therapistpatient(@RequestParam("pid") Integer pid, HttpSession session,
            patient ptns, drug_usage drug_usage, Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM patient WHERE patientid = ?;";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, pid);

            final var resultSet = statement.executeQuery();

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

            final var statement2 = connection.createStatement();

            final var resultSet2 = statement2.executeQuery(
                    "SELECT * FROM drug_usage ORDER BY drugid;");

            // int row = 0;
            ArrayList<drug_usage> Drug = new ArrayList<>();
            while (resultSet2.next()) {
                int dId = resultSet2.getInt("drugid");
                int pId = resultSet2.getInt("patientid");

                // System.out.println("drug ID :" + dId);
                // System.out.println("patient ID :" + pId);

                drug_usage drugs = new drug_usage(dId, pId);
                Drug.add(drugs);
            }
            model.addAttribute("drug_usage", Drug);
            // connection.close();
            return "therapist/therapist-patient";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "therapist/dashboard-therapist";
        }
    }

    @PostMapping("/therapist-patient")
    public String therapistP() {
        return "therapist/therapist-patient";
    }

    @GetMapping("/therapist-update-patient")
    public String therapist_update_patient() {
        // model.addAttribute("user", model);
        return "therapist/therapist-update-patient";
    }

    @GetMapping("/therapist-view-admission")
    public String therapistViewAdmission(patientAdmission pa, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            final var resultSet = statement.executeQuery(
                    "SELECT a.admissionid, p.patientname, p.patientsex, p.patientbloodtype, p.patientstatus, p.patientdob FROM patient p JOIN admission a ON (p.patientid = a.patientid) ORDER BY a.admissionid;");

            ArrayList<patientAdmission> patientAdmissionList = new ArrayList<>();
            while (resultSet.next()) {
                int admissionid = resultSet.getInt("admissionid");
                String patientname = resultSet.getString("patientname");
                String patientsex = resultSet.getString("patientsex");
                String patientbloodtype = resultSet.getString("patientbloodtype");
                String patientstatus = resultSet.getString("patientstatus");
                Date patientdob = resultSet.getDate("patientdob");

                patientAdmission patientAdmissionListData = new patientAdmission(admissionid, patientname, patientsex,
                        patientbloodtype, patientstatus, patientdob);
                patientAdmissionList.add(patientAdmissionListData);
            }

            model.addAttribute("patientAdmission", patientAdmissionList);

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
        return "therapist/dashboard-therapist";
    }
}
