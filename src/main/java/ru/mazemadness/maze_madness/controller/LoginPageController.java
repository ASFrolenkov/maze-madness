package ru.mazemadness.maze_madness.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginPageController {
    @GetMapping(path = "/login")
    public String getLoginPage() {
        return "loginPage";
    }
}
