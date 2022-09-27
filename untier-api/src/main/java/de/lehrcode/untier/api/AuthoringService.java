package de.lehrcode.untier.api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface AuthoringService {
    @NotNull
    Long publish(@Size(max = 400) String message,
                 @Valid Blob blob,
                 @NotBlank @Size(min = 1, max = 100) String author);

    default Long publish(@Size(max = 400) String message,
                         @NotBlank @Size(min = 1, max = 100) String author) {
        return publish(message, null, author);
    }

    default Long publish(@Valid Blob blob,
                         @NotBlank @Size(min = 1, max = 100) String author) {
        return publish(null, blob, author);
    }

    void retract(@NotNull Long id);
}
