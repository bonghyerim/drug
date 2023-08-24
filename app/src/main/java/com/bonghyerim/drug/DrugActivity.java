package com.bonghyerim.drug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bonghyerim.drug.adapter.DrugAdapter;
import com.bonghyerim.drug.config.Config;
import com.bonghyerim.drug.model.Drug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DrugActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private DrugAdapter adapter; // 어댑터 변수
    private ArrayList<Drug> drugList = new ArrayList<>(); // 데이터 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug);

        // XML 레이아웃 요소와 연결
        searchEditText = findViewById(R.id.editKeyword);
        searchButton = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);

        // 어댑터 초기화 및 RecyclerView에 어댑터 설정
        adapter = new DrugAdapter(this, drugList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new DrugAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Drug drug) {
                // 약 정보를 DrugDetailActivity에 전달하고 해당 액티비티를 호출
                Intent intent = new Intent(DrugActivity.this, DrugDetailActivity.class);
                intent.putExtra("selectedDrug", drug);
                intent.putExtra("searchKeyword", drug.itemNameText);
                startActivity(intent);
            }
        });

        // 검색 버튼 클릭 리스너 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString();

                // 검색 결과 데이터를 가져오는 API 요청 및 응답 처리
                RequestQueue queue = Volley.newRequestQueue(DrugActivity.this);
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.GET,
                        Config.HOST + Config.PATH + Config.API_KEY + "&pageNo=1&numOfRows=10&entpName=&itemName=" + searchText + "&type=json",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject body = response.getJSONObject("body");
                                    JSONArray items = body.getJSONArray("items");

                                    // 기존 데이터 초기화
                                    drugList.clear();

                                    // 검색 결과 데이터 파싱 및 drugList에 추가
                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject item = items.getJSONObject(i);

                                        Drug drug = new Drug();
                                        drug.itemNameText = item.getString("itemName");
                                        drug.entpNameText = item.getString("entpName");
                                        drug.itemSeqText = Integer.parseInt(item.getString("itemSeq")); // 파싱한 문자열을 정수로 변환
                                        drug.efcyQesitmText = item.getString("efcyQesitm");
                                        drug.useMethodQesitmText = item.getString("useMethodQesitm");
                                        drug.atpnWarnQesitmText = item.optString("atpnWarnQesitm", "정보없음"); // 기본값을 설정하여 null 처리
                                        drug.atpnQesitmText = item.optString("atpnQesitm", "정보없음"); // 기본값을 설정하여 null 처리
                                        drug.depositMethodQesitmText = item.getString("depositMethodQesitm");
                                        drug.itemImageUrl = item.getString("itemImage");

                                        drugList.add(drug);
                                    }

                                    // 어댑터에 데이터 변경 알림
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Log.i("TEST", "파싱에러");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("TEST", error.toString());
                            }
                        }
                );

                // API 요청을 큐에 추가
                queue.add(request);
            }
        });
    }
}