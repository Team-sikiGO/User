package com.example.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Order extends AppCompatActivity {
    private Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;
    private Bitmap qr;
    private ImageView img;
    private TextView txt;
    String menu;
    int price;
    String resName;
    String userID;
    NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // QR code set to ImageView
        Intent intent = getIntent();
        qr = (Bitmap) intent.getParcelableExtra("qrcode");
        menu = (String) intent.getStringExtra("menu");
        userID = intent.getStringExtra("userID");
        price = intent.getIntExtra("totalPrice", 1);
        resName = intent.getStringExtra("resName");

        TextView ResName = (TextView) findViewById(R.id.restaurant_name);
        ResName.setText(resName);

        numberFormat = new DecimalFormat("###,###");
        TextView prc = (TextView) findViewById(R.id.price);
        prc.setText("금액 : " + numberFormat.format(price) + "원");

        String printMenu = "";
        menu = menu.substring(7);
        String[] arr = menu.split("/");
        int size = arr.length;
        for (int i = 0; i < size - 1; i = i + 2) {
            printMenu = printMenu.concat(arr[i]);
            printMenu = printMenu.concat("\t\t\t· · · · ·\t\t\t");
            printMenu = printMenu.concat(" X");
            printMenu = printMenu.concat(arr[i+1]);
            if(i != (size - 3)) {
                printMenu = printMenu.concat("\n");
            }
        }

        txt = (TextView) findViewById(R.id.menu);
        txt.setText(printMenu);

        img = (ImageView) findViewById(R.id.qrcode);
        img.setImageBitmap(qr);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QRcode_popup.class);
                intent.putExtra("qrcode", qr);
                startActivity(intent);
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
//        updateNavigationBarState();
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

}