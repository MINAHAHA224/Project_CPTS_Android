//package com.example.androidapplication.activities;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.ImageFormat;
//import android.graphics.Rect;
//import android.graphics.YuvImage;
//import android.media.Image;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageProxy;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.androidapplication.R;
//import com.example.androidapplication.data.model.Status;
//import com.example.androidapplication.utils.FaceNetHelper;
//import com.example.androidapplication.utils.SharedPrefManager;
//import com.example.androidapplication.viewmodel.AuthViewModel;
//import com.google.common.util.concurrent.ListenableFuture;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.face.Face;
//import com.google.mlkit.vision.face.FaceDetection;
//import com.google.mlkit.vision.face.FaceDetector;
//import com.google.mlkit.vision.face.FaceDetectorOptions;
//
//import java.io.ByteArrayOutputStream;
//import java.nio.ByteBuffer;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class FaceLoginActivity extends AppCompatActivity {
//
//    private PreviewView cameraPreview;
//    private TextView txtStatus;
//    private AuthViewModel authViewModel;
//    private FaceNetHelper faceNetHelper;
//    private FaceDetector faceDetector;
//    private ExecutorService cameraExecutor;
//
//    private boolean isProcessing = false; // Cờ để khóa không gọi API liên tục
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_face_login);
//
//        cameraPreview = findViewById(R.id.camera_preview);
//        txtStatus = findViewById(R.id.txt_status);
//        findViewById(R.id.btn_close).setOnClickListener(v -> finish());
//
//        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
//        faceNetHelper = new FaceNetHelper(this);
//        cameraExecutor = Executors.newSingleThreadExecutor();
//
//        // Cấu hình ML Kit
//        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
//                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
//                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
//                .build();
//        faceDetector = FaceDetection.getClient(options);
//
//        // Check quyền Camera
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
//        } else {
//            startCamera();
//        }
//    }
//
//    private void startCamera() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                Preview preview = new Preview.Builder().build();
//                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());
//
//                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
//
//                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build();
//
//                imageAnalysis.setAnalyzer(cameraExecutor, this::processImageProxy);
//
//                cameraProvider.unbindAll();
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
//
//            } catch (ExecutionException | InterruptedException e) {
//                Log.e("FaceLogin", "Camera init failed", e);
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    @SuppressLint("UnsafeOptInUsageError")
//    private void processImageProxy(ImageProxy imageProxy) {
//        // Nếu đang xử lý API thì bỏ qua các frame tiếp theo
//        if (isProcessing) {
//            imageProxy.close();
//            return;
//        }
//
//        Image mediaImage = imageProxy.getImage();
//        if (mediaImage != null) {
//            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
//
//            faceDetector.process(image)
//                    .addOnSuccessListener(faces -> {
//                        if (!faces.isEmpty()) {
//                            // Tìm thấy khuôn mặt -> Lock process lại
//                            isProcessing = true;
//
//                            // Cập nhật UI
//                            runOnUiThread(() -> txtStatus.setText("Đang xác thực..."));
//
//                            Face face = faces.get(0);
//                            Bitmap frameBitmap = toBitmap(mediaImage);
//
//                            // Crop khuôn mặt (Cần xử lý rotate nếu ảnh bị xoay, ở đây làm đơn giản)
//                            // Lưu ý: Ảnh từ CameraX (YUV) thường bị xoay 270 độ trên một số máy
//                            // Để code đơn giản cho đồ án, ta cứ crop thử, nếu lỗi thì cần thêm bước xoay Bitmap
//
//                            Rect bounds = face.getBoundingBox();
//                            // Validate bounds để không crash khi crop
//                            int x = Math.max(0, bounds.left);
//                            int y = Math.max(0, bounds.top);
//                            int w = Math.min(frameBitmap.getWidth() - x, bounds.width());
//                            int h = Math.min(frameBitmap.getHeight() - y, bounds.height());
//
//                            if (w > 0 && h > 0) {
//                                Bitmap faceBitmap = Bitmap.createBitmap(frameBitmap, x, y, w, h);
//
//                                // Lấy Vector
//                                float[] embedding = faceNetHelper.getFaceEmbedding(faceBitmap);
//
//                                // Gửi lên Server
//                                performFaceLogin(embedding);
//                            } else {
//                                isProcessing = false; // Crop lỗi thì thử lại
//                            }
//                        }
//                    })
//                    .addOnCompleteListener(task -> imageProxy.close());
//        } else {
//            imageProxy.close();
//        }
//    }
//
//    private void performFaceLogin(float[] embedding) {
//        if (embedding == null) {
//            isProcessing = false;
//            return;
//        }
//
//        String vectorStr = FaceNetHelper.embeddingToString(embedding);
//
//        runOnUiThread(() -> {
//            authViewModel.loginByFaceId(vectorStr).observe(this, resource -> {
//                if (resource.status == Status.SUCCESS) {
//                    txtStatus.setText("Đăng nhập thành công!");
//                    // Lưu User và chuyển màn hình
//                    SharedPrefManager.getInstance(this).saveUser(resource.data.getData());
//
//                    Intent intent = new Intent(this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    txtStatus.setText("Không nhận diện được. Vui lòng thử lại.");
//                    // Đợi 2 giây rồi cho phép quét lại
//                    new android.os.Handler().postDelayed(() -> {
//                        isProcessing = false;
//                        txtStatus.setText("Đang quét khuôn mặt...");
//                    }, 2000);
//                }
//            });
//        });
//    }
//
//    // Hàm chuyển đổi YUV sang Bitmap (Giống bên Register)
//    private Bitmap toBitmap(Image image) {
//        Image.Plane[] planes = image.getPlanes();
//        ByteBuffer yBuffer = planes[0].getBuffer();
//        ByteBuffer uBuffer = planes[1].getBuffer();
//        ByteBuffer vBuffer = planes[2].getBuffer();
//
//        int ySize = yBuffer.remaining();
//        int uSize = uBuffer.remaining();
//        int vSize = vBuffer.remaining();
//
//        byte[] nv21 = new byte[ySize + uSize + vSize];
//        yBuffer.get(nv21, 0, ySize);
//        vBuffer.get(nv21, ySize, vSize);
//        uBuffer.get(nv21, ySize + vSize, uSize);
//
//        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);
//        byte[] imageBytes = out.toByteArray();
//        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        faceNetHelper.close();
//        cameraExecutor.shutdown();
//    }
//}

