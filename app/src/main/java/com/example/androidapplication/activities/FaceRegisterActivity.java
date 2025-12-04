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
//import android.os.CountDownTimer;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
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
//import com.example.androidapplication.utils.DialogUtils;
//import com.example.androidapplication.utils.FaceNetHelper;
//import com.example.androidapplication.viewmodel.UserViewModel;
//import com.google.common.util.concurrent.ListenableFuture;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.face.Face;
//import com.google.mlkit.vision.face.FaceDetection;
//import com.google.mlkit.vision.face.FaceDetector;
//import com.google.mlkit.vision.face.FaceDetectorOptions;
//
//import java.io.ByteArrayOutputStream;
//import java.nio.ByteBuffer;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class FaceRegisterActivity extends AppCompatActivity {
//
//    private PreviewView cameraPreview;
//    private TextView txtInstruction, txtTimer;
//    private Button btnStart;
//    private View overlayView;
//
//    private UserViewModel userViewModel;
//    private FaceNetHelper faceNetHelper;
//    private FaceDetector faceDetector;
//    private ExecutorService cameraExecutor;
//
//    private int currentStep = 0; // 0: Thẳng, 1: Trái, 2: Phải...
//    private boolean isRecording = false;
//    private boolean isProcessingFace = false; // Cờ để tránh xử lý liên tục gây lag
//    private String finalEmbeddingString = null; // Lưu kết quả tốt nhất
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_face_register);
//
//        cameraPreview = findViewById(R.id.camera_preview);
//        txtInstruction = findViewById(R.id.txt_instruction);
//        txtTimer = findViewById(R.id.txt_timer);
//        btnStart = findViewById(R.id.btn_start_record);
//        // overlayView = findViewById(R.id.overlay_view); // Nếu có
//
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        faceNetHelper = new FaceNetHelper(this);
//        cameraExecutor = Executors.newSingleThreadExecutor();
//
//        // Cấu hình ML Kit Face Detector
//        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
//                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
//                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
//                .build();
//        faceDetector = FaceDetection.getClient(options);
//
//        // Xin quyền Camera
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
//        } else {
//            startCamera();
//        }
//
//        btnStart.setOnClickListener(v -> startFaceRecordingProcess());
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
//                // ImageAnalysis để lấy frame ảnh xử lý AI
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
//                Log.e("CameraX", "Binding failed", e);
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    // Hàm xử lý từng frame ảnh từ Camera (Chạy ở Background Thread)
//    @SuppressLint("UnsafeOptInUsageError")
//    private void processImageProxy(ImageProxy imageProxy) {
//        if (!isRecording || currentStep != 0 || isProcessingFace) {
//            imageProxy.close();
//            return;
//        }
//
//        Image mediaImage = imageProxy.getImage();
//        if (mediaImage != null) {
//            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
//
//            // 1. Dùng ML Kit phát hiện khuôn mặt
//            faceDetector.process(image)
//                    .addOnSuccessListener(faces -> {
//                        if (!faces.isEmpty()) {
//                            // Chỉ lấy khuôn mặt to nhất
//                            Face face = faces.get(0);
//
//                            // 2. Cắt khuôn mặt ra khỏi ảnh gốc
//                            Bitmap frameBitmap = toBitmap(mediaImage);
//                            // Xoay ảnh nếu cần (Camera trước thường bị xoay)
//                            // Ở đây ta giả định ảnh đã ok hoặc cần rotateBitmap(frameBitmap, rotation)
//
//                            // Crop khuôn mặt
//                            Rect bounds = face.getBoundingBox();
//                            // Kiểm tra bounds hợp lệ
//                            if (bounds.left >= 0 && bounds.top >= 0 &&
//                                    bounds.right <= frameBitmap.getWidth() &&
//                                    bounds.bottom <= frameBitmap.getHeight()) {
//
//                                Bitmap faceBitmap = Bitmap.createBitmap(frameBitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
//
//                                // 3. Đưa vào FaceNet lấy Vector
//                                float[] embedding = faceNetHelper.getFaceEmbedding(faceBitmap);
//                                if (embedding != null) {
//                                    finalEmbeddingString = FaceNetHelper.embeddingToString(embedding);
//                                    Log.d("FaceAuth", "Đã lấy được vector khuôn mặt: " + finalEmbeddingString.substring(0, 20) + "...");
//                                    isProcessingFace = true; // Đánh dấu đã lấy được, không cần lấy nữa ở bước này
//                                }
//                            }
//                        }
//                    })
//                    .addOnCompleteListener(task -> imageProxy.close());
//        } else {
//            imageProxy.close();
//        }
//    }
//
//    // Utility: Chuyển YUV Image thành Bitmap
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
//    private void startFaceRecordingProcess() {
//        isRecording = true;
//        btnStart.setEnabled(false);
//        currentStep = 0;
//        isProcessingFace = false; // Reset cờ để bắt đầu lấy mẫu mới
//        finalEmbeddingString = null;
//        runStep();
//    }
//
//    private void runStep() {
//        String instruction = "";
//        switch (currentStep) {
//            case 0: instruction = "Giữ yên khuôn mặt và nhìn thẳng"; break; // Bước này quan trọng nhất để lấy Vector
//            case 1: instruction = "Quay mặt sang TRÁI từ từ"; break; // Các bước sau chỉ làm màu (Liveness)
//            case 2: instruction = "Quay mặt sang PHẢI từ từ"; break;
//            case 3: instruction = "Ngước mặt lên TRÊN"; break;
//            case 4: instruction = "Cúi mặt xuống DƯỚI"; break;
//            default:
//                finishRecording();
//                return;
//        }
//        txtInstruction.setText(instruction);
//
//        // Mỗi bước 3 giây
//        new CountDownTimer(3000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                txtTimer.setText((millisUntilFinished / 1000) + "s");
//            }
//
//            @Override
//            public void onFinish() {
//                currentStep++;
//                runStep();
//            }
//        }.start();
//    }
//
//    private void finishRecording() {
//        isRecording = false;
//        txtTimer.setText("");
//
//        if (finalEmbeddingString != null) {
//            txtInstruction.setText("Đang lưu dữ liệu...");
//
//            // Gọi API updateFaceData
//            userViewModel.updateFaceData(finalEmbeddingString).observe(this, resource -> {
//                switch (resource.status) {
//                    case SUCCESS:
//                        DialogUtils.showSuccessDialog(this, "Thành công", "Đăng ký FaceID hoàn tất!", () -> finish());
//                        break;
//                    case ERROR:
//                        DialogUtils.showErrorDialog(this, "Lỗi: " + resource.error.getMessage());
//                        btnStart.setEnabled(true);
//                        break;
//                }
//            });
//        } else {
//            txtInstruction.setText("Lỗi: Không nhận diện được khuôn mặt. Vui lòng thử lại.");
//            btnStart.setEnabled(true);
//        }
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
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.utils.FaceNetHelper;
import com.example.androidapplication.viewmodel.AuthViewModel;
import com.example.androidapplication.viewmodel.UserViewModel;
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

