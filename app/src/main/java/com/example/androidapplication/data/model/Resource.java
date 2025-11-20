package com.example.androidapplication.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidapplication.data.model.ErrorResponse;
import com.example.androidapplication.data.model.Status;

// Lớp này sẽ bao bọc kết quả trả về từ Repository
public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final ErrorResponse error;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable ErrorResponse error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    // Các phương thức static helper để tạo các trạng thái khác nhau
    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(@NonNull ErrorResponse error, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, error);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    // Enum để định nghĩa các trạng thái
 
}