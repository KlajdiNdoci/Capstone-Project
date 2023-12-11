package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record NewReviewDTO(
        @NotEmpty(message = "The field content cannot be empty!")
        @Size(min = 3, message = "The content must have at least 3 characters!")
        String content,
        @NotEmpty(message = "The field title cannot be empty!")
        @Size(min = 3,max = 30, message = "The title must be between 3 and 30 characters!")
        String title,
        @NotNull(message = "The field rating cannot be null!")
        @Min(value = 1, message = "The rating must be at least 1")
        @Max(value = 5, message = "The rating must be at most 5")
        Integer rating
) {
}
