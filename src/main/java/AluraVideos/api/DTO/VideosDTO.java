package AluraVideos.api.DTO;

import jakarta.validation.constraints.NotBlank;

public record VideosDTO(
        @NotBlank
        String titulo,

        @NotBlank
        String descricao,

        @NotBlank
        String url) {}
