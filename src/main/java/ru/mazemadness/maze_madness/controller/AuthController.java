package ru.mazemadness.maze_madness.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.mazemadness.maze_madness.dto.auth.JwtRequest;
import ru.mazemadness.maze_madness.dto.auth.RegistrationUserDto;
import ru.mazemadness.maze_madness.service.web.MazeAuthService;
import ru.mazemadness.maze_madness.service.web.MazeUserDetailsService;
import ru.mazemadness.maze_madness.utils.JwtTokenUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {
    private final MazeAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final MazeUserDetailsService mazeUserDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/auth/login")
    public ResponseEntity<String> authenticate(@RequestBody JwtRequest request, HttpServletResponse response) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(),
                            new ArrayList<>()));
            final UserDetails user = mazeUserDetailsService.loadUserByUsername(request.getUsername());
            if (user != null) {
                String jwt = jwtTokenUtils.generateJwtToken(user); // 7 days
                Cookie cookie = new Cookie("JWT", jwt);// Global
                cookie.setMaxAge(1 * 24 * 60 * 60);
                cookie.setSecure(false);
                cookie.setHttpOnly(true);
                cookie.setPath("/"); // Global
                cookie.setAttribute("SameSite", "Lax");
                response.addCookie(cookie);
                return ResponseEntity.ok(jwt);

            }
            return ResponseEntity.status(400).body("Error authenticating");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(400).body("" + e.getMessage());
        }
    }

    @PostMapping(value = "/auth/register")
    public ResponseEntity<?> mazeUserRegister(@RequestBody RegistrationUserDto registrationUserDto){
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping(value = "/auth/logout")
    public ResponseEntity<String> mazeUserLogout(HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            SecurityContextHolder.clearContext();
        }

        Cookie cookie = new Cookie("JWT", "");// Global
        cookie.setMaxAge(0);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // Global
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);

        return ResponseEntity.ok().body("You have been logged out!");
    }

    @PostMapping(value = "/auth/validate")
    public ResponseEntity<?> tokenValidate(HttpServletRequest request){
        String token = jwtTokenUtils.getJwtFromCookies(request);

        if (token == null || !jwtTokenUtils.validateJwtToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtTokenUtils.getJwtUsername(token);

        UserDetails userDetails = mazeUserDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().body(username);
    }

}
