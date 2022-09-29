package de.lehrcode.untier;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.BinaryDataStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonbConfiguration {
    @Bean
    public Jsonb jsonb() {
        return JsonbBuilder.create(new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64));
    }
}
