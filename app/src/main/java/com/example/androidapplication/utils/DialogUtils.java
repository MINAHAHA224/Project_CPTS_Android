package com.example.androidapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidapplication.R;
import com.example.androidapplication.databinding.DialogErrorBinding;
import com.example.androidapplication.databinding.DialogSuccessBinding;

public class DialogUtils {

    public static void showErrorDialog(Context context, String message) {
        // Sử dụng ViewBinding cho dialog
        DialogErrorBinding binding = DialogErrorBinding.inflate(LayoutInflater.from(context));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(binding.getRoot());

        // Tạo dialog và làm cho nó không thể bị hủy bằng cách nhấn ra ngoài
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Set nội dung lỗi
        binding.textMessage.setText(message);

        // Set sự kiện cho nút OK
        binding.buttonOk.setOnClickListener(v -> dialog.dismiss());

        // Đặt background của dialog thành trong suốt để chỉ thấy layout custom
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    // Thêm interface để xử lý sự kiện khi dialog được đóng
    public interface OnDialogDismissListener {
        void onDismiss();
    }

    // Bổ sung hàm showSuccessDialog
    public static void showSuccessDialog(Context context, String title, String message, OnDialogDismissListener listener) {
        DialogSuccessBinding binding = DialogSuccessBinding.inflate(LayoutInflater.from(context)); // Cần tạo layout dialog_success.xml

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(binding.getRoot());

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        binding.textTitle.setText(title);
        binding.textMessage.setText(message);

        binding.buttonOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) {
                listener.onDismiss(); // Gọi callback khi người dùng nhấn OK
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }
}