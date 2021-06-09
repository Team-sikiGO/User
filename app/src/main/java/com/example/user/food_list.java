package com.example.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

public class food_list extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        final ListView list = (ListView) findViewById(R.id.list01);
        final ListView menu = (ListView) findViewById(R.id.main_list);

        List<String> food_menu = new ArrayList<>();
        List<String> data = new ArrayList<>();

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
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });

        Intent food = getIntent();
        String tag = food.getStringExtra("it_tag");
        int[] cnt = new int[8];
        for (int i = 0; i < 8; i++)
            cnt[i] = 0;

        /* final GridView gridView = (GridView) findViewById(R.id.GridView01);*/

        food_menu.add("짜장면");
        food_menu.add("짬뽕");
        food_menu.add("간짜장");
        food_menu.add("쟁반짜장");
        food_menu.add("탕수육(소)");
        food_menu.add("탕수육(중)");
        food_menu.add("탕수육(대)");
        food_menu.add("양장피");

        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, food_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        menu.setAdapter(list_adapter);
        list.setAdapter(adapter);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String food = (String) adapterView.getItemAtPosition(position);
                cnt[position]++;
                data.clear();

                for (int n = 0; n < 8; n++) {
                    if (cnt[n] != 0)
                        data.add((String) adapterView.getItemAtPosition(n) + "  x  " + cnt[n]);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String text = (String) adapterView.getItemAtPosition(position);
                String[] array = text.split("  x  ");

                int n;
                for(n = 0; n < food_menu.size(); n++)
                {
                    boolean result = array[0].equals(food_menu.get(n));
                    if(result == true)
                    {
                        cnt[n]--;
                        if(cnt[n] == 0)
                            data.remove(position);
                        else
                            data.set(position, array[0] + "  x  " + cnt[n]);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void pay_page(View v) {

    }

    public void returntomain(View v) {
        startActivity(new Intent(getApplicationContext(), restaurant.class));
        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
        finish();
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
        startActivity(new Intent(getApplicationContext(), restaurant.class));
        overridePendingTransition(R.anim.horizon_exit, R.anim.none);
        finish();
    }
//    @Override
//    public void onBackPressed() {
//        long curTime = System.currentTimeMillis();
//        long gapTime = curTime - backBtnTime;
//
//        if(0 <= gapTime && 2000 >= gapTime) {
//            super.onBackPressed();
//        }
//        else {
//            backBtnTime = curTime;
//            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
//        }
//    }


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