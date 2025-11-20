package com.example.androidapplication.data.model.cart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetail {
    private Long productId;
    private String productImage;
    private String productName;

    // cartDetail
    private Long id;
    private Long quantity;
    private Double price;
    // total price

    private Long stockQuantity;
}
