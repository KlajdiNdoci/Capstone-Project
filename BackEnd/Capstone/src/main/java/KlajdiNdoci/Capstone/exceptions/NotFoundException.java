package KlajdiNdoci.Capstone.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(long id) {
        super("Element with id " + id + " not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}

