package ru.mazemadness.maze_madness.dto.socket;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
@ToString
public class PlayerDto {
    private String playerSessionId;
    private String name;
    private float spawnX;
    private float spawnY;
    private String playerColor = "0x000000";

    public PlayerDto(String playerSessionId) {
        this.playerSessionId = playerSessionId;
    }
}
