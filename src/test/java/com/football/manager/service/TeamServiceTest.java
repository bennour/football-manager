package com.football.manager.service;

import com.football.manager.model.constant.Position;
import com.football.manager.model.dto.PageDto;
import com.football.manager.model.dto.PlayerDto;
import com.football.manager.model.dto.TeamDto;
import com.football.manager.model.request.PlayerRequest;
import com.football.manager.model.request.TeamRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService service;

    @Test
    void shouldGetAllTeamsOrderedByAcronym() {
        Sort sort = Sort.by(Sort.Direction.ASC, "acronym");
        Pageable pageable = PageRequest.of(0, 10, sort);

        PageDto<TeamDto> teams = service.getTeams(pageable);

        assertThat(teams.getContent())
                .hasSize(3)
                .extracting(TeamDto::getAcronym)
                .containsExactlyInAnyOrder("PSG", "OM", "OGC");
    }

    @Test
    void shouldGetPaginateTeams() {
        PageDto<TeamDto> teams = service.getTeams(PageRequest.of(0, 2));

        assertThat(teams.getContent())
                .hasSize(2)
                .extracting(TeamDto::getAcronym)
                .containsExactlyInAnyOrder("PSG", "OGC");

        teams = service.getTeams(PageRequest.of(1, 2));

        assertThat(teams.getContent())
                .hasSize(1)
                .extracting(TeamDto::getAcronym)
                .containsExactlyInAnyOrder("OM");
    }

    @Test
    void shouldGetTeamById() {
        assertThat(service.getTeamById(1L))
                .returns("PSG", TeamDto::getAcronym);
    }

    @Test
    void shouldGetTeamByIdButUnknownId() {
        assertThatThrownBy(() -> service.getTeamById(9L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Team not found with id: 9");
    }

    @Test
    void shouldGetTeamByIdWithPlayers() {
        TeamDto team = service.getTeamByIdWithPlayers(1L);

        assertThat(team.getPlayers()).hasSize(3);
    }

    @Test
    void shouldCreateTeam() {
        List<PlayerRequest> playersRequest = List.of(
                new PlayerRequest("Player A", Position.FORWARD),
                new PlayerRequest("Player B", Position.DEFENDER)
        );
        TeamRequest teamRequest = new TeamRequest("Lyon", "OL", playersRequest, 12.0);

        TeamDto team = service.createTeam(teamRequest);

        assertThat(team)
                .returns("Lyon", TeamDto::getName)
                .returns("OL", TeamDto::getAcronym)
                .returns(12.0, TeamDto::getBudget);

        assertThat(team.getPlayers()).hasSize(2);
    }

    @Test
    void shouldAddPlayer() {
        Long teamId = 1L;
        PlayerRequest playerRequest = new PlayerRequest("Player A", Position.FORWARD);

        PlayerDto player = service.addPlayer(teamId, playerRequest);

        assertThat(player)
                .returns("Player A", PlayerDto::getName)
                .returns(Position.FORWARD, PlayerDto::getPosition);

        TeamDto team = service.getTeamByIdWithPlayers(teamId);
        assertThat(team.getPlayers())
                .hasSize(4)
                .extracting(PlayerDto::getName)
                .containsOnlyOnce("Player A");
    }
}