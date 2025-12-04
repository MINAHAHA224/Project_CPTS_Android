package com.example.androidapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class FaceNetHelper {

    private Interpreter interpreter;
    private int inputImageSize = 160; // Kích thước chuẩn của FaceNet (thường là 160 hoặc 112 tùy model, facenet thường là 160)
    private final String MODEL_NAME = "facenet.tflite"; // Tên file trong assets

    public FaceNetHelper(Context context) {
        try {
            // Load model
            MappedByteBuffer modelFile = FileUtil.loadMappedFile(context, MODEL_NAME);
            Interpreter.Options options = new Interpreter.Options();
            interpreter = new Interpreter(modelFile, options);

            // Kiểm tra input size từ model (để chắc chắn)
            // int[] inputShape = interpreter.getInputTensor(0).shape();
            // inputImageSize = inputShape[1];

        } catch (Exception e) {
            Log.e("FaceNetHelper", "Error loading model", e);
        }
    }

    public float[] getFaceEmbedding(Bitmap bitmap) {
        if (interpreter == null) return null;

        // 1. Preprocess ảnh (Resize, Chuẩn hóa)
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(127.5f, 127.5f)) // Chuẩn hóa về [-1, 1]
                .build();

        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);

        // 2. Chạy Model
        // Output của FaceNet là 1 vector 128 chiều (hoặc 512 tùy model)
        // File facenet.tflite gốc thường là 128
        float[][] output = new float[1][128];
        interpreter.run(tensorImage.getBuffer(), output);

        // 3. Trả về vector (Flatten ra mảng 1 chiều)
        return output[0];
    }

    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

    // Hàm tiện ích chuyển mảng float thành chuỗi String để gửi lên Server (ví dụ: "0.1,0.2,-0.5")
    public static String embeddingToString(float[] embedding) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}