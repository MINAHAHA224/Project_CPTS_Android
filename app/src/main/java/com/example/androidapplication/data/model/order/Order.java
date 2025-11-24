package com.example.androidapplication.data.model.order;

import lombok.*;

import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose; // Quan trọng: Thêm import này

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class Order {
    private Long id;
    private Double totalPrice;

    private List<OrderDetailRpDTO> orderDetails;

    private String status;


    // detail from show oderAdmin
    private String nameUser;
    private String emailUser;

    private Date time;

    private String typePayment;

    private String statusPayment;


    private String receiverName;

    private String receiverPhone;
    private String receiverAddress;


    // --- THÊM PHẦN NÀY ---
    @Expose(serialize = false, deserialize = false) // Báo cho Gson bỏ qua trường này khi parse JSON
    private boolean     isExpanded = false;
}
