package com.example.androidapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.order.Order;
import com.example.androidapplication.data.model.order.OrderDetailRpDTO;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;

    private final String BASE_IMAGE_URL = ApiClient.BASE_URL + "resources/images/product/";


    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList != null ? orderList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderList.get(position).getId();
    }

    private class ViewHolder {
        TextView txtId, txtDate, txtStatus, txtPrice ,imgArrow;
        LinearLayout layoutHeader, layoutDetails;
//        ImageView imgArrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order_history, null);
            holder = new ViewHolder();
            holder.txtId = convertView.findViewById(R.id.order_id);
            holder.txtDate = convertView.findViewById(R.id.order_date);
            holder.txtStatus = convertView.findViewById(R.id.order_status);
            holder.txtPrice = convertView.findViewById(R.id.order_total_price);
            holder.layoutHeader = convertView.findViewById(R.id.header_layout);
            holder.layoutDetails = convertView.findViewById(R.id.details_layout);
            holder.imgArrow = convertView.findViewById(R.id.expand_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orderList.get(position);

        holder.txtId.setText("Đơn hàng #" + order.getId());
        holder.txtStatus.setText(order.getStatus());

        if (order.getTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            holder.txtDate.setText(sdf.format(order.getTime()));
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtPrice.setText(currencyFormat.format(order.getTotalPrice()));

        // Xử lý Ẩn/Hiện chi tiết (Expandable thủ công)
        if (order.isExpanded()) {
            holder.layoutDetails.setVisibility(View.VISIBLE);
//            holder.imgArrow.setRotation(180f);

            // Xóa view cũ để tránh bị double khi scroll
            holder.layoutDetails.removeAllViews();

            // Lấy danh sách sản phẩm con
            List<OrderDetailRpDTO> details = order.getOrderDetails();
            if (details != null && !details.isEmpty()) {
                for (OrderDetailRpDTO item : details) {
                    // Inflate layout con
                    View childView = LayoutInflater.from(context).inflate(R.layout.item_order_detail, null);

                    // --- SỬA LỖI TẠI ĐÂY: Ánh xạ đầy đủ các View con ---
                    ImageView imgProduct = childView.findViewById(R.id.product_image); // Ánh xạ ảnh
                    TextView name = childView.findViewById(R.id.product_name);
                    TextView qty = childView.findViewById(R.id.product_quantity);
                    TextView price = childView.findViewById(R.id.product_price); // Nếu layout có giá riêng

                    // 1. Set Text (Dữ liệu từ API)
                    name.setText(item.getProductName());

                    // Format tiền tệ
                    String formattedPrice = currencyFormat.format(item.getPrice());
                    qty.setText("x" + item.getProductQuantity());

                    // Nếu bạn có TextView hiển thị giá trong item_order_detail.xml
                    if (price != null) {
                        price.setText(formattedPrice);
                    }

                    // 2. Set Ảnh (Dùng Glide load từ URL) - QUAN TRỌNG
                    // Ghép đường dẫn gốc + tên ảnh từ API
                    String fullImageUrl = BASE_IMAGE_URL + item.getProductImage();

                    Glide.with(context)
                            .load(fullImageUrl)
                            .placeholder(R.drawable.ic_launcher_background) // Hiện ảnh này khi đang tải
                            .error(R.drawable.ic_error) // Hiện ảnh này nếu link chết/lỗi
                            .centerCrop()
                            .into(imgProduct);

                    // Add view con vào layout cha
                    holder.layoutDetails.addView(childView);
                }
            } else {
                TextView errorTxt = new TextView(context);
                errorTxt.setText("Không có thông tin chi tiết");
                errorTxt.setPadding(0, 16, 0, 16);
                holder.layoutDetails.addView(errorTxt);
            }

        } else {
            holder.layoutDetails.setVisibility(View.GONE);
            holder.imgArrow.setRotation(0f);
        }

        // Sự kiện click để mở rộng
        holder.layoutHeader.setOnClickListener(v -> {
            order.setExpanded(!order.isExpanded());
            notifyDataSetChanged(); // Load lại list để cập nhật trạng thái
        });

        return convertView;
    }
}