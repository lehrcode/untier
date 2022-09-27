package de.lehrcode.untier.api;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class PostingDto {
    @NotNull
    private final Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private final String author;

    @Size(max = 400)
    private final String text;

    private final Long imageId;

    @NotNull
    private final OffsetDateTime published;
}
