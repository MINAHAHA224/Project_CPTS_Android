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
import com.example.androidapplication.data.model.order.OrderDetailRpDTO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDetailRpDTO> orderDetailList;
    private final String BASE_IMAGE_URL = ApiClient.BASE_URL + "resources/images/product/";

    public OrderDetailAdapter(Context context, List<OrderDetailRpDTO> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @Override
    public int getCount() {
        return orderDetailList != null ? orderDetailList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return orderDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtQuantity, txtPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order_detail, null);

            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.product_image);
            holder.txtName = convertView.findViewById(R.id.product_name);
            holder.txtQuantity = convertView.findViewById(R.id.product_quantity);
            holder.txtPrice = convertView.findViewById(R.id.product_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderDetailRpDTO item = orderDetailList.get(position);

        holder.txtName.setText(item.getProductName());
        holder.txtQuantity.setText("Số lượng: " + item.getProductQuantity());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtPrice.setText(currencyFormat.format(item.getPrice()));

        Glide.with(context)
                .load(BASE_IMAGE_URL + item.getProductImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error) // Ảnh lỗi nếu không load được
                .centerCrop()
                .into(holder.imgProduct);

        return convertView;
    }
}