package com.example.RideShare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
//@RequestMapping("/")
public class templateController {

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }

}
