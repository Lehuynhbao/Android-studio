package com.example.scanqr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText user,pass;
    Button btnLogin;
    TextView txtRegister;
    String url1="http://192.168.1.6/android/checkLogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=(EditText) findViewById(R.id.username);
        pass=(EditText) findViewById(R.id.pass);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        txtRegister=(TextView) findViewById(R.id.txtRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=user.getText().toString().trim();
                String password=pass.getText().toString().trim();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Bạn phải nhập tài khoảng và mật khẩu!",Toast.LENGTH_SHORT).show();
                } else {
                    checkLogin(url1);
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,Register.class);
                startActivity(intent);
            }
        });
    }
    private void checkLogin(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("false")){
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,Mainhome.class);
                            intent.putExtra("username",response.trim());
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,"Tài khoản hoặc mật khẩu không đúng!",Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Xảy ra lỗi!!!",Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Lỗi!\n"+error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
                params.put("username",user.getText().toString().trim());
                params.put("password",pass.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}