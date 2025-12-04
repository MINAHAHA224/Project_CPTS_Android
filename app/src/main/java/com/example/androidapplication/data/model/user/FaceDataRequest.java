package com.example.androidapplication.data.model.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaceDataRequest {
    private String embeddedFaceData;
}