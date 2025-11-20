package com.example.androidapplication.data.model.cart;

import lombok.*;

import java.util.List;

@Getter@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class Cart {
    private List<CartDetail> cartDetails;
    private double totalPrice;
}
