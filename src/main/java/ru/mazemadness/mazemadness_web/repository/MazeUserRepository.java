package ru.mazemadness.mazemadness_web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mazemadness.mazemadness_web.entities.MazeUser;

import java.util.Optional;

@Repository
public interface MazeUserRepository extends CrudRepository<MazeUser, Long> {
    Optional<MazeUser> findByUsername(String username);
}
