package ru.mazemadness.maze_madness.entities;

import jakarta.persistence.*;
import lombok.Data;
import ru.mazemadness.maze_madness.dto.socket.PlayerDto;

import java.util.Collection;

@Entity
@Data
@Table(name = "maze_users")
public class MazeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Embedded
    private PlayerDto player;

    @ManyToMany
    @JoinTable(
            name = "maze_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<MazeRole> mazeRoles;

}
