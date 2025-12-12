package com.example.astrabank;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<GetBankBranchActivity.Branch> branchList;
    private Location myCurrentLocation;
    private Polyline currentPolyline;

    private Button btnFindNearest;
    private LinearLayout infoPanel;
    private TextView tvNearestName, tvDistance;
    private Button btnGoNow;
    private GetBankBranchActivity.Branch nearestBranchFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnFindNearest = findViewById(R.id.btnFindNearest);
        infoPanel = findViewById(R.id.infoPanel);
        tvNearestName = findViewById(R.id.tvNearestName);
        tvDistance = findViewById(R.id.tvDistance);
        btnGoNow = findViewById(R.id.btnGoNow);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createDataWithCoordinates();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnFindNearest.setOnClickListener(v -> findNearestBranchLogic());

        btnGoNow.setOnClickListener(v -> {
            if (nearestBranchFound != null) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + nearestBranchFound.getLat() + "," + nearestBranchFound.getLng() + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    private void createDataWithCoordinates() {
        branchList = new ArrayList<>();
        branchList.add(new GetBankBranchActivity.Branch("Astrabank Cao Thang", "39 Cao Thang, District 3, HCMC", "028 3832 1234", 10.768100, 106.680000));
        branchList.add(new GetBankBranchActivity.Branch("Astrabank Da Nang", "21 Nguyen Van Linh, Da Nang", "0236 365 7890", 16.060100, 108.216000));
        branchList.add(new GetBankBranchActivity.Branch("Astrabank Ha Noi", "108 Tran Hung Dao, Hoan Kiem District", "024 3942 5678", 21.025200, 105.842000));
        branchList.add(new GetBankBranchActivity.Branch("Astrabank Can Tho", "12 Hoa Binh, Ninh Kieu District", "0292 381 2345", 10.033333, 105.783333));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            return;
        }

        enableUserLocation();

        for (GetBankBranchActivity.Branch branch : branchList) {
            LatLng branchLoc = new LatLng(branch.getLat(), branch.getLng());
            mMap.addMarker(new MarkerOptions()
                    .position(branchLoc)
                    .title(branch.getName())
                    .snippet("Phone: " + branch.getPhoneNumber())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.768100, 106.680000), 5));
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) myCurrentLocation = location;
            });
        }
    }

    private void findNearestBranchLogic() {
        if (myCurrentLocation == null) {
            Toast.makeText(this, "Getting your location...", Toast.LENGTH_SHORT).show();
            enableUserLocation();
            return;
        }

        double minDistance = Double.MAX_VALUE;
        GetBankBranchActivity.Branch nearest = null;

        for (GetBankBranchActivity.Branch branch : branchList) {
            float[] results = new float[1];
            Location.distanceBetween(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude(),
                    branch.getLat(), branch.getLng(), results);

            if (results[0] < minDistance) {
                minDistance = results[0];
                nearest = branch;
            }
        }

        if (nearest != null) {
            nearestBranchFound = nearest;
            showSuggestionUI(nearest, minDistance);
        }
    }

    private void showSuggestionUI(GetBankBranchActivity.Branch branch, double distance) {
        if (currentPolyline != null) currentPolyline.remove();

        LatLng userLatLng = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
        LatLng branchLatLng = new LatLng(branch.getLat(), branch.getLng());

        currentPolyline = mMap.addPolyline(new PolylineOptions()
                .add(userLatLng, branchLatLng)
                .width(10)
                .color(Color.BLUE)
                .geodesic(true));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(branchLatLng, 15));

        btnFindNearest.setVisibility(View.GONE);
        infoPanel.setVisibility(View.VISIBLE);
        tvNearestName.setText(branch.getName());
        tvDistance.setText("Distance: " + Math.round(distance) + " m");
    }
}
