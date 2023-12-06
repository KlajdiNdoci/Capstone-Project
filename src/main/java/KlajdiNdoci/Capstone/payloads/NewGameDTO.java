package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record NewGameDTO (
        @NotEmpty(message = "The field title cannot be empty!")
        @Size(min = 3,max = 30, message = "The title must be between 3 and 30 characters!")
        String title,
        @NotEmpty(message = "The field description cannot be empty!")
        String description,
        @NotEmpty(message = "The field developer cannot be empty!")
        String developer,
        @NotEmpty(message = "The field publisher cannot be empty!")
        String publisher,
        @NotEmpty(message = "The field genres cannot be empty!")
        List<String> genres,
        @NotEmpty(message = "The field platforms cannot be empty!")
        List<String> platforms,
        @NotNull(message = "The field releaseDate cannot be null!")
        LocalDate releaseDate
){
}
