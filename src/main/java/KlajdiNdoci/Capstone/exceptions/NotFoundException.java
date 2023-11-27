package KlajdiNdoci.Capstone.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(UUID id) {
        super("Element with id " + id + " not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}

