package com.example.user;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My extends AppCompatActivity {
    private Toolbar toolbar;
    private final int GET_GALLERY_IMAGE = 200;
    private CircleImageView imageView_profile;
    BottomNavigationView bottomNavigationView;
    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

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
        imageView_profile = (CircleImageView) findViewById(R.id.profile);
        TextView textView_name = (TextView) findViewById(R.id.name);
        TextView textView_logout = (TextView) findViewById(R.id.account_logout);
        TextView textView_notice = (TextView) findViewById(R.id.notice);
        TextView textView_manage_account = (TextView) findViewById(R.id.manage_account);
        TextView textView_order_detail = (TextView) findViewById(R.id.order_detail);
        TextView textView_cs = (TextView) findViewById(R.id.cs);
        TextView textView_policy = (TextView) findViewById(R.id.policy);

        Privacy privacy = (Privacy)getApplicationContext();
        String Name = privacy.getName();
        String Number = privacy.getNumber();
        String str = Name + '\n' + '\n' + Number;
        textView_name.setText(str);
        Log.i("name", Name);
        Log.i("number", Number);

//        Intent fromlogin = getIntent();
//        String Name = fromlogin.getStringExtra("userName");
//        String Num = fromlogin.getStringExtra("userNumber");
//        Log.i("이름", Name);
//        Log.i("번호", Num);
//        String NameNum = Name + '\n' + '\n' + Num;
//        textView_name.setText(NameNum);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.profile:
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, GET_GALLERY_IMAGE);
//                        Toast.makeText(getApplicationContext(), "profile click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.name:
                        Toast.makeText(getApplicationContext(), "name click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.account_logout:
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                        finish();
                        break;

                    case R.id.notice:
                        Toast.makeText(getApplicationContext(), "noitce click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.manage_account:
                        Toast.makeText(getApplicationContext(), "manage_account click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.order_detail:
                        Toast.makeText(getApplicationContext(), "order_detail click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.cs:
                        Toast.makeText(getApplicationContext(), "cs click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.policy:
                        Toast.makeText(getApplicationContext(), "policy click", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        imageView_profile.setOnClickListener(clickListener);
        textView_name.setOnClickListener(clickListener);
        textView_logout.setOnClickListener(clickListener);
        textView_notice.setOnClickListener(clickListener);
        textView_manage_account.setOnClickListener(clickListener);
        textView_order_detail.setOnClickListener(clickListener);
        textView_cs.setOnClickListener(clickListener);
        textView_policy.setOnClickListener(clickListener);
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
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageView_profile.setImageURI(selectedImageUri);
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
}

