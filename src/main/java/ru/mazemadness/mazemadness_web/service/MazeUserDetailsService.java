package ru.mazemadness.mazemadness_web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mazemadness.mazemadness_web.entities.MazeUser;
import ru.mazemadness.mazemadness_web.repository.MazeUserRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MazeUserDetailsService implements UserDetailsService {
    private final MazeUserRepository mazeUserRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        MazeUser user = mazeUserService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
//                String.format("User '%s' not found", username)
//        ));
        MazeUser user = mazeUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getMazeRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
}
