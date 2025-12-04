package com.niwe.erp.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied(@RequestParam(required = false) String message,
                               Model model) {
        model.addAttribute("errorMessage",
                message != null ? message : "You don’t have permission to perform this action.");
        return "error/403";
    }
}

