package com.football.manager.service;

import com.football.manager.mapper.PlayerMapper;
import com.football.manager.mapper.TeamMapper;
import com.football.manager.model.dto.PageDto;
import com.football.manager.model.dto.PlayerDto;
import com.football.manager.model.dto.TeamDto;
import com.football.manager.model.entity.Player;
import com.football.manager.model.entity.Team;
import com.football.manager.model.request.PlayerRequest;
import com.football.manager.model.request.TeamRequest;
import com.football.manager.repository.PlayerRepository;
import com.football.manager.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger logger = LogManager.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final TeamMapper teamMapper;

    private final PlayerMapper playerMapper;

    /**
     * Fetches all teams with pagination.
     *
     * @param pageable the pagination information.
     * @return a PageDto containing a paginated list of TeamDto objects.
     */
    public PageDto<TeamDto> getTeams(Pageable pageable) {
        logger.info("Fetching all teams with pagination. Page size: {}", pageable.getPageSize());

        Page<TeamDto> team = teamRepository.findAll(pageable)
                .map(teamMapper::lazyMap);

        logger.info("Successfully fetched {} teams.", team.getTotalElements());

        return new PageDto<>(team);
    }

    /**
     * Fetches a team by its identifier.
     *
     * @param teamId the identifier of the team.
     * @return the TeamDto corresponding to the provided teamId.
     * @throws EntityNotFoundException if no team is found with the given id.
     */
    public TeamDto getTeamById(Long teamId) {
        logger.info("Fetching team by id: {}", teamId);

        TeamDto team = teamRepository.findById(teamId)
                .map(teamMapper::lazyMap)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        logger.info("Successfully fetched team with id: {}", teamId);

        return team;
    }

    /**
     * Fetches a team with its players by the team's identifier.
     *
     * @param teamId the identifier of the team.
     * @return the TeamDto with players included for the provided teamId.
     * @throws EntityNotFoundException if no team is found with the given id.
     */
    public TeamDto getTeamByIdWithPlayers(Long teamId) {
        logger.info("Fetching team with players by id: {}", teamId);

        TeamDto team = teamRepository.findTeamWithPlayersById(teamId)
                .map(teamMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        logger.info("Successfully fetched team with players with id: {}", teamId);

        return team;
    }

    /**
     * Creates a new team based on the provided team request data.
     *
     * @param teamRequest the team request object containing the team data.
     * @return the created TeamDto.
     */
    public TeamDto createTeam(TeamRequest teamRequest) {
        logger.info("Creating a new team with name: {}", teamRequest.getName());

        Team newTeam = teamMapper.map(teamRequest);
        Team team = teamRepository.save(newTeam);

        logger.info("Successfully created new team with id: {}", team.getId());

        return teamMapper.map(team);
    }

    /**
     * Adds a new player to an existing team.
     *
     * @param teamId        the identifier of the team to which the player is to be added.
     * @param playerRequest the player request object containing the player data.
     * @return the created PlayerDto for the added player.
     * @throws EntityNotFoundException if no team is found with the given teamId.
     */
    public PlayerDto addPlayer(Long teamId, PlayerRequest playerRequest) {
        logger.info("Adding player to team with id: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        Player newPlayer = playerMapper.map(playerRequest);
        newPlayer.setTeam(team);

        Player player = playerRepository.save(newPlayer);

        logger.info("Successfully added player with id: {} to team with id: {}", player.getId(), teamId);

        return playerMapper.map(player);
    }
}
