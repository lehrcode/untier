package de.lehrcode.untier.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder(setterPrefix = "with")
public class Problem {
    private final String title;
    private final String detail;
    private final int status;

    public static Problem of(HttpStatus httpStatus, String detail) {
        return builder().withDetail(detail)
                        .withStatus(httpStatus.value())
                        .withTitle(httpStatus.getReasonPhrase())
                        .build();
    }
}
