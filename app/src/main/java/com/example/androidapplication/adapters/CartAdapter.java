
package com.example.androidapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.cart.CartDetail;
import com.example.androidapplication.databinding.ItemCartBinding;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private List<CartDetail> cartDetailList;
    private final String BASE_IMAGE_URL = ApiClient.BASE_URL + "resources/images/product/";
    private final OnCartItemInteractionListener listener;

    // Interface chuẩn hóa với 3 hành động rõ ràng
    public interface OnCartItemInteractionListener {
        void onDeleteItem(long productId);        // Hành động cho nút thùng rác (xóa toàn bộ)
        void onIncreaseQuantity(long productId); // Hành động cho nút +
        void onDecreaseQuantity(long productId); // Hành động cho nút -
    }

    public CartAdapter(Context context, List<CartDetail> cartDetailList, OnCartItemInteractionListener listener) {
        this.context = context;
        this.cartDetailList = cartDetailList;
        this.listener = listener;
    }

    public void updateCartItems(List<CartDetail> newCartDetails) {
        this.cartDetailList.clear();
        this.cartDetailList.addAll(newCartDetails);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartDetailList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartDetailList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartDetail cartDetail) {
            binding.cartItemName.setText(cartDetail.getProductName());
            binding.textQuantity.setText(String.valueOf(cartDetail.getQuantity()));

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            double itemTotalPrice = cartDetail.getPrice() * cartDetail.getQuantity();
            binding.cartItemPrice.setText(currencyFormat.format(itemTotalPrice));

            Glide.with(context)
                    .load(BASE_IMAGE_URL + cartDetail.getProductImage())
                    .centerCrop()
                    .into(binding.cartItemImage);

            // Nút thùng rác -> Luôn gọi hàm xóa toàn bộ
            binding.buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteItem(cartDetail.getProductId());
                }
            });

            // Nút + -> Luôn gọi hàm tăng
            binding.buttonIncrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIncreaseQuantity(cartDetail.getProductId());
                }
            });

            // Nút - -> Gọi hàm giảm
            binding.buttonDecrease.setOnClickListener(v -> {
                if (listener != null) {
                    // Dù số lượng là bao nhiêu, vẫn gọi hàm giảm.
                    // Activity sẽ quyết định gọi API nào (giảm 1 hay xóa)
                    listener.onDecreaseQuantity(cartDetail.getProductId());
                }
            });
        }
    }
}