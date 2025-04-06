package ru.mazemadness.maze_madness.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mazemadness.maze_madness.dto.JwtRequest;
import ru.mazemadness.maze_madness.dto.RegistrationUserDto;
import ru.mazemadness.maze_madness.service.web.MazeAuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MazeAuthService authService;

    @PostMapping(value = "/v1/login")
    @CrossOrigin(origins = {"*"})
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.createAuthToken(authRequest);
    }

    @PostMapping(value = "/v1/register")
    @CrossOrigin(origins = {"*"})
    public ResponseEntity<?> mazeUserRegister(@RequestBody RegistrationUserDto registrationUserDto){
        return authService.createNewUser(registrationUserDto);
    }
}
