package ru.mazemadness.maze_madness.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MazeUserDto {
    private Long id;
    private String username;
    private String email;
}
