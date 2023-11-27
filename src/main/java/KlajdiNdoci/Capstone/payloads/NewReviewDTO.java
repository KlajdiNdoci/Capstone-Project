package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record NewReviewDTO(
        @NotEmpty(message = "The field content cannot be empty!")
        @Size(min = 3, message = "The content must have at least 3 characters!")
        String content,
        @NotNull(message = "The field rating cannot be null!")
        Integer rating,
        @NotNull(message = "The field gameId cannot be null!")
        UUID gameId
) {
}
