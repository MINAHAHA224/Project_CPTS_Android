package com.example.androidapplication.data.model.user;

import lombok.*;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class ChangePasswordDTO {
    private String currentPassword;

    private String newPassword;

    private String confirmNewPassword;
}
