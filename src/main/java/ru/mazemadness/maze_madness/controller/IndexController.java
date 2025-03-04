package ru.mazemadness.maze_madness.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping(path = "/")
    public String getIndex(){
        return "index";
    }
}
