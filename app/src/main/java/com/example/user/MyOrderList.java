package com.example.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyOrderList extends AppCompatActivity {
    private Toolbar toolbar;
    protected BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorderlist);

        // toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //Initialize And Assign Variable
        bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.page_my);

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
                        startActivity(new Intent(getApplicationContext(), Order.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
                        return true;

                    case R.id.page_my:
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
        overridePendingTransition(R.anim.none, R.anim.horizon_exit);
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
}
