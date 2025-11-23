package com.example.androidapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.activities.ProductDetailActivity;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.product.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
    private final String BASE_IMAGE_URL = ApiClient.BASE_URL + "resources/images/product/";

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList != null ? productList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    // Class ViewHolder để tối ưu hiệu năng (Giống RecyclerView nhưng code tay)
    private class ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Kiểm tra xem View đã được tái sử dụng chưa
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_product, null);

            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.product_image);
            holder.txtName = convertView.findViewById(R.id.product_name);
            holder.txtPrice = convertView.findViewById(R.id.product_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Gán dữ liệu
        Product product = productList.get(position);

        holder.txtName.setText(product.getName());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtPrice.setText(currencyFormat.format(product.getPrice()));

        Glide.with(context)
                .load(BASE_IMAGE_URL + product.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground) // Ảnh chờ
                .into(holder.imgProduct);

        // Sự kiện click vào item
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            context.startActivity(intent);
        });

        return convertView;
    }
}