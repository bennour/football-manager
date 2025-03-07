package com.football.manager.repository;


import com.football.manager.model.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * This method retrieves the team with the players using an entity graph.
     *
     * @param id The identifier of the team.
     * @return The team wrapped in an Optional.
     */
    @EntityGraph(attributePaths = "players")
    Optional<Team> findTeamWithPlayersById(Long id);
}
