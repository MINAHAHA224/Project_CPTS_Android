package com.example.androidapplication.data.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {
    private String timestamp ;
    private int status;
    private String error;
    private String message;
    private List<ValidationError> errorDetails;
    private String path;
}
