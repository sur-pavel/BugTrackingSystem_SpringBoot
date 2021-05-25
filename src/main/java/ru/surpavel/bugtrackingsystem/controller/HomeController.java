package ru.surpavel.bugtrackingsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${msg.title}")
    private String title;
    
    @GetMapping(value = {"/", "/index"})

    public String index(Model model) {

        model.addAttribute("title", title);

        return "index";

    }
}
