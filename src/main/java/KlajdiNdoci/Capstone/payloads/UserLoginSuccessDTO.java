package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginSuccessDTO (
        @NotEmpty
        String accessToken
) {
}
