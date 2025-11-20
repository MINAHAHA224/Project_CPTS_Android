package com.example.androidapplication.data.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}
