package de.lehrcode.untier.api;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryService {
    @NotNull
    Optional<PostingDto> getPosting(@NotNull Long id);

    @NotNull
    default List<PostingDto> getPostings() {
        return getPostings(Pageable.unpaged()).toList();
    }

    @NotNull
    Page<PostingDto> getPostings(@NotNull Pageable pageable);

    @NotNull
    Optional<AttachmentDto> getAttachment(@NotNull Long id);
}
