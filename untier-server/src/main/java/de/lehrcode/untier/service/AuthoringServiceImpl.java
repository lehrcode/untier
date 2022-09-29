package de.lehrcode.untier.service;

import de.lehrcode.untier.api.AuthoringService;
import de.lehrcode.untier.api.DraftPostingDto;
import de.lehrcode.untier.model.Attachment;
import de.lehrcode.untier.model.AttachmentRepository;
import de.lehrcode.untier.model.Posting;
import de.lehrcode.untier.model.PostingRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class AuthoringServiceImpl implements AuthoringService {
    private final PostingRepository postingRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public Long publish(DraftPostingDto draftPosting, String author) {
        log.debug("publish(draftPosting={}, author={})", draftPosting, author);
        Long attachmentId = null;
        if (draftPosting.getAttachment() != null) {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            byte[] checksum = digest.digest(draftPosting.getAttachment().getBytes());
            attachmentId = attachmentRepository.findIdByChecksum(checksum).orElseGet(() -> {
                return attachmentRepository.save(Attachment.builder()
                                                           .withBytes(draftPosting.getAttachment().getBytes())
                                                           .withChecksum(checksum)
                                                           .withMediaType(draftPosting.getAttachment().getMediaType())
                                                           .withCreated(OffsetDateTime.now())
                                                           .build())
                                           .getId();
            });
        }
        return postingRepository.save(Posting.builder()
                                             .withPublished(OffsetDateTime.now())
                                             .withAuthor(author)
                                             .withAttachmentId(attachmentId)
                                             .withText(draftPosting.getText())
                                             .build())
                                .getId();
    }

    @Override
    public void retract(Long id) {
        log.debug("retract(id={})", id);
        Posting posting = postingRepository.findById(id)
                                           .orElseThrow(PostingNotFoundException::new);
        postingRepository.delete(posting);
    }
}
