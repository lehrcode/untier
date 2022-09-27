package de.lehrcode.untier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(UntierProperties.class)
@SpringBootApplication
public class UntierServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UntierServerApplication.class, args);
    }

}
