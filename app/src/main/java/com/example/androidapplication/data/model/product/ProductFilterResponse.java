package com.example.androidapplication.data.model.product;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProductFilterResponse {

    @SerializedName("products")
    private List<Product> products;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("totalPages")
    private int totalPages;

    // Getter and Setter
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}