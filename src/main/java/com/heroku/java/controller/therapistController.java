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
import com.heroku.java.model.precord;
import com.heroku.java.model.therapist;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
    // =============================== CREATE PROGRESSION ========================

    @GetMapping("/create-progression")
    public String createP() {
        // model.addAttribute("user", model);
        return "therapist/create-progression";
    }

    @PostMapping("/create-progression")
    public String intsertProgress(@ModelAttribute("precord") precord record) {
        // model.addAttribute("user", model);
        try {
            Connection connection = dataSource.getConnection();
            String insertPatientRecordSql = "INSERT INTO patient_record(recorddate, activities,  admissionid) VALUES(?,?,?);";
            PreparedStatement insertPatientRecordStatement = connection.prepareStatement(insertPatientRecordSql);
            insertPatientRecordStatement.setDate(1, record.getRdate());
            insertPatientRecordStatement.setString(2, record.getRactivities());
            insertPatientRecordStatement.setInt(3, record.getAdmissionid());

            insertPatientRecordStatement.execute();
            return "therapist/create-progression";
        } catch (SQLException sqe) {
            System.out.println("Error Code: " + sqe.getErrorCode());
            System.out.println("SQL State: " + sqe.getSQLState());
            System.out.println("Message: " + sqe.getMessage());
            sqe.printStackTrace();
            return "therapist/create-progression";
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "therapist/create-progression";
        }

    }
    // =============================== READ PROGRESSION ==========================

    @GetMapping("/therapist-update-patient")
    public String therapist_update_patient() {
        // model.addAttribute("user", model);
        return "therapist/therapist-update-patient";
    }

    @GetMapping("/therapist-patientlist")
    public String viewPatientRecord(HttpSession session, precord rec, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            final var resultSet = statement.executeQuery(
                    "SELECT a.admissionid, p.patientname, r.* FROM admission a JOIN patient_record r ON (a.admissionid = r.admissionid) JOIN patient p ON (a.patientid = p.patientid) ORDER BY a.admissionid;");

            ArrayList<precord> recordList = new ArrayList<>();
            while (resultSet.next()) {
                int Rid = resultSet.getInt("recordid");
                Date Rdate = resultSet.getDate("recorddate");
                String Ractivities = resultSet.getString("activities");
                int admissionid = resultSet.getInt("admissionid");

                precord record = new precord(Rid, Rdate, Ractivities, admissionid);
                recordList.add(record);
            }

            model.addAttribute("precord", recordList);
            return "therapist/therapist-patientlist";

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
        return "therapist/therapist-patientlist";
    }

    @PostMapping("/therapist-patientlist")
    public String therapistPlist() {
        return "therapist/therapist-patientlist";
    }
}
