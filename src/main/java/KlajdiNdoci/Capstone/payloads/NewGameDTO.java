package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewGameDTO (
        @NotEmpty(message = "The field title cannot be empty!")
        @Size(min = 3, message = "The title must have at least 3 characters!")
        String title,
        @NotEmpty(message = "The field description cannot be empty!")
        String description,
        @NotEmpty(message = "The field genres cannot be empty!")
        List<String> genres,
        @NotEmpty(message = "The field platforms cannot be empty!")
        List<String> platforms
){
}
