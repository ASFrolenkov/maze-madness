package ru.mazemadness.maze_madness.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "maze_roles")
public class MazeRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
}
