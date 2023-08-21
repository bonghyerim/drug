package com.bonghyerim.drug;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bonghyerim.drug.config.Config;
import com.bonghyerim.drug.model.Drug;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView entpName, itemName, itemSeq, efcyQesitm, useMethodQesitm, atpnWarnQesitm, atpnQesitm, depositMethodQesitm;

    ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        entpName = findViewById(R.id.entpName);
        itemName = findViewById(R.id.itemName);
        itemSeq = findViewById(R.id.itemSeq);
        efcyQesitm = findViewById(R.id.efcyQesitm);
        useMethodQesitm = findViewById(R.id.useMethodQesitm);
        atpnWarnQesitm = findViewById(R.id.atpnWarnQesitm);
        atpnQesitm = findViewById(R.id.atpnQesitm);
        depositMethodQesitm = findViewById(R.id.depositMethodQesitm);
        itemImage = findViewById(R.id.itemImage);



        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Config.HOST + Config.PATH + Config.API_KEY + "&pageNo=1&numOfRows=10&entpName=&itemName=" + "아스피린" + "&type=json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONObject body = response.getJSONObject("body");
                            JSONArray items = body.getJSONArray("items");
                            JSONObject firstItem = items.getJSONObject(0);


                            Drug drug = new Drug();
                            drug.itemNameText = firstItem.getString("itemName");
                            drug.entpNameText = firstItem.getString("entpName");
                            drug.itemSeqText = Integer.parseInt(firstItem.getString("itemSeq")); // 파싱한 문자열을 정수로 변환
                            drug.efcyQesitmText = firstItem.getString("efcyQesitm");
                            drug.useMethodQesitmText = firstItem.getString("useMethodQesitm");
                            drug.atpnWarnQesitmText = firstItem.optString("atpnWarnQesitm", "정보없음"); // 기본값을 설정하여 null 처리
                            drug.atpnQesitmText = firstItem.optString("atpnQesitm", "정보없음"); // 기본값을 설정하여 null 처리
                            drug.depositMethodQesitmText = firstItem.getString("depositMethodQesitm");
                            drug.itemImageUrl = firstItem.getString("itemImage");




                            itemName.setText(drug.itemNameText+"");
                            entpName.setText(drug.entpNameText+"");
                            itemSeq.setText(drug.itemSeqText+"");
                            efcyQesitm.setText(drug.efcyQesitmText+"");
                            useMethodQesitm.setText(drug.useMethodQesitmText+"");



                            if (drug.atpnWarnQesitmText != "null" && !drug.atpnWarnQesitmText.isEmpty()) {
                                atpnWarnQesitm.setText(drug.atpnWarnQesitmText);
                            } else {
                                atpnWarnQesitm.setText("정보없음");
                            }


                            if (drug.atpnQesitmText != "null" && !drug.atpnQesitmText.isEmpty()) {
                                atpnQesitm.setText(drug.atpnQesitmText);
                            } else {
                                atpnQesitm.setText("정보없음");
                            }


                            depositMethodQesitm.setText(drug.depositMethodQesitmText+"");



                            Glide.with(MainActivity.this)
                                    .load(drug.itemImageUrl)
                                    .placeholder(R.drawable.baseline_image_24)
                                    .into(itemImage);


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

        queue.add(request);


    }



}
