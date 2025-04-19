package ru.mazemadness.maze_madness.config.web;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.mazemadness.maze_madness.service.web.MazeUserDetailsService;
import ru.mazemadness.maze_madness.utils.JwtTokenUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final MazeUserDetailsService mazeUserDetailsService;
    private final JwtTokenUtils jwtTokenUtils;


    private String getJwtFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("JWT")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
        String username;
        String jwt = getJwtFromCookies(request);

//        if (request.getCookies() != null){
//            for (Cookie cookie : request.getCookies()) {
//                if (cookie.getName().equals("JWT")) {
//                    jwt = cookie.getValue();
//                }
//            }
//        }

        if (jwt == null){
            filterChain.doFilter(request, response);
            return;
        }

//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwt = authHeader.substring(7);
//            try {
//                username = jwtTokenUtils.getJwtUsername(jwt);
//                log.debug("Username: {}", username);
//            } catch (ExpiredJwtException e) {
//                log.error("Token has expired.");
//            }
//        }
        username = jwtTokenUtils.getJwtUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = mazeUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtils.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        jwtTokenUtils.getJwtRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }
}
