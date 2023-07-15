package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties.Reactive.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.drug;
import com.heroku.java.model.drug_usage;
import com.heroku.java.model.patient;
import com.heroku.java.model.patientdrug;
import com.heroku.java.model.therapist;
import com.heroku.java.model.users;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class mainController {
    private final DataSource dataSource;

    @Autowired
    public mainController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    @GetMapping("/")
    public String adminlogin(HttpSession session) {
        if (session.getAttribute("usr") != null) {
            return "admin/adminmainmenu";
        } else {
            return "login";
        }
    }

    @PostMapping("/login")
    public String dashboard(HttpSession session, @ModelAttribute("staff") users staff, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM employee WHERE name = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, staff.getUsr());
            statement.setString(2, staff.getPwd());
            ResultSet resultSet = statement.executeQuery();

            String returnPage = "/login";

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                int id = resultSet.getInt("id");

                session.setAttribute("usr", staff.getUsr());
                session.setAttribute("role", role);
                session.setAttribute("id", id);
                System.out.println("user id is" + id);
                if (role.equals("admin")) {
                    returnPage = "redirect:/adminmainmenu";
                } else if (role.equals("therapist")) {
                    returnPage = "redirect:/dashboard-therapist";
                } else if (role.equals("staff")) {
                    returnPage = "redirect:/staffmainmenu";
                }
            }

            return returnPage;

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "/login";
        }
    }

    /**
     * @param session
     * @param staff
     * @param model
     * @return
     */

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("succesfully logout");
        return "redirect:/";
    }

    @GetMapping("/adminmainmenu")
    public String showDashboard(HttpSession session) {
        return "admin/adminmainmenu";
    }

    @GetMapping("/therapist")
    public String viewTherapist(HttpSession session, therapist thr, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            final var resultSet = statement
                    .executeQuery(
                            "SELECT * FROM therapist JOIN employee ON therapist.id = employee.id ORDER BY therapist.id;");

            ArrayList<therapist> therapist = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tName = resultSet.getString("therapistname");
                String tRelationStatus = resultSet.getString("therapiststatus");
                String tSex = resultSet.getString("therapistsex");
                Date tDate = resultSet.getDate("therapistdate");
                Date tDOB = resultSet.getDate("therapistdob");
                String tPhoneNo = resultSet.getString("therapistphoneno");
                String tSpecialist = resultSet.getString("therapistspecialist");

                therapist therapists = new therapist(tName, tRelationStatus, tDate, tSex, tDOB, tPhoneNo, tSpecialist);
                therapist.add(therapists);

            }

            model.addAttribute("therapists", therapist);

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
        return "admin/therapist";
    }

    @GetMapping("/register-therapist")
    public String registerT() {
        // model.addAttribute("user", model);
        return "admin/register-therapist";
    }

    @GetMapping("/update-therapist")
    public String updateT() {
        // model.addAttribute("user", model);
        return "admin/update-therapist";
    }

    // CREATE PATIENT
    @GetMapping("/register-patient")
    public String registerP() {
        // model.addAttribute("user", model);
        return "admin/register-patient";
    }

    @PostMapping("/register-patient")
    public String adminRegisterPatient(@ModelAttribute("patient") patient patient,
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

            return "redirect:/register-patient";
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            System.out.println("error");
        }

        return "admin/adminmainmenu";
    }

    // READ PATIENT
    @GetMapping("/patient")
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
            return "admin/patient";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminmainmenu";
        }

    }

    // UPDATE PATIENT
    @GetMapping("/update-patient")
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

            return "admin/update-patient";
        }   catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminmainmenu";
        }
    }

    @PostMapping("/update-patient")
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
              
        }   catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminmainmenu";
        }
        return "redirect:/patient";
}


    
    // Delete Patient
    @GetMapping("/delete-patient")
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
                return "redirect:/patient";
            } else {
                // No rows affected, account not found
                return "account-not-found";
            }
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "redirect:/adminmainmenu";
        }

    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(mainController.class, args);
    }
}
