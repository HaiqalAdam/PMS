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

@GetMapping("/staff-update-patient")
    public String staff_update_patient() {
        // model.addAttribute("user", model);
        return "staff/staff-update-patient";
    }

}