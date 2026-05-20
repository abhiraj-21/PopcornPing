package com.abhiraj.PopcornPing.domain.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "Enter an email to proceed.")
    @Email(message = "Check the email format")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}
