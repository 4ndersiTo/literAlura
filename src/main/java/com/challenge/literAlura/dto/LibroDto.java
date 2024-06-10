package com.challenge.literAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroDto(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AutorDto> author,
        @JsonAlias("languages") List<String> language,
        @JsonAlias("download_count") Double downloads
) {
}
