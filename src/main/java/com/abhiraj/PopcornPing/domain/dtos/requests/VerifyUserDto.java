package com.abhiraj.PopcornPing.domain.dtos.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyUserDto {

    private String email;
    private String verificationCode;

}
