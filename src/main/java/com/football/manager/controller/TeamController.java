package com.football.manager.controller;

import com.football.manager.model.dto.PageDto;
import com.football.manager.model.dto.PlayerDto;
import com.football.manager.model.dto.TeamDto;
import com.football.manager.model.request.PlayerRequest;
import com.football.manager.model.request.TeamRequest;
import com.football.manager.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;

    @Operation(summary = "Get a paginated list of teams with sorting options.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of teams successfully retrieved."),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @GetMapping
    public PageDto<TeamDto> getTeams(
            @Parameter(description = "Current page of pagination.", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page.", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Field name by which to sort.", example = "name")
            @RequestParam(defaultValue = "name") String sort,

            @Parameter(description = "Sort direction.", example = "asc")
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return service.getTeams(pageable);
    }

    @Operation(summary = "Get a team details by its identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "Team not found.")
    })
    @GetMapping("/{teamId}")
    public TeamDto getTeam(
            @Parameter(description = "Identifier of the team to retrieve.", example = "2")
            @PathVariable Long teamId
    ) {
        return service.getTeamById(teamId);
    }

    @Operation(summary = "Get a team details by its identifier with its players.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "Team not found.")
    })
    @GetMapping("/{teamId}/players")
    public TeamDto getTeamWithPlayers(
            @Parameter(description = "Identifier of the team to retrieve.", example = "2")
            @PathVariable Long teamId
    ) {
        return service.getTeamByIdWithPlayers(teamId);
    }

    @Operation(summary = "Create a new team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The team was successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TeamDto createTeam(
            @Parameter(description = "Details of the team to be created.")
            @Valid @RequestBody TeamRequest teamRequest) {
        return service.createTeam(teamRequest);
    }

    @Operation(summary = "Add a player to a specified team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The player was successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid request."),
            @ApiResponse(responseCode = "404", description = "Team not found.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{teamId}/players")
    public PlayerDto addPlayer(
            @Parameter(description = "Identifier of the team to which the player will be added.", example = "2")
            @PathVariable Long teamId,

            @Parameter(description = "Details of the player to be added.")
            @Valid @RequestBody PlayerRequest playerRequest
    ) {
        return service.addPlayer(teamId, playerRequest);
    }
}
