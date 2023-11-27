package KlajdiNdoci.Capstone.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotEmpty(message = "The field username cannot be empty")
        @Size(min = 3, max = 20, message = "The username must be between 3 and 20 characters!")
        String username,
        @NotEmpty(message = "The field name cannot be empty")
        @Size(min = 3, max = 20, message = "The name must be between 3 and 20 characters!")
        String name,
        @NotEmpty(message = "The field surname cannot be empty")
        @Size(min = 3, max = 20, message = "The surname must be between 3 and 20 characters!")
        String surname,

        @NotEmpty(message = "The field email cannot be empty")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "The email is not valid!")
        String email,

        @NotEmpty(message = "The field password cannot be empty")
        @Size(min = 3, max = 20, message = "The password must be between 3 and 20 characters!!")
        String password
) {
}
