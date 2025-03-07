package com.football.manager.model.request;

import com.football.manager.model.constant.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {

    @NotNull(message = "Player name cannot be null.")
    @Schema(description = "The name of the player.", example = "Kylian Mbappe")
    private String name;

    @NotNull(message = "Player position cannot be null.")
    @Schema(description = "The position of the player on the team.", example = "Forward")
    private Position position;
}
