package com.bonghyerim.drug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView entpName, itemName, itemSeq, efcyQesitm, useMethodQesitm, atpnWarnQesitm, atpnQesitm, depositMethodQesitm;

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




        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=eMeMZoqhFkwFUee4yNBwZg%2BoTNqM6rt2YV6KEeoHyE7h24m5mOCcizeY%2B25N94lnG7%2BNO0AadU2Bk62Xg7Z6lQ%3D%3D&pageNo=1&numOfRows=3&entpName=&itemName=정&type=json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONArray items = response.getJSONObject("body").getJSONArray("items");
                            String itemNameText = items.getJSONObject(0).getString("itemName");

                            String entpNameText  = items.getJSONObject(0).getString("entpName") ;
                            int itemSeqText  = items.getJSONObject(0).getInt("itemSeq") ;
                            String efcyQesitmText  = items.getJSONObject(0).getString("efcyQesitm") ;
                            String useMethodQesitmText  = items.getJSONObject(0).getString("useMethodQesitm") ;
                            String atpnWarnQesitmText  = items.getJSONObject(0).getString("atpnWarnQesitm") ;
                            String atpnQesitmText  = items.getJSONObject(0).getString("atpnQesitm") ;
                            String depositMethodQesitmText  = items.getJSONObject(0).getString("depositMethodQesitm") ;

                            itemName.setText(itemNameText+"");
                            entpName.setText(entpNameText+"");
                            itemSeq.setText(itemSeqText+"");
                            efcyQesitm.setText(efcyQesitmText+"");
                            useMethodQesitm.setText(useMethodQesitmText+"");


                            if (atpnWarnQesitmText != "null" && !atpnWarnQesitmText.isEmpty()) {
                                atpnWarnQesitm.setText(atpnWarnQesitmText);
                            } else {
                                atpnWarnQesitm.setText("정보없음");
                            }


                            if (atpnQesitmText != "null" && !atpnQesitmText.isEmpty()) {
                                atpnQesitm.setText(atpnQesitmText);
                            } else {
                                atpnQesitm.setText("정보없음");
                            }


                            depositMethodQesitm.setText(depositMethodQesitmText+"");



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
