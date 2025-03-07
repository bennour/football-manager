package com.football.manager.mapper;

import com.football.manager.model.dto.PlayerDto;
import com.football.manager.model.entity.Player;
import com.football.manager.model.request.PlayerRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    /**
     * Maps a Player entity to a PlayerDto.
     *
     * @param player The Player entity that needs to be converted.
     * @return The mapped PlayerDto containing the details of the Player.
     */
    PlayerDto map(Player player);

    /**
     * Maps a Player request to a Player entity.
     *
     * @param playerRequest The player request that needs to be converted.
     * @return The mapped Player entity containing the details of the Player.
     */
    Player map(PlayerRequest playerRequest);
}
