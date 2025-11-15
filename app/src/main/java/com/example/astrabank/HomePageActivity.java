package com.example.astrabank; // Thay bằng package của bạn

import android.Manifest; // <-- QUAN TRỌNG
import android.content.Context;
import android.content.pm.PackageManager; // <-- QUAN TRỌNG
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat; // <-- QUAN TRỌNG
import androidx.core.content.ContextCompat; // <-- QUAN TRỌNG

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

// Đảm bảo tên class này khớp với tên file .java của bạn
public class HomePageActivity extends AppCompatActivity {

    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay; // Lớp overlay cho vị trí

    // Mã này để nhận diện khi hỏi xin quyền
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cấu hình osmdroid (phải gọi trước setContentView)
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_home_page); // Tải file XML

        // --- BƯỚC 1: XIN QUYỀN TRƯỚC KHI DÙNG BẢN ĐỒ ---
        // Yêu cầu các quyền cần thiết
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, // Quyền vị trí chính xác
                Manifest.permission.WRITE_EXTERNAL_STORAGE // Quyền ghi cache bản đồ
        });

        // --- BƯỚC 2: CÀI ĐẶT BẢN ĐỒ ---
        mapView = findViewById(R.id.map_view);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // --- BƯỚC 3: CÀI ĐẶT LỚP "VỊ TRÍ CỦA TÔI" ---
        // 1. Tạo overlay
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);

        // 2. Kích hoạt để nó bắt đầu tìm vị trí
        mLocationOverlay.enableMyLocation();

        // 3. Thêm nó vào bản đồ
        mapView.getOverlays().add(mLocationOverlay);

        // 4. (Tùy chọn) Tự động di chuyển bản đồ đến vị trí hiện tại
        //    ngay khi tìm thấy vị trí lần đầu tiên.
        mLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                // Phải chạy trên UI thread (luồng giao diện)
                if (mapView != null && mapView.getController() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Zoom vào mức 17 (mức đường phố)
                            mapView.getController().setZoom(17.0);
                            // Di chuyển camera đến vị trí của user
                            mapView.getController().animateTo(mLocationOverlay.getMyLocation());
                        }
                    });
                }
            }
        });

        // Bạn có thể XÓA code cũ về việc thêm Marker "TP. Hồ Chí Minh"
        // vì bây giờ chúng ta đã tự động tìm vị trí.
    }

    // --- CÁC HÀM QUẢN LÝ VÒNG ĐỜI (Lifecycle) RẤT QUAN TRỌNG ---

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // Bắt buộc
        if (mLocationOverlay != null) {
            mLocationOverlay.enableMyLocation(); // Bật lại khi app resume
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause(); // Bắt buộc
        if (mLocationOverlay != null) {
            mLocationOverlay.disableMyLocation(); // Tắt đi khi app pause để tiết kiệm pin
        }
    }

    // --- CÁC HÀM XIN QUYỀN (Runtime Permissions) ---

    /**
     * Hàm này được gọi sau khi người dùng bấm "Cho phép" hoặc "Từ chối"
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Chúng ta không cần xử lý gì phức tạp ở đây,
        // nếu người dùng từ chối, bản đồ sẽ không hiển thị vị trí của họ.
    }

    /**
     * Hàm kiểm tra xem đã có quyền chưa, nếu chưa thì hiển thị hộp thoại xin quyền
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            // Kiểm tra xem đã có quyền (granted) chưa
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa có, thêm vào danh sách cần xin
                permissionsToRequest.add(permission);
            }
        }

        // Nếu danh sách cần xin > 0 (tức là có quyền chưa được cấp)
        if (permissionsToRequest.size() > 0) {
            // Hiển thị hộp thoại xin quyền
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
        // Nếu đã có đủ quyền, app sẽ tiếp tục chạy bình thường
    }
}