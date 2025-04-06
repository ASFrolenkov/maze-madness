package ru.mazemadness.maze_madness.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
