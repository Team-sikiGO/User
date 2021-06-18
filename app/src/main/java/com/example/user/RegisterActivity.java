package com.example.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_number;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 아이디 값 찾아주기
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // 회원가입 버튼 클릭 시 수행
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_id.getText().toString().replace(" ", " ").equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("아이디를 입력해주세요.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }
                Pattern p = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
                Matcher m = p.matcher((et_id).getText().toString());
                if (!m.matches()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("아이디가 이메일 형식이 아닙니다.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }
                if (et_pass.getText().toString().replace(" ", " ").equals("") || et_pass.length() < 8) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("비밀번호는 8자 이상이어야 합니다.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }
                if (et_name.getText().toString().replace(" ", " ").equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("이름을 입력해주세요.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }
                if (et_number.getText().toString().replace(" ", " ").equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("번호를 입력해주세요.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }
                // EditText에 현재 입력되어있는 값을 get해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String userNumber = et_number.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록에 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록에 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                // 서버로 Volley를 이용해서 요청을 함
                RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, userNumber, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });
    }
}