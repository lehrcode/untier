package de.lehrcode.untier;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "untier")
public class UntierProperties {
    private String authorizationUrl;
    private String tokenUrl;
}
