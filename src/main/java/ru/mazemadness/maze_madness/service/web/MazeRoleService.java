package ru.mazemadness.maze_madness.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mazemadness.maze_madness.entities.MazeRole;
import ru.mazemadness.maze_madness.repository.MazeRoleRepository;

@Service
@RequiredArgsConstructor
public class MazeRoleService {
    private final MazeRoleRepository mazeRoleRepository;

    public MazeRole getUserRole(){
        return mazeRoleRepository.findByName("ROLE_USER").get();
    }
}
