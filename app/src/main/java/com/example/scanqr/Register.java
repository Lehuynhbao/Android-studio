package com.example.scanqr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {
    EditText ed_hoten,ed_user,ed_pass,ed_email;
    TextView txtLogin;
    Button btnRegister;
    private String url="http://192.168.1.6/android/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_hoten=(EditText) findViewById(R.id.register_hoten);
        ed_user=(EditText) findViewById(R.id.register_user);
        ed_pass=(EditText) findViewById(R.id.register_pass);
        ed_email=(EditText) findViewById(R.id.register_email);
        txtLogin=(TextView)findViewById(R.id.register_txtlogin);
        btnRegister=(Button)findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=ed_hoten.getText().toString().trim();
                String email=ed_email.getText().toString().trim();
                String user=ed_user.getText().toString().trim();
                String pass=ed_pass.getText().toString().trim();
                if(name.isEmpty()||email.isEmpty()||user.isEmpty()||pass.isEmpty()) {
                    Toast.makeText(Register.this,"Bạn phải nhập đủ thông tin!",Toast.LENGTH_SHORT).show();
                } else {
                    checkRegister(url,user,name,pass,email);
                }
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkRegister(String url,String user,String name,String pass,String email){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(Register.this,"Đăng kí thành công!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this,"Tên đăng nhập bị trùng!",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this,"Xảy ra lỗi!!!",Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Lỗi!\n"+error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
                params.put("user",user);
                params.put("pass",pass);
                params.put("email",email);
                params.put("hoten",name);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}