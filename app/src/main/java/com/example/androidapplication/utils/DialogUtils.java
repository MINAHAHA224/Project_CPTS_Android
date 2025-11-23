package com.example.androidapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidapplication.R;

public class DialogUtils {

    // Interface callback
    public interface OnDialogDismissListener {
        void onDismiss();
    }

    public static void showErrorDialog(Context context, String message) {
        showErrorDialog(context, "Lỗi", message, null);
    }

    public static void showErrorDialog(Context context, String title, String message, OnDialogDismissListener listener) {
        // Dùng LayoutInflater thường để tránh lỗi Binding nếu chưa Rebuild
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_error, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        // QUAN TRỌNG: Làm trong suốt nền mặc định của Android để thấy bo góc của mình
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCancelable(false);

        // Ánh xạ
        TextView txtTitle = view.findViewById(R.id.text_title);
        TextView txtMessage = view.findViewById(R.id.text_message);
        Button btnOk = view.findViewById(R.id.button_ok);

        // Gán dữ liệu
        txtTitle.setText(title != null ? title : "Thông báo");
        txtMessage.setText(message);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) {
                listener.onDismiss();
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessDialog(Context context, String title, String message, OnDialogDismissListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCancelable(false);

        TextView txtTitle = view.findViewById(R.id.text_title);
        TextView txtMessage = view.findViewById(R.id.text_message);
        Button btnOk = view.findViewById(R.id.button_ok);

        txtTitle.setText(title);
        txtMessage.setText(message);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) {
                listener.onDismiss();
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}