package ru.mazemadness.maze_madness.dto;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;
    private String sender;
    private String content;
    private long timestamp = System.currentTimeMillis();
}
