package com.example.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.ArrayList;
import java.util.List;

public class food_list extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;
    private Button btn;
    private int foodCnt;
    String[] foodArr = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        int foodArr[] = new int[100];
        String foodList[] = new String[100];

/*
        final ListView menu = (ListView) findViewById(R.id.main_list);
        List<String> food_menu = new ArrayList<>();
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, food_menu);
        menu.setAdapter(list_adapter);
*/

        ListView m_oListView;
        ListAdapter oAdapter;

        // Adapter 생성
        oAdapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        m_oListView = (ListView) findViewById(R.id.main_list);
        m_oListView.setAdapter(oAdapter);

/*
        final ListView m_oListView = (ListView) findViewById(R.id.main_list);
        ArrayList<food_item> oData = new ArrayList<>();

                        ListAdapter oAdapter = new ListAdapter(oData);
                        m_oListView.setAdapter(oAdapter);
                        */


        final ListView list = (ListView) findViewById(R.id.list01);
        List<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);


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

                //Toast.makeText(getApplicationContext(), menu, Toast.LENGTH_SHORT).show();
                qr = generateQRCode(menu);
                Intent orderView = new Intent(getApplicationContext(), Order.class);
                orderView.putExtra("menu", menu);
                orderView.putExtra("qrcode", qr);
                startActivity(orderView);
                overridePendingTransition(R.anim.horizon_enter, R.anim.horizon_exit);
                finish();
            }
        });
        Intent food = getIntent();
        String resName = food.getStringExtra("가게이름");
        String tag = food.getStringExtra("it_tag");
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
                        Log.i("test", "test = 음식이름 : " + foodName);
                        foodList[j] = foodName;
                        oAdapter.addItem(foodName, "가격 : " + price);

/*                        food_item oItem = new food_item();
                        oItem.food_name = foodName;
                        oItem.price = "가격 : " + price + "원";
                        oData.add(oItem);*/
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FoodRequest foodRequest = new FoodRequest(resName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(food_list.this);
        queue.add(foodRequest);

        for (int i = 0; i < foodCnt; i++)
            foodArr[i] = 0;

        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                food_item item = (food_item) parent.getItemAtPosition(position);
                String food = item.getFood_name();
                String price = item.getPrice();

                data.clear();
                foodArr[position]++;
                for (int n = 0; n < foodCnt; n++) {
                    if (foodArr[n] != 0)
                        data.add(foodList[n] + "  x  " + foodArr[n]);

                }
                adapter.notifyDataSetChanged();
            }
        });

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String text = (String) adapterView.getItemAtPosition(position);
//                String[] array = text.split("  x  ");
//
//                int n;
//                for(n = 0; n < foodArr.length; n++)
//                {
//                    boolean result = array[0].equals();
//                    if(result == true)
//                    {
//                        cnt[n]--;
//                        if(cnt[n] == 0)
//                            data.remove(position);
//                        else
//                            data.set(position, array[0] + "  x  " + cnt[n]);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
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
        int actionId = R.id.page_my;
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.horizon_enter, R.anim.horizon_exit);
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