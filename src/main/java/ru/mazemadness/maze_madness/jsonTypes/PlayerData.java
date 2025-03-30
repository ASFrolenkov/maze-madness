package ru.mazemadness.maze_madness.jsonTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerData {
    private short id;
    private String name;
    private float x;
    private float y;
    private String playerColor = "0x000000";

    @Override
    public String toString() {
        return "PlayerData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", playerColor='" + playerColor + '\'' +
                '}';
    }
}
