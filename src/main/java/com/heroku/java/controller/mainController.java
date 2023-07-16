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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heroku.java.model.drug;
import com.heroku.java.model.drug_usage;
import com.heroku.java.model.patient;
import com.heroku.java.model.patientdrug;
import com.heroku.java.model.staff;
import com.heroku.java.model.staffemp;
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
import java.util.Collections;
import java.util.HashMap;
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
    public String showDashboard(Model model) {
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.createStatement();
            final var resultSet = statement.executeQuery(
                    "SELECT COUNT(*) AS count FROM therapist;");
            resultSet.next();
            int count = resultSet.getInt("count");

            model.addAttribute("therapistCount", count);

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
        return "admin/adminmainmenu";
    }

    // ======================================================THERAPIST=================================================================
    @GetMapping("/therapist")
    public String viewTherapist(HttpSession session, therapist thr, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            final var resultSet = statement.executeQuery(
                    "SELECT e.id, t.* FROM employee e JOIN therapist t ON (e.id = t.id) ORDER BY e.id;");

            ArrayList<therapist> therapistList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tName = resultSet.getString("therapistname");
                String tRelationStatus = resultSet.getString("therapiststatus");
                String tSex = resultSet.getString("therapistsex");
                Date tDate = resultSet.getDate("therapistdate");
                Date tDOB = resultSet.getDate("therapistdob");
                String tPhoneNo = resultSet.getString("therapistphoneno");
                String tSpecialist = resultSet.getString("therapistspecialist");

                therapist therapists = new therapist(id, null, null, null, tName, tRelationStatus, tDate, tSex, tDOB,
                        tPhoneNo, tSpecialist);
                therapistList.add(therapists);
            }

            model.addAttribute("therapist", therapistList);
            return "admin/therapist";

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
        return "admin/adminmainmenu";
    }

    @GetMapping("/register-therapist")
    public String registerT() {
        // model.addAttribute("user", model);
        return "admin/register-therapist";
    }

    @PostMapping("/register-therapist")
    public String registerTherapist(HttpSession session, therapist t, users u) {
        try (Connection connection = dataSource.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM employee WHERE name = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, u.getUsr());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Name has been used!");
                return "admin/register-therapist";
            }

            String sql = "INSERT INTO employee(name, password, role, managerid) VALUES (?,?,?,?) RETURNING id";
            PreparedStatement statement = connection.prepareStatement(sql);
            String name = u.getUsr();
            String password = u.getPwd();
            String role = "therapist";

            int managerId = (int) session.getAttribute("id");
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.setInt(4, managerId);
            ResultSet aidee = statement.executeQuery();

            int employeeId = 0;
            if (aidee.next()) {
                employeeId = aidee.getInt(1);
            }
            String staffSql = "INSERT INTO therapist(id, therapistname, therapistspecialist, therapistphoneno, therapistdob, therapistdate, therapiststatus, therapistsex) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement statement2 = connection.prepareStatement(staffSql);
            statement2.setInt(1, employeeId);
            statement2.setString(2, t.getTName());
            statement2.setString(3, t.getTSpecialist());
            statement2.setString(4, t.getTPhoneNo());
            statement2.setDate(5, t.getTDOB());
            statement2.setDate(6, t.getTDate());
            statement2.setString(7, t.getTRelationStatus());
            statement2.setString(8, t.getTSex());
            statement2.executeUpdate();
            return "admin/therapist";
        } catch (SQLException sqe) {
            System.out.println("Error Code: " + sqe.getErrorCode());
            System.out.println("SQL State: " + sqe.getSQLState());
            System.out.println("Message: " + sqe.getMessage());
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/update-therapist")
    public String showUpdateTherapist(@ModelAttribute("therapist") therapist trp, @RequestParam("id") int id,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM therapist WHERE id = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            final var resultSet = statement.executeQuery();
            while (resultSet.next()) {

                String tName = resultSet.getString("therapistname");
                String tSpecialist = resultSet.getString("therapistspecialist");
                String tPhoneNo = resultSet.getString("therapistphoneno");
                Date tDOB = resultSet.getDate("therapistdob");
                Date tDate = resultSet.getDate("therapistdate");
                String tRelationStatus = resultSet.getString("therapiststatus");
                String tSex = resultSet.getString("therapistsex");

                therapist therapists = new therapist(id, tName, tRelationStatus, tDate, tSex, tDOB,
                        tPhoneNo, tSpecialist);
                model.addAttribute("therapist", therapists);
            }
            return "admin/update-therapist";
        } catch (SQLException sqe) {
            System.out.println("message : " + sqe.getMessage());

            return "admin/adminmainmenu";
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "admin/adminmainmenu";
        }
    }

    @PostMapping("/update-therapist")
    public String updateTherapist(HttpSession session, therapist Therapist, @RequestParam("tId") int thid,
            Model model) {

        int tId = Therapist.getTId();
        String tName = Therapist.getTName();
        String tRelationStatus = Therapist.getTRelationStatus();
        Date tDate = Therapist.getTDate();
        Date tDOB = Therapist.getTDOB();
        String tSex = Therapist.getTSex();
        String tPhoneNo = Therapist.getTPhoneNo();
        String tSpecialist = Therapist.getTSpecialist();

        System.out.println(tId + " " + tName);
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE therapist SET id = ? ,therapistname = ? , therapiststatus = ?, therapistdate = ?, therapistdob = ?, therapistsex = ?, therapistphoneno = ?, therapistspecialist = ? WHERE id =?";
            final var statement = connection.prepareStatement(sql);

            statement.setInt(1, tId);
            statement.setString(2, tName);
            statement.setString(3, tRelationStatus);
            statement.setDate(4, tDate);
            statement.setDate(5, tDOB);
            statement.setString(6, tSex);
            statement.setString(7, tPhoneNo);
            statement.setString(8, tSpecialist);
            statement.setInt(9, thid);

            statement.executeUpdate();

            System.out.println(tId + " " + tName);
            return "redirect:/therapist";
        } catch (Throwable t) {
            System.out.println("message: " + t.getMessage());
            System.out.println("error");
            return "redirect:/adminmainmenu";
        }
    }

    @GetMapping("/delete-therapist")
    public String deleteTherapist(@ModelAttribute("therapist") therapist therapist, @RequestParam("id") int userid,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM therapist WHERE id=?;";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, userid);

            // execute delete
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Deletion successful
                return "redirect:/therapist";
            } else {
                // No rows affected, account not found
                return "account-not-found";
            }
        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "redirect:/adminmainmenu";
        }
    }

    // ======================================================STAFF=================================================================
    @GetMapping("/staff")
    public String staffView(HttpSession session, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            final var resultSet = statement
                    .executeQuery("SELECT e.id, s.* FROM employee e JOIN staff s ON(e.id = s.id) ORDER BY e.id");

            ArrayList<staff> stafflList = new ArrayList<>();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Date staffDate = resultSet.getDate("staffdate");
                String staffStatus = resultSet.getString("staffstatus");
                String staffSex = resultSet.getString("staffsex");
                Date staffDOB = resultSet.getDate("stafftdob");
                String staffPhoneNo = resultSet.getString("staffphoneno");
                String staffPosition = resultSet.getString("staffposition");
                String staffName = resultSet.getString("staffname");

                staff staffs = new staff(id, null, null, null, staffName, staffStatus, staffDate, staffSex, staffDOB,
                        staffPhoneNo, staffPosition);
                stafflList.add(staffs);
                System.out.println(id);
                System.out.println(staffDate);
            }
            model.addAttribute("staff", stafflList);
            return "admin/staff";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminmainmenu";
        }
    }

    @GetMapping("/register-staff")
    public String registerS() {
        // model.addAttribute("user", model);
        return "admin/register-staff";
    }

    @PostMapping("/register-staff")
    public String registerStaff(HttpSession session, staff s, users u) {
        try (Connection connection = dataSource.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM employee WHERE name = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, u.getUsr());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("name has been used!");
                return "admin/register-staff";
            }

            String sql = "INSERT INTO employee(name, password, role, managerid) VALUES (?,?,?,?) RETURNING id";
            PreparedStatement statement = connection.prepareStatement(sql);
            String name = u.getUsr();
            String password = u.getPwd();
            String roles = "staff";

            int managerid = (int) session.getAttribute("id");
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, roles);
            statement.setInt(4, managerid);
            ResultSet aidee = statement.executeQuery();

            int employeeId = 0;
            if (aidee.next()) {
                employeeId = aidee.getInt(1);
            }
            String staffSql = "INSERT INTO staff(id, staffname, staffposition, staffphoneno, stafftdob, staffdate, staffstatus, staffsex) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement statement2 = connection.prepareStatement(staffSql);
            statement2.setInt(1, employeeId);
            statement2.setString(2, s.getSName());
            statement2.setString(3, s.getSPosition());
            statement2.setString(4, s.getSPhoneNo());
            statement2.setDate(5, s.getSDOB());
            statement2.setDate(6, s.getSDate());
            statement2.setString(7, s.getSRelationStatus());
            statement2.setString(8, s.getSSex());
            statement2.executeUpdate();
            return "admin/register-staff";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/";
        }

    }

    @GetMapping("/update-staff")
    public String showUpdateStaff(HttpSession session, @ModelAttribute("staff") staff clerk, @RequestParam("id") int id,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM staff WHERE id = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int sId = resultSet.getInt("id");
                String sName = resultSet.getString("staffname");
                String sRelationStatus = resultSet.getString("staffstatus");
                Date sDate = resultSet.getDate("staffdate");
                String sSex = resultSet.getString("staffsex");
                Date sDOB = resultSet.getDate("stafftdob");
                String sPhoneNo = resultSet.getString("staffphoneno");
                String sPosition = resultSet.getString("staffposition");

                System.out.println(sId + sName + sRelationStatus + id);

                staff staffs = new staff(sId, sName, sRelationStatus, sDate, sSex, sDOB, sPhoneNo, sPosition);
                model.addAttribute("staff", staffs);
            }

            return "admin/update-staff";
        } catch (Exception e) {
            System.out.println("message: " + e.getMessage());
            System.out.println("error");
        }

        return "admin/update-staff";
    }

    @PostMapping("/update-staff")
    public String updateStaff(HttpSession session, staff Staff, @RequestParam("sId") int stfid, Model model) {

        int sId = Staff.getSId();
        String sName = Staff.getSName();
        String sRelationStatus = Staff.getSRelationStatus();
        Date sDate = Staff.getSDate();
        Date sDOB = Staff.getSDOB();
        String sSex = Staff.getSSex();
        String sPhoneNo = Staff.getSPhoneNo();
        String sPosition = Staff.getSPosition();

        System.out.println(sId + " " + sName);
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE staff SET id = ? ,staffname = ? , staffstatus = ?, staffdate = ?, stafftdob = ?, staffsex = ?, staffphoneno = ?, staffposition = ? WHERE id =?";
            final var statement = connection.prepareStatement(sql);

            statement.setInt(1, sId);
            statement.setString(2, sName);
            statement.setString(3, sRelationStatus);
            statement.setDate(4, sDate);
            statement.setDate(5, sDOB);
            statement.setString(6, sSex);
            statement.setString(7, sPhoneNo);
            statement.setString(8, sPosition);
            statement.setInt(9, stfid);

            statement.executeUpdate();

            System.out.println(sId + " " + sName);
            return "redirect:/staff";
        } catch (Throwable t) {
            System.out.println("message: " + t.getMessage());
            System.out.println("error");
            return "redirect:/adminmainmenu";
        }
    }

    @GetMapping("/delete-staff")
    public String deletetStaff(@ModelAttribute("staff") staff staff, @RequestParam("id") int userid,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM staff WHERE id=?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, userid);

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

    // ======================================================PATIENT=================================================================
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
        } catch (Throwable t) {
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

        } catch (Throwable t) {
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

    @PostMapping("/delete-therapist")
    public String delTherapist(@ModelAttribute("therapist") therapist therapist, @RequestParam("id") int userid,
            Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM therapist WHERE id=?;";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, userid);

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

    @GetMapping("/record")
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

            ArrayList<drug_usage> Drug = new ArrayList<>();
            while (resultSet2.next()) {
                int dId = resultSet2.getInt("drugid");
                int pId = resultSet2.getInt("patientid");


                drug_usage drugs = new drug_usage(dId, pId);
                Drug.add(drugs);
            }
            model.addAttribute("drug_usage", Drug);
            // connection.close();
            return "admin/record";

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            return "admin/adminmainmenu";
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(mainController.class, args);
    }
}
