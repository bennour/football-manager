package com.football.manager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {

    @Schema(description = "The identifier of the team.", example = "1")
    private Long id;

    @Schema(description = "The name of the team.", example = "Paris Saint-Germain")
    private String name;

    @Schema(description = "The acronym of the team.", example = "PSG")
    private String acronym;

    @Schema(description = "The list of players associated with the team.")
    private List<PlayerDto> players;

    @Schema(description = "The team's budget in millions.", example = "150")
    private double budget;
}
