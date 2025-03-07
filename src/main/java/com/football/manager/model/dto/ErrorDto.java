package com.football.manager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    @Schema(description = "List of errors occurred")
    private List<Error> errors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {

        @Schema(description = "Name of the attribute related to the error", example = "acronym")
        private String attributes;

        @Schema(description = "Message describing the error", example = "The 'acronym' field cannot be empty.")
        private String message;
    }
}
