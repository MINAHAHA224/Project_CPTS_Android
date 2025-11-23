package com.example.androidapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.cart.CartDetail;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<CartDetail> cartList;
    private final String BASE_IMAGE_URL = ApiClient.BASE_URL + "resources/images/product/";
    private OnCartItemListener listener;

    // Interface để gửi sự kiện click ra ngoài Activity
    public interface OnCartItemListener {
        void onDelete(long productId);
        void onIncrease(long productId);
        void onDecrease(long productId);
    }

    public CartAdapter(Context context, List<CartDetail> cartList, OnCartItemListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    // Hàm cập nhật dữ liệu mới
    public void updateData(List<CartDetail> newList) {
        this.cartList.clear();
        this.cartList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartList != null ? cartList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder pattern cho ListView
    private class ViewHolder {
        ImageView imgProduct, btnDelete;
        TextView txtName, txtPrice, txtQuantity, btnMinus, btnPlus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_cart, null);

            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.cart_item_image);
            holder.btnDelete = convertView.findViewById(R.id.button_delete);
            holder.txtName = convertView.findViewById(R.id.cart_item_name);
            holder.txtPrice = convertView.findViewById(R.id.cart_item_price);
            holder.txtQuantity = convertView.findViewById(R.id.text_quantity);
            holder.btnMinus = convertView.findViewById(R.id.button_decrease);
            holder.btnPlus = convertView.findViewById(R.id.button_increase);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy dữ liệu
        CartDetail item = cartList.get(position);

        // Gán dữ liệu lên View
        holder.txtName.setText(item.getProductName());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        // Tính giá tổng của item (đơn giá * số lượng) hoặc hiển thị đơn giá tùy bạn.
        // Ở đây hiển thị giá tổng của dòng đó.
        double lineTotal = item.getPrice() * item.getQuantity();
        holder.txtPrice.setText(currencyFormat.format(lineTotal));

        Glide.with(context)
                .load(BASE_IMAGE_URL + item.getProductImage())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgProduct);

        // Xử lý sự kiện click
        holder.btnDelete.setOnClickListener(v -> {
            if(listener != null) listener.onDelete(item.getProductId());
        });

        holder.btnPlus.setOnClickListener(v -> {
            if(listener != null) listener.onIncrease(item.getProductId());
        });

        holder.btnMinus.setOnClickListener(v -> {
            if(listener != null) listener.onDecrease(item.getProductId());
        });

        return convertView;
    }
}