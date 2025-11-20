package com.example.androidapplication.data.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@ToString
public class ValidationError {
    private String field;
    private String message;
}
