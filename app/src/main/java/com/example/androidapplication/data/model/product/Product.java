package com.example.androidapplication.data.model.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product {
    private Long id;
    private String name;      // Thêm dòng này
    private String image;
    private String shortDesc;
    private Double price;
}
