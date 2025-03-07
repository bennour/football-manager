package com.football.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "Player identifier cannot be null.")
    @Schema(description = "The identifier of the player being transferred.", example = "1")
    private Long playerId;

    @NotNull(message = "Seller team identifier cannot be null.")
    @Schema(description = "The identifier of the team selling the player.", example = "2")
    private Long sellerTeamId;

    @NotNull(message = "Buyer team identifier cannot be null.")
    @Schema(description = "The identifier of the team buying the player.", example = "3")
    private Long buyerTeamId;

    @NotNull(message = "Transfer amount cannot be null.")
    @Schema(description = "The amount for the transfer.", example = "20")
    private Long transferAmount;
}
