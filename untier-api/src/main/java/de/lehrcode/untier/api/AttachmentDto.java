package de.lehrcode.untier.api;

import javax.json.bind.annotation.JsonbCreator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor(onConstructor = @__(@JsonbCreator))
public class AttachmentDto {
    @NotNull
    @ToString.Exclude
    @Size(min = 1, max = 1_048_576)
    private final byte[] bytes;

    @NotBlank
    @Size(min = 1, max = 255)
    private final String mediaType;
}
