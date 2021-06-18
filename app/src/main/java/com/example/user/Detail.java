package com.example.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Detail extends AppCompatActivity {
    private Toolbar toolbar;
    protected BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;
    String clickDate[] = new String[100];
    String cDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        final ListView list;
        DetailAdapter dAdapter;
        dAdapter = new DetailAdapter();
        list = (ListView) findViewById(R.id.details_list);
        list.setAdapter(dAdapter);

        Intent detail = getIntent();
        String userID = detail.getStringExtra("userID");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String resName = jsonObject.getString("resName");
                        String date = jsonObject.getString("date");
                        String menu = jsonObject.getString("menu");
                        int price = jsonObject.getInt("price");
                        clickDate[j] = date;

                        dAdapter.addItem(resName, date);
                        dAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        OrderDetail orderDetail = new OrderDetail(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Detail.this);
        queue.add(orderDetail);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                cDate = clickDate[position];
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resName = jsonObject.getString("resName");
                            String date = jsonObject.getString("date");
                            String menu = jsonObject.getString("menu");
                            int price = jsonObject.getInt("price");

                            Bitmap qr;
                            qr = generateQRCode(menu);

                            Intent orderView = new Intent(getApplicationContext(), Order.class);
                            orderView.putExtra("menu", menu);
                            orderView.putExtra("qrcode", qr);
                            orderView.putExtra("totalPrice", price);
                            orderView.putExtra("userID", userID);
                            orderView.putExtra("resName", resName);

                            startActivity(orderView);
                            overridePendingTransition(R.anim.horizon_enter, R.anim.none);
//                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                OrderDetailClick orderDetailClick = new OrderDetailClick(cDate, userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Detail.this);
                queue.add(orderDetailClick);
            }
        });

        //Initialize And Assign Variable
        bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.page_order);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
                        return true;

                    case R.id.page_search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
                        return true;

                    case R.id.page_order:
                        return true;

                    case R.id.page_my:
                        startActivity(new Intent(getApplicationContext(), My.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
        updateNavigationBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.none, R.anim.horizon_exit);
    }

    private void updateNavigationBarState() {
        int actionId = R.id.page_order;
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap generateQRCode(String contents) {
        Bitmap bitmap = null;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 300, 300));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
