package com.example.androidapplication.data.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResetPasswordDTO {
    private String email;
    private String password;
    private String confirmPassword;
}
