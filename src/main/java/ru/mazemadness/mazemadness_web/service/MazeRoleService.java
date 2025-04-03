package ru.mazemadness.mazemadness_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mazemadness.mazemadness_web.entities.MazeRole;
import ru.mazemadness.mazemadness_web.repository.MazeRoleRepository;

@Service
@RequiredArgsConstructor
public class MazeRoleService {
    private final MazeRoleRepository mazeRoleRepository;

    public MazeRole getUserRole(){
        return mazeRoleRepository.findByName("ROLE_USER").get();
    }
}