package com.example.androidapplication.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.R;
import com.example.androidapplication.data.model.Status;
import com.example.androidapplication.utils.FaceNetHelper;
import com.example.androidapplication.utils.SharedPrefManager;
import com.example.androidapplication.viewmodel.AuthViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FaceLoginActivity extends AppCompatActivity {

    private PreviewView cameraPreview;
    private TextView txtStatus;
    private AuthViewModel authViewModel;
    private FaceNetHelper faceNetHelper;
    private FaceDetector faceDetector;
    private ExecutorService cameraExecutor;

    private boolean isProcessing = false; // Cờ khóa để không gọi API liên tục
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);

        cameraPreview = findViewById(R.id.camera_preview);
        txtStatus = findViewById(R.id.txt_status);
        findViewById(R.id.btn_close).setOnClickListener(v -> finish());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        faceNetHelper = new FaceNetHelper(this);
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Cấu hình ML Kit
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .build();
        faceDetector = FaceDetection.getClient(options);

        // QUAN TRỌNG: Kiểm tra quyền Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Cần quyền Camera để đăng nhập", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::processImageProxy);

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("FaceLogin", "Camera init failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void processImageProxy(ImageProxy imageProxy) {
        // Nếu đang xử lý API thì bỏ qua các frame tiếp theo
        if (isProcessing) {
            imageProxy.close();
            return;
        }

        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            faceDetector.process(image)
                    .addOnSuccessListener(faces -> {
                        if (!faces.isEmpty()) {
                            // Tìm thấy khuôn mặt -> Lock process lại
                            isProcessing = true;

                            // Cập nhật UI
                            runOnUiThread(() -> txtStatus.setText("Đang xác thực..."));

                            Face face = faces.get(0);
                            Bitmap frameBitmap = toBitmap(mediaImage);

                            Rect bounds = face.getBoundingBox();
                            int x = Math.max(0, bounds.left);
                            int y = Math.max(0, bounds.top);
                            int w = Math.min(frameBitmap.getWidth() - x, bounds.width());
                            int h = Math.min(frameBitmap.getHeight() - y, bounds.height());

                            if (w > 0 && h > 0) {
                                Bitmap faceBitmap = Bitmap.createBitmap(frameBitmap, x, y, w, h);

                                // Lấy Vector
                                float[] embedding = faceNetHelper.getFaceEmbedding(faceBitmap);

                                // Gửi lên Server
                                performFaceLogin(embedding);
                            } else {
                                isProcessing = false; // Crop lỗi thì thử lại
                            }
                        }
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        } else {
            imageProxy.close();
        }
    }

    private void performFaceLogin(float[] embedding) {
        if (embedding == null) {
            isProcessing = false;
            return;
        }

        String vectorStr = FaceNetHelper.embeddingToString(embedding);

        runOnUiThread(() -> {
            authViewModel.loginByFaceId(vectorStr).observe(this, resource -> {
                if (resource.status == Status.SUCCESS) {
                    txtStatus.setText("Đăng nhập thành công!");

                    // Lưu User và chuyển màn hình
                    SharedPrefManager.getInstance(this).saveUser(resource.data.getData());

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    txtStatus.setText("Không nhận diện được. Vui lòng thử lại.");
                    // Đợi 2 giây rồi cho phép quét lại
                    new android.os.Handler().postDelayed(() -> {
                        isProcessing = false;
                        txtStatus.setText("Đang quét khuôn mặt...");
                    }, 2000);
                }
            });
        });
    }

    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);
        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        faceNetHelper.close();
        cameraExecutor.shutdown();
    }
}