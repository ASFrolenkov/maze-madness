package ru.mazemadness.maze_madness.dto.auth;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
//    private boolean rememberMe;
}
