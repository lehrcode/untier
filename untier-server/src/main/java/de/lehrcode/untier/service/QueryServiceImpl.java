package de.lehrcode.untier.service;

import de.lehrcode.untier.api.AttachmentDto;
import de.lehrcode.untier.api.PostingDto;
import de.lehrcode.untier.api.QueryService;
import de.lehrcode.untier.model.AttachmentRepository;
import de.lehrcode.untier.model.Posting;
import de.lehrcode.untier.model.PostingRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {
    private final PostingRepository postingRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public Page<PostingDto> getPostings(Pageable pageable) {
        log.debug("getPostings(pageable={})", pageable);
        return postingRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public Optional<PostingDto> getPosting(Long id) {
        log.debug("getPosting(id={})", id);
        return postingRepository.findById(id).map(this::toDto);
    }

    public PostingDto toDto(Posting posting) {
        log.debug("toDto(posting={})", posting);
        return PostingDto.builder()
                         .withId(posting.getId())
                         .withAuthor(posting.getAuthor())
                         .withPublished(posting.getPublished())
                         .withText(posting.getText())
                         .withAttachmentId(posting.getAttachmentId())
                         .build();
    }

    @Override
    public Optional<AttachmentDto> getAttachment(Long id) {
        log.debug("getAttachment(id={})", id);
        return attachmentRepository.findById(id)
                                   .map(attachment -> AttachmentDto.builder()
                                                                   .withBytes(attachment.getBytes())
                                                                   .withMediaType(attachment.getMediaType())
                                                                   .build());
    }
}
