package ru.mazemadness.maze_madness.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mazemadness.maze_madness.entities.MazeRole;

import java.util.Optional;

@Repository
public interface MazeRoleRepository extends CrudRepository<MazeRole, Integer> {
    Optional<MazeRole> findByName(String name);
}
