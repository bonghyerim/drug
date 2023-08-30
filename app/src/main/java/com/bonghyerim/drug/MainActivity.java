package com.bonghyerim.drug;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private EditText editKeyword;
    private ImageButton btnSearch;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editKeyword = findViewById(R.id.editKeyword);
        btnSearch = findViewById(R.id.btnSearch);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                // 위치 권한 요청
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }

                // 사용자의 위치 표시
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.addMarker(new MarkerOptions().position(userLatLng).title("My Location"));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
                                }
                            }
                        });

                // 검색 버튼 클릭 리스너 설정
                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String keyword = editKeyword.getText().toString();
                        // 키워드를 사용하여 검색 기능 구현
                        // 주변에 마커 추가 등
                        // Google Places API 활용
                        // Place API 사용 예시: https://developers.google.com/maps/documentation/places/web-service/search
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
