package ru.mazemadness.maze_madness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
}
