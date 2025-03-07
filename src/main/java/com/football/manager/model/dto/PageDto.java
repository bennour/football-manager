package com.football.manager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * A DTO used to simplify paginated responses by excluding unnecessary fields from Spring's Page.
 * It contains only the relevant information: the current page content, total pages, total elements,
 * and the current page number.
 */
@Getter
@Setter
public class PageDto<T> {

    @Schema(description = "The list of items on the current page.")
    private List<T> content;

    @Schema(description = "The total number of pages available.", example = "5")
    private int totalPages;

    @Schema(description = "The total number of elements across all pages.", example = "50")
    private long totalElements;

    @Schema(description = "The current page number.", example = "1")
    private int currentPage;

    public PageDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
    }
}
