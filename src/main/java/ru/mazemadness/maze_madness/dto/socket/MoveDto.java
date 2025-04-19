package ru.mazemadness.maze_madness.dto.socket;

import lombok.*;

@Data
public class MoveDto {
    private String roomName;
    private float x;
    private float y;
}
