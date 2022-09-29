package de.lehrcode.untier.api;

import javax.json.bind.annotation.JsonbCreator;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor(onConstructor = @__(@JsonbCreator))
public class DraftPostingDto {
    @Size(max = 400)
    private final String text;

    @Valid
    private final AttachmentDto attachment;
}
