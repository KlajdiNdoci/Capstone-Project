package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UserLoginDTO(
        @NotEmpty(message = "The field email cannot be empty")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "The email is not valid")
        String email,
        @NotEmpty(message = "The field password cannot be empty")
        String password
) {
}
