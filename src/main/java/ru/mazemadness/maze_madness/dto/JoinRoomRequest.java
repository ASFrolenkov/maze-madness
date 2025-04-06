package ru.mazemadness.maze_madness.dto;

import lombok.Data;

@Data
public class JoinRoomRequest {
    private String roomId;
    private String userId;
}
