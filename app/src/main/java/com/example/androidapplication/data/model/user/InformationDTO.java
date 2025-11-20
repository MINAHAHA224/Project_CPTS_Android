package com.example.androidapplication.data.model.user;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InformationDTO {
    private Long id;
    private String email;
    private String role;
    private String fullName;
    private String avatar;
    private String tokenJWT;
}
