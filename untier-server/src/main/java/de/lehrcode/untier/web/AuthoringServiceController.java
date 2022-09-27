package de.lehrcode.untier.web;

import de.lehrcode.untier.api.AuthoringService;
import de.lehrcode.untier.api.Blob;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestHeader;
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
    @PostMapping(path = "/postings", consumes = {MediaType.IMAGE_GIF_VALUE,
                                                 MediaType.IMAGE_JPEG_VALUE,
                                                 MediaType.IMAGE_PNG_VALUE,
                                                 "image/webp"})
    public ResponseEntity<Void> publish(@RequestBody byte[] imageBytes,
                                        @RequestHeader(HttpHeaders.CONTENT_TYPE) MediaType contentType,
                                        @AuthenticationPrincipal Jwt token) {
        log.debug("publish(imageBytes=..., token={})", token);
        Long id = authoringService.publish(Blob.builder()
                                               .withBytes(imageBytes)
                                               .withMediaType(String.join("/", contentType.getType(),
                                                                               contentType.getSubtype()))
                                               .build(), token.getSubject());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                                                                 .path("/{id}")
                                                                 .build(id))
                             .build();
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/postings", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> publish(@RequestParam("text") String text,
                                        @RequestParam("image") MultipartFile imageFile,
                                        @AuthenticationPrincipal Jwt token) throws IOException {
        log.debug("publish(text={}, imageFile={}, token={})", text, imageFile, token);
        Blob blob = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            blob = Blob.builder()
                       .withBytes(imageFile.getBytes())
                       .withMediaType(imageFile.getContentType())
                       .build();
        }
        Long id = authoringService.publish(StringUtils.trimToNull(text), blob, token.getSubject());
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
