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
import com.heroku.java.model.drug_usage;
import com.heroku.java.model.patient;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public String showPatientDrug(HttpSession session, patient ptns, drug_usage drug_usage, Model model) {

        // if (session.getAttribute("name") != null) {
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
            return "staff/staff-patient";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "staff/staffmainmenu";
        }

    }

    @GetMapping("/staff-register-patient")
    public String registerPatient() {
        // model.addAttribute("user", model);
        return "staff/staff-register-patient";
    }

    @PostMapping("/staff-register-patient")
    public String staffRegisterPatient(@ModelAttribute("patient") patient patient,
            @RequestParam("pDrugType") List<String> drugType) {
        System.out.println("type of drug : " + drugType);
        try {
            Connection connection = dataSource.getConnection();
            String insertPatientSql = "INSERT INTO patient(patientname, patientic, patientsex, patientaddress, patientdate, patientstatus, patientdob, patientphoneno, patientbloodtype) VALUES(?,?,?,?,?,?,?,?,?) RETURNING patientid";
            PreparedStatement insertPatientStatement = connection.prepareStatement(insertPatientSql);
            insertPatientStatement.setString(1, patient.getPName());
            insertPatientStatement.setString(2, patient.getPIc());
            insertPatientStatement.setString(3, patient.getPSex());
            insertPatientStatement.setString(4, patient.getPAddress());
            insertPatientStatement.setDate(5, patient.getPDate());
            insertPatientStatement.setString(6, patient.getPStatus());
            insertPatientStatement.setDate(7, patient.getPDOB());
            insertPatientStatement.setString(8, patient.getPPhoneNo());
            insertPatientStatement.setString(9, patient.getPBloodType());
            insertPatientStatement.execute();

            ResultSet generatedKeys = insertPatientStatement.getResultSet();
            int patientId = 0;
            if (generatedKeys.next()) {
                patientId = generatedKeys.getInt(1);
            }

            System.out.println("patient id " + patientId);

            for (String value : drugType) {
                String updateBridgeSql = "INSERT INTO drug_usage (patientid, drugid) VALUES (?, ?)";
                PreparedStatement insertBridgeStatement = connection.prepareStatement(updateBridgeSql);
                insertBridgeStatement.setInt(1, patientId);
                insertBridgeStatement.setInt(2, Integer.parseInt(value));
                insertBridgeStatement.executeUpdate();
                System.out.println(value);
            }
            connection.close();

            return "redirect:/staff-register-patient";
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            System.out.println("error");
        }

        return "staff/staffmainmenu";
    }

    // UPDATE PATIENT
    @GetMapping("/staff-update-patient")
    public String showUpdatePatient(@ModelAttribute("patient") drug_usage drug_usage, @RequestParam("id") int patientid,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM patient WHERE patientid = ?;";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, patientid);

            final var resultSet = statement.executeQuery();
            patient patients = new patient();

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
                // String pdDrugType = resultSet.getString("drugtype");

                System.out.println("patient ID :" + pId);
                System.out.println("patient name :" + pName);

                patients = new patient(pId, pName, pIc, pSex, pAddress, pDate, pStatus, pDOB, pPhoneNo, pBloodType);
            }
            model.addAttribute("patient", patients);

            Connection connection2 = dataSource.getConnection();
            String sql2 = "SELECT drugid FROM drug_usage WHERE patientid = ?;";
            final var statement2 = connection2.prepareStatement(sql2);
            statement2.setInt(1, patientid);

            List<Integer> drugs = new ArrayList<>();
            try (ResultSet resultSet2 = statement2.executeQuery()) {
                while (resultSet2.next()) {
                    int dId = resultSet2.getInt("drugid");
                    System.out.println("drug ID :" + dId);
                    drugs.add(dId);
                }
            }
            System.out.println(drugs.isEmpty());
            model.addAttribute("drugUsage", drugs);

            return "staff/staff-update-patient";
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "staff/staffmainmenu";
        }
    }

    @PostMapping("/staff-update-patient")
    String updatePatient(Model model, @ModelAttribute("patient") patient patient,
            @RequestParam(name = "pId") int patientId,
            @RequestParam(name = "pDrugType", required = false) List<String> drugType) {
        System.out.println("type of drug : " + drugType);

        patient.setPId(patientId);

        boolean statusDelete = false;
        boolean statusUpdate = false;

        try (Connection connection = dataSource.getConnection()) {

            // Update patient
            String updatePatientQuery = "UPDATE patient SET patientname = ?, patientic = ?, patientsex = ?, patientaddress = ?, patientdate = ?, patientstatus = ?,patientdob = ?, patientphoneno = ?, patientbloodtype = ? WHERE patientid = ?";
            try (PreparedStatement updatePatientStatement = connection.prepareStatement(updatePatientQuery)) {
                updatePatientStatement.setString(1, patient.getPName());
                updatePatientStatement.setString(2, patient.getPIc());
                updatePatientStatement.setString(3, patient.getPSex());
                updatePatientStatement.setString(4, patient.getPAddress());
                updatePatientStatement.setDate(5, patient.getPDate());
                updatePatientStatement.setString(6, patient.getPStatus());
                updatePatientStatement.setDate(7, patient.getPDOB());
                updatePatientStatement.setString(8, patient.getPPhoneNo());
                updatePatientStatement.setString(9, patient.getPBloodType());
                updatePatientStatement.setInt(10, patientId);
                int rowsAffected = updatePatientStatement.executeUpdate();
                statusUpdate = rowsAffected > 0;
            }

            // Delete all drugs by patientId
            String deleteDrugQuery = "DELETE FROM drug_usage WHERE patientid = ?";
            try (PreparedStatement deleteDrugStatement = connection.prepareStatement(deleteDrugQuery)) {
                deleteDrugStatement.setInt(1, patientId);
                int rowsAffected = deleteDrugStatement.executeUpdate();
                statusDelete = rowsAffected > 0;
            }
            // Insert new drugs
            String insertDrugQuery = "INSERT INTO drug_usage (patientid, drugid) VALUES (?, ?)";
            try (PreparedStatement insertDrugStatement = connection.prepareStatement(insertDrugQuery)) {
                for (String value : drugType) {
                    insertDrugStatement.setInt(1, patientId);
                    insertDrugStatement.setInt(2, Integer.parseInt(value));
                    insertDrugStatement.executeUpdate();
                }
            }
            connection.close();

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "staff/staffmainmenu";
        }
        return "redirect:/staff-patient";
    }

    // Delete Patient
    @GetMapping("/staff-delete-patient")
    public String DeletePatient(@ModelAttribute("patient") patient patient, @RequestParam("pId") int ptnid,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM patient WHERE patientid=?;";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, ptnid);

            // execute delete
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Deletion successful
                return "redirect:/staff-patient";
            } else {
                // No rows affected, account not found
                return "account-not-found";
            }
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "redirect:/staffmainmenu";
        }

    }
}