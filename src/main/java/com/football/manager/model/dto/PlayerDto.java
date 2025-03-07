package com.football.manager.model.dto;

import com.football.manager.model.constant.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {

    @Schema(description = "The identifier of the player.", example = "1")
    private Long id;

    @Schema(description = "The name of the player.", example = "Kylian Mbappe")
    private String name;

    @Schema(description = "The position of the player on the team.", example = "Forward")
    private Position position;
}
