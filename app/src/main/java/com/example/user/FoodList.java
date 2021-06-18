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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodList extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;
    private Button btn;
    private int foodCnt;
    private int TotalPay = 0;
    NumberFormat numberFormat;
    int resID;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);
        int foodNum[] = new int[100]; // 주문할 음식들의 최대개수
        int foodPay[] = new int[100]; // 주문할 음식들의 가격
        String foodList[] = new String[100]; //주문 음식 리스트
        Intent food = getIntent();
        userID = food.getStringExtra("userID");
        String resName = food.getStringExtra("가게이름");
        resID = food.getIntExtra("resID", 0);

        Privacy privacy = (Privacy) getApplicationContext();
        String userID2 = privacy.getID();

        ListView m_oListView;
        ListAdapter oAdapter;
        final TextView text = (TextView) findViewById(R.id.TotalPrice);

        oAdapter = new ListAdapter();
        m_oListView = (ListView) findViewById(R.id.main_list);
        m_oListView.setAdapter(oAdapter);

        final ListView list = (ListView) findViewById(R.id.list01);
        List<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        oAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        //Initialize And Assign Variable
        bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.page_home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
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
                        startActivity(new Intent(getApplicationContext(), Order.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
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

        int[] cnt = new int[8];
        for (int i = 0; i < 8; i++)
            cnt[i] = 0;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    foodCnt = jsonArray.length();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String foodName = jsonObject.getString("foodName");
                        int price = jsonObject.getInt("price");
                        foodList[j] = foodName;
                        foodPay[j] = price;
                        numberFormat = new DecimalFormat("###,###");
                        oAdapter.addItem(foodName, "가격 : " + numberFormat.format(price));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FoodRequest foodRequest = new FoodRequest(resName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FoodList.this);
        queue.add(foodRequest);

        for (int i = 0; i < foodCnt; i++)
            foodNum[i] = 0;


        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                btn.setEnabled(true);
                btn.setVisibility(View.VISIBLE);
                FoodItem item = (FoodItem) parent.getItemAtPosition(position);
                String food = item.getFood_name();
                String price = item.getPrice();


                data.clear();
                foodNum[position]++;
                TotalPay = 0;
                for (int n = 0; n < foodCnt; n++) {
                    if (foodNum[n] != 0) {
                        data.add(foodList[n] + "  x  " + foodNum[n]);
                        TotalPay += foodPay[n] * foodNum[n];
                    }
                }
                numberFormat = new DecimalFormat("###,###");
                text.setText("가격 : " + numberFormat.format(TotalPay) + "원");
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String spl = (String) adapterView.getItemAtPosition(position);
                String[] array = spl.split("  x  ");

                int n;
                for (n = 0; n < foodList.length; n++) {
                    boolean result = array[0].equals(foodList[n]);
                    if (result == true) {
                        foodNum[n]--;
                        TotalPay = TotalPay - foodPay[n] * 1;
                        if (foodNum[n] == 0)
                            data.remove(position);
                        else
                            data.set(position, array[0] + "  x  " + foodNum[n]);
                        numberFormat = new DecimalFormat("###,###");
                        text.setText("가격 : " + numberFormat.format(TotalPay) + "원");
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        /* QR 코드 생성 버튼 클릭시 이벤트 처리 */
        btn = (Button) findViewById(R.id.createQR);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap qr;
                String menu = "";
                for (int i = 0; i < data.size(); i++) {
                    String str = data.get(i);
                    String[] arr = str.split(" x ");
                    menu = menu.concat(arr[0]);
                    menu = menu.concat("\t\t\t· · · · ·\t\t\t");
                    menu = menu.concat(" X");
                    menu = menu.concat(arr[1]);
                    if (i == data.size() - 1) {
                        break;
                    } else {
                        menu = menu.concat("\n");
                    }
                }

                qr = generateQRCode(menu);
                Intent orderView = new Intent(getApplicationContext(), Order.class);
                orderView.putExtra("menu", menu);
                orderView.putExtra("qrcode", qr);
                orderView.putExtra("totalPrice", TotalPay);
                orderView.putExtra("userID", userID);
                orderView.putExtra("resName", resName);

                long now = System.currentTimeMillis();
                Date mdate = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                String date = sdf.format(mdate);

//                Log.i("주문내역1", userID);
//                Log.i("주문내역2", resName);
//                Log.i("주문내역3", menu);
//                Log.i("주문내역4", TotalPay + "");
//                Log.i("주문내역5", date);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success)
                                Toast.makeText(getApplicationContext(), "주문 내역 저장에 성공", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                OrderListRequest orderListRequest = new OrderListRequest(userID, resName, menu, TotalPay, date, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FoodList.this);
                queue.add(orderListRequest);

                startActivity(orderView);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
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
        int actionId = R.id.page_home;
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FoodList.this, Restaurant.class);
        intent.putExtra("userID", userID);
        intent.putExtra("위치", resID);
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.none);
        finish();
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