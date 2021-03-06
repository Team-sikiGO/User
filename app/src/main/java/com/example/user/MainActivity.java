package com.example.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    protected BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        final GridView gv = (GridView) findViewById(R.id.gridView1);
        MyGridAdapter gAdapter = new MyGridAdapter(this);
        gv.setAdapter(gAdapter);

        Privacy privacy = (Privacy) getApplicationContext();
        String userID = privacy.getID();

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
                        return true;

                    case R.id.page_search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
                        return true;

                    case R.id.page_order:
                        Intent detail = new Intent(MainActivity.this, Detail.class);
                        detail.putExtra("userID", userID);
                        startActivity(detail);
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
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyGridAdapter extends BaseAdapter {
        Context context;

        public MyGridAdapter(Context c) {
            context = c; // context ????????? ?????? ??????????????? ???????????? ??????
        }

        @Override
        public int getCount() { // ??????????????? ????????? ???????????? ????????? ??????????????? ??????
            return posterID.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        // ????????? ????????? ????????? ?????????
        Integer[] posterID = {R.drawable.bunsik3, R.drawable.korea3, R.drawable.china3, R.drawable.japan3, R.drawable.italy3, R.drawable.cafe3
                , R.drawable.chicken3, R.drawable.pizza3, R.drawable.jokbo3, R.drawable.yasik3};

        // ??????????????? ?????? ????????? ?????????
        String[] posterTitle = {"??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????/??????", "??????"};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { // ??? ??????????????? ????????? ??????????????? ???????????? ???????????? ???
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // ???????????? ?????? ????????? ??????
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageResource(posterID[position]); //

            int pos = position;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent main = getIntent();
                    String userID = main.getStringExtra("userID");

                    Intent maintores = new Intent(v.getContext(), RestaurantList.class);
                    maintores.putExtra("??????", pos);
                    maintores.putExtra("userID", userID);
                    startActivity(maintores);
                    overridePendingTransition(R.anim.horizon_enter, R.anim.horizon_exit);
                    finish();
                }
            });
            return imageView;
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
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Activity MainActivity;
}