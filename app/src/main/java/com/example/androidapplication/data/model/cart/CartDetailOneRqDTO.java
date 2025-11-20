package com.example.androidapplication.data.model.cart;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartDetailOneRqDTO {
    private Long id;
    private Long quantity;
}
