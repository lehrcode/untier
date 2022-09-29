package de.lehrcode.untier.web;

import de.lehrcode.untier.UntierProperties;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.bind.Jsonb;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatusController {
    private final ApplicationAvailability availability;
    private final BuildProperties buildProperties;
    private final UntierProperties untierProperties;
    private final Jsonb jsonb;

    @Value("classpath:openapiv3.json")
    private Resource apiDocsTemplate;

    @GetMapping(path = {"/health/readiness"})
    public ResponseEntity<Map<String, ReadinessState>> getReadinessState() {
        ReadinessState state = availability.getReadinessState();
        return ResponseEntity
            .status(
                state == ReadinessState.ACCEPTING_TRAFFIC
                    ? HttpStatus.OK
                    : HttpStatus.SERVICE_UNAVAILABLE
            )
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noCache().mustRevalidate())
            .header(HttpHeaders.PRAGMA, "no-cache")
            .body(Collections.singletonMap("status", state));
    }

    @GetMapping(path = "/health/liveness")
    public ResponseEntity<Map<String, LivenessState>> getLivenessState() {
        LivenessState state = availability.getLivenessState();
        return ResponseEntity
            .status(
                state == LivenessState.CORRECT
                    ? HttpStatus.OK
                    : HttpStatus.SERVICE_UNAVAILABLE
            )
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noCache().mustRevalidate())
            .header(HttpHeaders.PRAGMA, "no-cache")
            .body(Collections.singletonMap("status", state));
    }

    @GetMapping(path = "/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noCache().mustRevalidate())
            .header(HttpHeaders.PRAGMA, "no-cache")
            .body(
                Map.of(
                    "artifact", buildProperties.getArtifact(),
                    "version", buildProperties.getVersion(),
                    "buildtime", buildProperties.getTime().toString()
                )
            );
    }

    @GetMapping(path = "/api-docs")
    public ResponseEntity<JsonObject> getApiDocs() throws IOException {
        JsonObject apiDocs;
        try (var in = apiDocsTemplate.getInputStream()) {
            apiDocs = jsonb.fromJson(in, JsonObject.class);
        }

        JsonPatch patch = Json
            .createPatchBuilder()
            .replace("/info/version", buildProperties.getVersion())
            .replace("/components/securitySchemes/jwt/flows/implicit/authorizationUrl",
                     untierProperties.getAuthorizationUrl())
            .replace("/components/securitySchemes/jwt/flows/authorizationCode/authorizationUrl",
                     untierProperties.getAuthorizationUrl())
            .replace("/components/securitySchemes/jwt/flows/authorizationCode/tokenUrl",
                     untierProperties.getTokenUrl())
            .replace("/components/securitySchemes/jwt/flows/authorizationCode/refreshUrl",
                     untierProperties.getTokenUrl())
            .build();

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noCache().mustRevalidate())
            .header(HttpHeaders.PRAGMA, "no-cache")
            .body(patch.apply(apiDocs));
    }
}
