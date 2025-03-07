package com.football.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotNull(message = "Team name cannot be null.")
    @Schema(description = "The name of the team.", example = "Paris Saint-Germain")
    private String name;

    @NotNull(message = "Team acronym cannot be null.")
    @Size(min = 2, max = 4, message = "Acronym must be between 2 and 5 characters long.")
    @Schema(description = "The acronym of the team.", example = "PSG")
    private String acronym;

    @Schema(description = "The list of players associated with the team.")
    private List<PlayerRequest> players;

    @NotNull(message = "Team budget cannot be null.")
    @Schema(description = "The team's budget in millions.", example = "150")
    private Double budget;
}
