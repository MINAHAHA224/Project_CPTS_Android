package com.example.androidapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.order.OrderDetailRpDTO;
import com.example.androidapplication.databinding.ItemOrderDetailBinding;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private final Context context;
    private final List<OrderDetailRpDTO> orderDetailList;
    private final String BASE_IMAGE_URL = ApiClient.BASE_URL + "resources/images/product/";

    public OrderDetailAdapter(Context context, List<OrderDetailRpDTO> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        holder.bind(orderDetailList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderDetailBinding binding;

        public OrderDetailViewHolder(ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderDetailRpDTO detail) {
            binding.productName.setText(detail.getProductName());
            binding.productQuantity.setText("Số lượng: " + detail.getProductQuantity());

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.productPrice.setText(currencyFormat.format(detail.getPrice()));

            Glide.with(context)
                    .load(BASE_IMAGE_URL + detail.getProductImage())
                    .centerCrop()
                    .into(binding.productImage);
        }
    }
}