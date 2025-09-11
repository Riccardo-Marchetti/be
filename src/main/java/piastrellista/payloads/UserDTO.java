package piastrellista.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// This class is a Data Transfer Object (DTO) for User
public record UserDTO(@NotEmpty(message = "Username is mandatory")
                      @Size(min = 4, message = "Username must be longer than 4 chars")
                      String username,
                      @NotEmpty(message = "Email is mandatory")
                      @Email(message = "You must insert a valid email")
                      String email,
                      @NotEmpty(message = "Password is mandatory")
                      @Size(min = 4, message = "Password must be longer than 4 chars")
                      String password

                      ) {
}
