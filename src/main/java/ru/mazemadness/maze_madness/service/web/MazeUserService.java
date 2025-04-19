package ru.mazemadness.maze_madness.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mazemadness.maze_madness.dto.socket.PlayerDto;
import ru.mazemadness.maze_madness.dto.auth.RegistrationUserDto;
import ru.mazemadness.maze_madness.entities.MazeUser;
import ru.mazemadness.maze_madness.repository.MazeUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MazeUserService {

    private final MazeUserRepository mazeUserRepository;
    private final MazeRoleService mazeRoleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<MazeUser> findByUsername(String username){
        return mazeUserRepository.findByUsername(username);
    }

    public MazeUser createNewMazeUser(RegistrationUserDto registrationUserDto){
        MazeUser user = new MazeUser();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setMazeRoles(List.of(mazeRoleService.getUserRole()));
        // new
        user.setPlayer(new PlayerDto());
        return mazeUserRepository.save(user);
    }

}
