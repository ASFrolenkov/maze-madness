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
    private float x;
    private float y;
    private String anim;
    private String playerColor = "0x000000";

    @Override
    public String toString() {
        return "PlayerData{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", anim='" + anim + '\'' +
                ", playerColor='" + playerColor + '\'' +
                '}';
    }
}
