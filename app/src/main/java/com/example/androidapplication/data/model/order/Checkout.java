package com.example.androidapplication.data.model.order;

import com.example.androidapplication.data.model.cart.CartDetail;

import java.util.List;

public class Checkout {
    private List<CartDetail> cartDetails;
    private double totalPrice;
    private InfoOrderRqDTO infoOrderRqDTO;
}
