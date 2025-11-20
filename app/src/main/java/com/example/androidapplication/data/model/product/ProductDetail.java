package com.example.androidapplication.data.model.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDetail {
    private Long id ;
    private String image;
    private String name;
    private String factory;
    private Double price;
    private String detailDesc;
    private String shortDesc;

    // more detail
    private Long quantity;
    private String target;
    private Long sold;
}
