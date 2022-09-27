package de.lehrcode.untier.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostingNotFoundException extends RuntimeException {
    public PostingNotFoundException() {
        super("Requested posting not found");
    }
}
