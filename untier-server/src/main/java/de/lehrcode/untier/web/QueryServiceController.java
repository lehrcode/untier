package de.lehrcode.untier.web;

import de.lehrcode.untier.api.PostingDto;
import de.lehrcode.untier.api.QueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QueryServiceController {
    private final QueryService queryService;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping(path = "/postings", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostingDto> getPostings(Pageable pageable) {
        log.debug("getPostings(pageable={})", pageable);
        return queryService.getPostings(pageable).toList();
    }

    @GetMapping(path = "/postings/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostingDto> getPosting(@PathVariable Long id) {
        log.debug("getPosting(id={})", id);
        return ResponseEntity.of(queryService.getPosting(id));
    }

    @GetMapping(path = "/images/{id:\\d+}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return queryService.getImage(id)
                           .map(img -> ResponseEntity.ok()
                                                     .contentType(MediaType.parseMediaType(img.getMediaType()))
                                                     .body(img.getBytes()))
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
