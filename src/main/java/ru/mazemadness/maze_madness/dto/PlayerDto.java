package ru.mazemadness.maze_madness.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PlayerDto {
    private short id;
    private String name;
    private float x;
    private float y;
    private String playerColor = "0x000000";
}
