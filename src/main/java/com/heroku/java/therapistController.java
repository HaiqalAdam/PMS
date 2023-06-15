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
public class therapistController {
    private final DataSource dataSource;

    @Autowired
    public therapistController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

@GetMapping("/therapist-progression")
    public String therapist_progression() {
        // model.addAttribute("user", model);
        return "therapist/therapist-progression";
    }

@GetMapping("/therapist-patient")
    public String therapist_patient() {
        // model.addAttribute("user", model);
        return "therapist/therapist-patient";
    }
    
@GetMapping("/therapist-update-patient")
    public String therapist_update_patient() {
        // model.addAttribute("user", model);
        return "therapist/therapist-update-patient";
    }
}
