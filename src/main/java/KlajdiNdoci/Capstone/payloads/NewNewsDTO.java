package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewNewsDTO (
        @NotEmpty(message = "The field content cannot be empty!")
        @Size(min = 3, message = "The content must have at least 3 characters!")
        String content,
        @NotEmpty(message = "The field title cannot be empty!")
        @Size(min = 3,max = 30, message = "The title must be between 3 and 30 characters!")
        String title,
        @NotNull(message = "The field gameId cannot be null!")
        UUID gameId
){
}
