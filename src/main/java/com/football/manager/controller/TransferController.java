package com.football.manager.controller;

import com.football.manager.model.request.TransferRequest;
import com.football.manager.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService service;

    @Operation(summary = "Transfer a player between teams.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player successfully transferred."),
            @ApiResponse(responseCode = "400", description = "Invalid transfer request."),
            @ApiResponse(responseCode = "404", description = "Player or team not found."),
            @ApiResponse(responseCode = "422", description = "Transfer failed due to insufficient funds.")
    })
    @PostMapping
    public void transferPlayer(@RequestBody TransferRequest transferRequest) {
        service.transfer(
                transferRequest.getPlayerId(),
                transferRequest.getSellerTeamId(),
                transferRequest.getBuyerTeamId(),
                transferRequest.getTransferAmount()
        );
    }
}
