package com.company.librarymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lms")
public class SiteController {

    @GetMapping("/")
    public String getAuthorsPage() {
        return "site";
    }

}
