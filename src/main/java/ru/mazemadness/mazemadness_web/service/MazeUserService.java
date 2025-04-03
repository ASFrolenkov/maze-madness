package ru.mazemadness.mazemadness_web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mazemadness.mazemadness_web.dto.RegistrationUserDto;
import ru.mazemadness.mazemadness_web.entities.MazeUser;
import ru.mazemadness.mazemadness_web.repository.MazeUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MazeUserService {

    private final MazeUserRepository mazeUserRepository;
    private final MazeRoleService mazeRoleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<MazeUser> findByUsername(String username){
        return mazeUserRepository.findByUsername(username);
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        MazeUser user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
//                String.format("User '%s' not found", username)
//        ));
//        return new User(
//                user.getUsername(),
//                user.getPassword(),
//                user.getMazeRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
//        );
//    }

    public MazeUser createNewMazeUser(RegistrationUserDto registrationUserDto){
        MazeUser user = new MazeUser();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setMazeRoles(List.of(mazeRoleService.getUserRole()));
        return mazeUserRepository.save(user);
    }

}
