package com.example.androidapplication.data.model.order;

import lombok.*;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class InfoOrderRqDTO {
    private String receiverName;

    private String receiverAddress;

    private String receiverPhone;

    private Double totalPriceToSaveOrder;

    private String paymentMethod;
}
