package ru.mazemadness.mazemadness_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping("/")
    public String index(){
        return "index.html";
    }

    @GetMapping("/admin/secured")
    public String securedAdmin(){
        return "secured.html";
    }
}
