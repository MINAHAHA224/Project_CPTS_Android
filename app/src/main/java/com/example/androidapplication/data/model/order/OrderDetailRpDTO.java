package com.example.androidapplication.data.model.order;

import lombok.*;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class OrderDetailRpDTO {
    private String productImage;
    private Long productId;
    private String productName;
    private Double price;
    private Long productQuantity;
}
