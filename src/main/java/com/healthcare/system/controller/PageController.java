package com.healthcare.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // @GetMapping("/dashboard")
    // public String dashboard() {
    //     return "dashboard";
    // }

    @GetMapping("/bookPage")
    public String bookPage() {
        return "book";
    }

    @GetMapping("/viewPage")
    public String viewPage() {
        return "view";
    }
}