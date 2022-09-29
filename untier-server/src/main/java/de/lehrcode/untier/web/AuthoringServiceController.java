package de.lehrcode.untier.web;

import de.lehrcode.untier.api.AttachmentDto;
import de.lehrcode.untier.api.AuthoringService;
import de.lehrcode.untier.api.DraftPostingDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthoringServiceController {
    private final AuthoringService authoringService;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/postings", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> publish(@RequestBody String text,
                                        @AuthenticationPrincipal Jwt token) {
        log.debug("publish(text={}, token={})", text, token);
        Long id = authoringService.publish(StringUtils.trimToNull(text), token.getSubject());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                                                                 .path("/{id}")
                                                                 .build(id))
                             .build();
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/postings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publish(@RequestBody DraftPostingDto draftPosting,
                                        @AuthenticationPrincipal Jwt token) {
        log.debug("publish(draftPosting={}, token={})", draftPosting, token);
        Long id = authoringService.publish(draftPosting, token.getSubject());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                                                                 .path("/{id}")
                                                                 .build(id))
                             .build();
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/postings", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> publish(@RequestParam("text") String text,
                                        @RequestParam("attachment") MultipartFile attachedFile,
                                        @AuthenticationPrincipal Jwt token) throws IOException {
        log.debug("publish(text={}, attachedFile={}, token={})", text, attachedFile, token);
        AttachmentDto attachment = null;
        if (attachedFile != null && !attachedFile.isEmpty()) {
            attachment = AttachmentDto.builder()
                                         .withBytes(attachedFile.getBytes())
                                         .withMediaType(attachedFile.getContentType())
                                         .build();
        }
        Long id = authoringService.publish(DraftPostingDto.builder()
                                                          .withText(text)
                                                          .withAttachment(attachment)
                                                          .build(), token.getSubject());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                                                                 .path("/{id}")
                                                                 .build(id))
                             .build();
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/postings/{id:\\d+}")
    public void retract(@PathVariable Long id) {
        log.debug("retract(id={})", id);
        authoringService.retract(id);
    }
}
