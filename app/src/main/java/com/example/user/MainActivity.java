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
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyGridAdapter extends BaseAdapter {
        Context context;

        public MyGridAdapter(Context c) {
            context = c; // context 변수를 다른 메소드에서 사용하기 위함
        }

        @Override
        public int getCount() { // 그리드뷰에 보여질 이미지의 개수를 반환하도록 수정
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

        // 이미지 파일을 배열로 초기화
        Integer[] posterID = {R.drawable.bunsik3, R.drawable.korea3, R.drawable.china3, R.drawable.japan3, R.drawable.italy3, R.drawable.cafe3
                , R.drawable.chicken3, R.drawable.pizza3, R.drawable.jokbo3, R.drawable.yasik3};

        // 카테고리를 문자 배열로 초기화
        String[] posterTitle = {"분식", "한식", "중식", "일식", "양식", "카페", "치킨", "피자", "족발/보쌈", "야식"};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { // 각 그리드뷰의 칸마다 이미지뷰를 생성해서 보여주게 함
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // 그리드뷰 칸의 중앙에 배치
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageResource(posterID[position]); //

            int pos = position;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent main = getIntent();
                    String userID = main.getStringExtra("userID");

                    Intent maintores = new Intent(v.getContext(), Restaurant.class);
                    maintores.putExtra("위치", pos);
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
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Activity MainActivity;
}