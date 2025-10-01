package com.example.likelionds_BE1.domain.home.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home/home"; // src/main/resources/templates/home/home.html
    }
}
