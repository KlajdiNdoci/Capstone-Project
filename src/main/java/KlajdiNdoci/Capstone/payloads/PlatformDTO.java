package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PlatformDTO(
        @NotNull(message = "The field platforms cannot be empty!")
        List<String> platforms
) {
}
