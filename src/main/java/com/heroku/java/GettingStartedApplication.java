package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String adminlogin() {
        return "admin/adminlogin";
    }

    @GetMapping("/successful")
    public String greetingForm(Model model) {
        model.addAttribute("staff", new staffUser());
        return "successful";
    }

    @PostMapping("/successful")
    public String greetingSubmit(@ModelAttribute staffUser staff, Model model) {
        model.addAttribute("staff", staff);
        System.out.println("Staff data-------- : " + staff);
        return "succesful";
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }
}
