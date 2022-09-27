package de.lehrcode.untier.service;

import de.lehrcode.untier.api.AuthoringService;
import de.lehrcode.untier.api.Blob;
import de.lehrcode.untier.model.Image;
import de.lehrcode.untier.model.ImageRepository;
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
    private final ImageRepository imageRepository;

    @Override
    public Long publish(String text, Blob blob, String author) {
        log.debug("publish(text={}, blob={}, author={})", text, blob, author);
        Long imageId = null;
        if (blob != null) {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            byte[] checksum = digest.digest(blob.getBytes());
            imageId = imageRepository.findIdByChecksum(checksum).orElseGet(() -> {
                return imageRepository.save(Image.builder()
                                                 .withBytes(blob.getBytes())
                                                 .withChecksum(checksum)
                                                 .withMediaType(blob.getMediaType())
                                                 .withCreated(OffsetDateTime.now())
                                                 .build())
                                      .getId();
            });
        }
        return postingRepository.save(Posting.builder()
                                             .withPublished(OffsetDateTime.now())
                                             .withAuthor(author)
                                             .withImageId(imageId)
                                             .withText(text)
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
