package KlajdiNdoci.Capstone.exceptions;

import lombok.Getter;

@Getter
public class UnsupportedMediaTypeException extends RuntimeException {
    public UnsupportedMediaTypeException(String message) {
        super(message);
    }
}
