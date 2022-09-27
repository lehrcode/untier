package de.lehrcode.untier.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(setterPrefix = "with")
public class Blob {
    @NotNull
    @ToString.Exclude
    @Size(min = 1, max = 1_048_576)
    private final byte[] bytes;
    @NotBlank
    private final String mediaType;
}
