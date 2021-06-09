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
                qr = generateQRCode("https://www.naver.com");
                Intent orderView = new Intent(getApplicationContext(), Order.class);
                orderView.putExtra("qrcode", qr);
                startActivity(orderView);
            }
        });

        Intent food = getIntent();
        String tag = food.getStringExtra("it_tag");
        int[] cnt = new int[16];
        for(int i = 0; i<16;i++)
            cnt[i] = 1;

        final GridView gridView = (GridView)findViewById(R.id.GridView01);
        final ListView list = (ListView)findViewById(R.id.list01);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter);

        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int checked ;
                int count = adapter.getCount() ;

                if (cnt[position] > 1)
                {
                    if (count > 0) {
                        checked = position;
                        if (checked > -1 && checked < count) {
                            data.set(checked, position + "  x  " + cnt[position]) ;
                            adapter.notifyDataSetChanged();
                            cnt[position]++;
                        }
                    }
                }
                else{
                    data.add(position + "  x  " + cnt[position]);
                    adapter.notifyDataSetChanged();
                    cnt[position]++;
                }
            }
        });


    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private Integer[] mThumIds = {
                R.drawable.icon, R.drawable.icon,
                R.drawable.icon, R.drawable.icon,
                R.drawable.icon, R.drawable.icon,
                R.drawable.icon, R.drawable.icon,
                R.drawable.icon, R.drawable.icon,
                R.drawable.icon, R.drawable.icon,
        };
        public ImageAdapter(Context c) {
            mContext = c;
        }
        @Override
        public int getCount() {
            return mThumIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(320, 240));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            }
            else {
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(mThumIds[position]);
            return imageView;
        }
    }

    public void pay_page(View v){

    }
    public void returntomain(View v){
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

    private void updateNavigationBarState(){
        int actionId = R.id.page_my;
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.none, R.anim.horizon_exit);
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
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                return true;
            case R.id.account:
                Toast.makeText(getApplicationContext(), "Account", Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap generateQRCode(String contents) {
        Bitmap bitmap = null;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 300 , 300));
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