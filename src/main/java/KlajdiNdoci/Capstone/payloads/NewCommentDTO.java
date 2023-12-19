package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewCommentDTO(
        @NotEmpty(message = "The field content cannot be empty!")
        @Size(min = 3, message = "The content must have at least 3 characters!")
        String content
) {
}
