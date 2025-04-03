package ru.mazemadness.mazemadness_web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mazemadness.mazemadness_web.entities.MazeRole;

import java.util.Optional;

@Repository
public interface MazeRoleRepository extends CrudRepository<MazeRole, Integer> {
    Optional<MazeRole> findByName(String name);
}
