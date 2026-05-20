package com.abhiraj.PopcornPing.domain.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequestDto {

    @NotBlank(message = "Enter an email to proceed.")
    @Email(message = "Check the email format")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

}
