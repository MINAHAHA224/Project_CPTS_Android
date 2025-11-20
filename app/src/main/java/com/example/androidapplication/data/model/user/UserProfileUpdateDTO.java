package com.example.androidapplication.data.model.user;

import lombok.*;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class UserProfileUpdateDTO {
    private String email;

    private String fullName;

    private String phone;

    private String address;

    private String avatar; // Không có validation, không cần message key

    private boolean hasChangePass; // Không có validation, không cần message key
}
