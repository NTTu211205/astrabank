package com.example.astrabank;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class FaceAuthenticationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final String TAG = "FaceAuthActivity";

    // Khai báo Views
    private PreviewView previewView;
    private TextView tvStatus, tvSubtitle;
    private ProgressBar progressBar;
    private Button btnStartScan, btnSkip;

    // Biến xử lý logic giả lập quét
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_authentication);
        initViews();
        setupEvents();

        if (checkCameraPermission()) {
            startCamera();
        } else {
            requestCameraPermission();
        }
    }

    private void initViews() {
        previewView = findViewById(R.id.previewView);
        tvStatus = findViewById(R.id.tvStatus);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        progressBar = findViewById(R.id.progressBar);
        btnStartScan = findViewById(R.id.btnStartScan);
        btnSkip = findViewById(R.id.btnSkip);
    }

    private void setupEvents() {
        btnSkip.setOnClickListener(v -> {
            Toast.makeText(this, "Đã bỏ qua đăng ký khuôn mặt", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnStartScan.setOnClickListener(v -> startScanningSimulation());
    }

    // --- LOGIC CAMERAX ---

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                try {
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview);

                } catch (Exception exc) {
                    Log.e(TAG, "Không thể khởi động camera.", exc);
                    Toast.makeText(this, "Lỗi khởi động camera", Toast.LENGTH_SHORT).show();
                }

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Lỗi camera provider", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private void startScanningSimulation() {
        btnStartScan.setEnabled(false);
        btnSkip.setEnabled(false);
        progressStatus = 0;

        tvStatus.setText("Đang định vị khuôn mặt...");
        tvStatus.setTextColor(ContextCompat.getColor(this, R.color.black)); // Hoặc màu primary

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 2;

                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    updateStatusText(progressStatus);
                });

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            handler.post(() -> {
                tvStatus.setText("Đăng ký thành công!");
                tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                Toast.makeText(FaceAuthenticationActivity.this, "Khuôn mặt đã được lưu!", Toast.LENGTH_LONG).show();

                new Handler().postDelayed(this::finish, 1000);
            });
        }).start();
    }

    private void updateStatusText(int progress) {
        if (progress < 20) {
            tvStatus.setText("Giữ yên thiết bị...");
        } else if (progress < 50) {
            tvStatus.setText("Đang quét các điểm đặc trưng...");
        } else if (progress < 80) {
            tvStatus.setText("Đang xác thực...");
        } else {
            tvStatus.setText("Hoàn tất!");
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Cần cấp quyền Camera để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}