package com.example.androidapplication.data.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApiResponse <T> {
    private int status;
    private String message;
    private T data;
}