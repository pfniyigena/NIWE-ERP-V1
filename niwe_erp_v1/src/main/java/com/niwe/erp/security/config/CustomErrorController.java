package com.niwe.erp.security.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest webRequest, Model model) {
        Map<String, Object> errors = errorAttributes.getErrorAttributes(
                webRequest, ErrorAttributeOptions.defaults());

        int status = (int) errors.getOrDefault("status", 500);
        String message = (String) errors.getOrDefault("error", "Unexpected error");

        model.addAttribute("status", status);
        model.addAttribute("message", message);

        if (status == 404) {
            return "error/404"; // 👈 show custom 404 page
        } else if (status == 403) {
            return "error/403"; // reuse 403 page if you want
        } else {
            return "error/500"; // generic error page
        }
    }
}
