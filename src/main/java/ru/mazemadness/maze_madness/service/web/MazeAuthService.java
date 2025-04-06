package ru.mazemadness.maze_madness.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.mazemadness.maze_madness.dto.JwtRequest;
import ru.mazemadness.maze_madness.dto.JwtResponse;
import ru.mazemadness.maze_madness.dto.MazeUserDto;
import ru.mazemadness.maze_madness.dto.RegistrationUserDto;
import ru.mazemadness.maze_madness.entities.MazeUser;
import ru.mazemadness.maze_madness.exception.AppError;
import ru.mazemadness.maze_madness.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class MazeAuthService {
    private final MazeUserService mazeUserService;
    private final MazeUserDetailsService mazeUserDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Bad credentials"), HttpStatus.UNAUTHORIZED);
        }
//        UserDetails userDetails = mazeUserService.loadUserByUsername(authRequest.getUsername());
        UserDetails userDetails = mazeUserDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateJwtToken(userDetails);

//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Access-Control-Allow-Origin", "*");
//        return ResponseEntity.ok().headers(responseHeaders).body(new JwtResponse(token));
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto){
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords not equal"), HttpStatus.BAD_REQUEST);
        }
        if (mazeUserService.findByUsername(registrationUserDto.getUsername()).isPresent()){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User already exist"), HttpStatus.BAD_REQUEST);
        }
        MazeUser user = mazeUserService.createNewMazeUser(registrationUserDto);

//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Access-Control-Allow-Origin", "*");
//        return ResponseEntity.ok().headers(responseHeaders).body(new MazeUserDto(user.getId(), user.getUsername(), user.getEmail()));
        return ResponseEntity.ok(new MazeUserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}