public class FaceRegisterActivity extends AppCompatActivity {

    private PreviewView cameraPreview;
    private TextView txtInstruction;
    private Button btnCapture;
//    private ImageView btnBack;

    private AuthViewModel authViewModel;
    private FaceNetHelper faceNetHelper;
    private FaceDetector faceDetector;
    private ExecutorService cameraExecutor;
    private UserViewModel userViewModel;

    // Biến lưu ảnh frame hiện tại để khi bấm chụp thì lấy dùng
    private Bitmap currentFrameBitmap = null;
    private Face currentFace = null;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_register);

        // 1. Ánh xạ View
        cameraPreview = findViewById(R.id.camera_preview);
        txtInstruction = findViewById(R.id.txt_instruction);
        btnCapture = findViewById(R.id.btn_start_record);
//        btnBack = findViewById(R.id.btn_back);

        // 2. Khởi tạo
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        faceNetHelper = new FaceNetHelper(this);
        cameraExecutor = Executors.newSingleThreadExecutor();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Cấu hình ML Kit (Chỉ tìm 1 khuôn mặt, mode chính xác cao)
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .build();
        faceDetector = FaceDetection.getClient(options);

        // 3. Sự kiện
//        btnBack.setOnClickListener(v -> finish());

        btnCapture.setOnClickListener(v -> {
            if (currentFrameBitmap != null && currentFace != null) {
                processFaceRegistration(currentFrameBitmap, currentFace);
            } else {
                Toast.makeText(this, "Chưa nhận diện được khuôn mặt. Hãy nhìn thẳng vào camera.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCapture.setEnabled(false); // Khóa nút chụp cho đến khi thấy mặt

        // 4. QUAN TRỌNG: Kiểm tra quyền Camera
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền Camera để sử dụng FaceID", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview Use Case
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                // Image Analysis Use Case (Xử lý từng frame ảnh)
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);

                // Chọn Camera trước
                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                // Unbind và Bind lại
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("FaceRegister", "Lỗi mở camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void analyzeImage(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            faceDetector.process(image)
                    .addOnSuccessListener(faces -> {
                        if (!faces.isEmpty()) {
                            // Tìm thấy mặt -> Lưu lại frame và face để dùng khi bấm nút chụp
                            currentFace = faces.get(0);
                            currentFrameBitmap = toBitmap(mediaImage); // Chuyển YUV sang Bitmap ngay (vì ImageProxy sẽ close)

                            // Cập nhật UI
                            runOnUiThread(() -> {
                                txtInstruction.setText("Đã thấy khuôn mặt. Giữ yên và bấm Chụp.");
                                txtInstruction.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                                btnCapture.setEnabled(true);
                                btnCapture.setAlpha(1.0f);
                            });
                        } else {
                            currentFace = null;
                            // Cập nhật UI
                            runOnUiThread(() -> {
                                txtInstruction.setText("Di chuyển khuôn mặt vào giữa khung hình");
                                txtInstruction.setTextColor(getResources().getColor(R.color.white));
                                btnCapture.setEnabled(false);
                                btnCapture.setAlpha(0.5f);
                            });
                        }
                    })
                    .addOnCompleteListener(task -> imageProxy.close()); // Quan trọng: Phải close để nhận frame tiếp theo
        } else {
            imageProxy.close();
        }
    }

    private void processFaceRegistration(Bitmap originalBitmap, Face face) {
        // Cắt khuôn mặt từ ảnh gốc
        // Lưu ý: Với Camera trước, ảnh có thể bị xoay. Ở đây giả sử ảnh thẳng (hoặc TFLite tự xử lý tốt)
        // Để đơn giản hóa cho đồ án, ta cắt theo bounding box.

        try {
            Rect bounds = face.getBoundingBox();

            // Kiểm tra tọa độ hợp lệ để tránh crash
            int x = Math.max(0, bounds.left);
            int y = Math.max(0, bounds.top);
            int width = Math.min(originalBitmap.getWidth() - x, bounds.width());
            int height = Math.min(originalBitmap.getHeight() - y, bounds.height());

            if (width > 0 && height > 0) {
                Bitmap faceBitmap = Bitmap.createBitmap(originalBitmap, x, y, width, height);

                // 1. Lấy Vector đặc trưng (Embedding)
                float[] embedding = faceNetHelper.getFaceEmbedding(faceBitmap);

                // 2. Chuyển thành chuỗi String
                String vectorString = FaceNetHelper.embeddingToString(embedding);

                // 3. Gọi API lưu lên server
                callApiRegisterFace(vectorString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi xử lý ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void callApiRegisterFace(String vector) {
        btnCapture.setEnabled(false);
        btnCapture.setText("Đang lưu...");

        userViewModel.updateFaceData(vector).observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                DialogUtils.showSuccessDialog(this, "Thành công", "Đã đăng ký FaceID!", () -> finish());
            } else if (resource.status == Status.ERROR) {
                btnCapture.setEnabled(true);
                btnCapture.setText("CHỤP & ĐĂNG KÝ");
                String msg = resource.error != null ? resource.error.getMessage() : "Lỗi không xác định";
                DialogUtils.showErrorDialog(this, msg);
            }
        });
    }

    // Hàm tiện ích: Chuyển YUV Image thành Bitmap
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