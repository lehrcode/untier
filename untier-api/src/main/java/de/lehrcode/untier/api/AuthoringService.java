package de.lehrcode.untier.api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface AuthoringService {
    @NotNull
    Long publish(@Valid DraftPostingDto draftPosting,
                 @NotBlank @Size(min = 1, max = 100) String author);

    default Long publish(@Size(max = 400) String text,
                         @NotBlank @Size(min = 1, max = 100) String author) {
        return publish(DraftPostingDto.builder().withText(text).build(), author);
    }

    void retract(@NotNull Long id);
}
