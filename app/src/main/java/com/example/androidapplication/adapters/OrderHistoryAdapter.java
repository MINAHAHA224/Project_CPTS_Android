package com.example.androidapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidapplication.data.model.order.Order;
import com.example.androidapplication.databinding.ItemOrderHistoryBinding;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryBinding binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private ItemOrderHistoryBinding binding;

        public OrderViewHolder(ItemOrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            binding.orderId.setText("Order #" + order.getId());
            binding.orderStatus.setText(order.getStatus());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            if (order.getTime() != null) {
                binding.orderDate.setText(sdf.format(order.getTime()));
            }

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.orderTotalPrice.setText(currencyFormat.format(order.getTotalPrice()));

            // --- LOGIC MỚI ĐỂ XỬ LÝ EXPAND/COLLAPSE ---

            // 1. Kiểm tra trạng thái và set visibility
            boolean isExpanded = order.isExpanded();
            binding.detailsLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            binding.expandArrow.setRotation(isExpanded ? 180f : 0f);

            // 2. Nếu đang mở, hiển thị danh sách sản phẩm
            if (isExpanded) {
                OrderDetailAdapter detailAdapter = new OrderDetailAdapter(context, order.getOrderDetails());
                binding.productsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                binding.productsRecyclerView.setAdapter(detailAdapter);
            }

            // 3. Gắn sự kiện click cho phần header
            binding.headerLayout.setOnClickListener(v -> {
                // Đảo ngược trạng thái
                order.setExpanded(!order.isExpanded());
                // Thông báo cho adapter rằng item này đã thay đổi để vẽ lại
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}