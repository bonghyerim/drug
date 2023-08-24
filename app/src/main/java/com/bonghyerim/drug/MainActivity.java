package com.bonghyerim.drug;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bonghyerim.drug.adapter.PlaceAdapter;
import com.bonghyerim.drug.api.NetworkClient;
import com.bonghyerim.drug.api.PlaceApi;
import com.bonghyerim.drug.config.Config;
import com.bonghyerim.drug.model.Place;
import com.bonghyerim.drug.model.PlaceList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editKeyword;
    ImageButton btnSearch;


    RecyclerView recyclerView;
    PlaceAdapter adapter;
    ArrayList<Place> placeArrayList = new ArrayList<>();

    // 내 위치 가져오기 위한 멤버변수
    LocationManager locationManager;
    LocationListener locationListener;

    double lat;
    double lng;

    int radius = 2000;  // 미터 단위
    String keyword;

    boolean isLocationReady;

    String pagetoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editKeyword = findViewById(R.id.editKeyword);
        btnSearch = findViewById(R.id.btnSearch);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCont = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCont){

                    if(pagetoken != null){
                        addNetworkData();
                    }

                }

            }
        });

        // 폰의 위치를 가져오기 위해서는, 시스템서비스로부터 로케이션 매니져를
        // 받아온다.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 로케이션 리스터를 만든다.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 여러분들의 로직 작성.

                // 위도 가져오는 코드.
                // location.getLatitude();
                // 경도 가져오는 코드.
                // location.getLongitude();

                lat = location.getLatitude();
                lng = location.getLongitude();

                isLocationReady = true;
            }
        };

        if( ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    100);
            return;
        }

        // 위치기반 허용하였으므로,
        // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                -1,
                locationListener);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isLocationReady == false){
                    Snackbar.make(btnSearch,
                            "아직 위치를 잡지 못했습니다. 잠시후 다시 검색하세요.",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }

                keyword = editKeyword.getText().toString().trim();

                Log.i("AAA", keyword);

                if(keyword.isEmpty()){
                    Log.i("AAA", "isEmpty");
                    return;
                }

                getNetworkData();

            }
        });

    }

    private void addNetworkData() {


        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlaceList("ko",
                lat+","+lng,
                radius,
                Config.GOOGLE_API_KEY,
                keyword);

        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {


                if(response.isSuccessful()){

                    PlaceList placeList = response.body();

                    pagetoken = placeList.next_page_token;

                    placeArrayList.addAll( placeList.results );

                    adapter.notifyDataSetChanged();

                }else{

                }

            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {

            }
        });

    }

    private void getNetworkData() {

        Log.i("AAA", "getNetworkData");



        placeArrayList.clear();

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlaceList("ko",
                lat+","+lng,
                radius,
                Config.GOOGLE_API_KEY,
                keyword);

        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {



                if(response.isSuccessful()){

                    PlaceList placeList = response.body();

                    pagetoken = placeList.next_page_token;

                    placeArrayList.addAll(placeList.results);

                    adapter = new PlaceAdapter(MainActivity.this, placeArrayList);
                    recyclerView.setAdapter(adapter);

                }{

                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){

            if( ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION} ,
                        100);
                return;
            }

            // 위치기반 허용하였으므로,
            // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    -1,
                    locationListener);

        }

    }




}