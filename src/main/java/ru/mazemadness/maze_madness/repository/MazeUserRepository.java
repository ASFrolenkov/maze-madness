package ru.mazemadness.maze_madness.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mazemadness.maze_madness.entities.MazeUser;

import java.util.Optional;

@Repository
public interface MazeUserRepository extends CrudRepository<MazeUser, Long> {
    Optional<MazeUser> findByUsername(String username);
}
