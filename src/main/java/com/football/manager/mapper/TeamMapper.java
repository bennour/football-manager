package com.football.manager.mapper;

import com.football.manager.model.dto.TeamDto;
import com.football.manager.model.entity.Team;
import com.football.manager.model.request.TeamRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface TeamMapper {

    /**
     * Maps a Team entity to a TeamDto, ignoring the players field.
     * This method is used when we want to lazily map a Team object without including
     * the players data, typically for performance optimization when players are not needed.
     *
     * @param team The Team entity that needs to be converted to a TeamDto.
     * @return The mapped TeamDto containing the details of the Team, without players.
     */
    @Mapping(target = "players", ignore = true)
    TeamDto lazyMap(Team team);

    /**
     * Maps a Team entity to a TeamDto.
     * This method converts a full Team object from the domain model
     * that includes all relevant details, including the players.
     *
     * @param team The Team entity that needs to be converted to a TeamDto.
     * @return The mapped TeamDto containing the full details of the Team, including players.
     */
    TeamDto map(Team team);

    /**
     * Maps a TeamRequest DTO to a Team entity.
     *
     * @param teamRequest The TeamRequest DTO containing the input data.
     * @return The mapped Team entity that can be saved to the database.
     */
    Team map(TeamRequest teamRequest);

    /**
     * After mapping a Team entity, this method ensures that all players
     * in the team have their `team` field correctly set to maintain consistency.
     * This is useful to prevent detached entity issues in JPA.
     *
     * @param team The mapped Team entity to which players need to be linked.
     */
    @AfterMapping
    default void linkPlayerToTeam(@MappingTarget Team team) {
        if (team.getPlayers() != null) {
            team.getPlayers().forEach(player -> player.setTeam(team));
        }
    }
}
