package com.example.scanqr.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scanqr.LoginActivity;
import com.example.scanqr.MainActivity;
import com.example.scanqr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Accountfragment extends Fragment {

    EditText name,email,address;
    Button btnEdit,btnChange;
    String user;
    private String url3="http://192.168.130.117/android/showinfo.php";
    private String url1="http://192.168.130.117/android/update.php";
    private String url2="http://192.168.130.117/android/changepass.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account,container,false);

        name=(EditText) view.findViewById(R.id.account_name);
        email=(EditText) view.findViewById(R.id.account_email);
        address=(EditText) view.findViewById(R.id.account_address);
        btnEdit=(Button) view.findViewById(R.id.account_btnExit);
        btnChange=(Button)view.findViewById(R.id.account_btnChange);

        Bundle bundle=getArguments();
        if(bundle!=null){
            user=bundle.getString("user");
        }
        showInfo(url3,user);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnEdit.getText().toString().trim().equals("Edit")) {
                    name.setEnabled(true);
                    email.setEnabled(true);
                    address.setEnabled(true);
                    btnEdit.setText("Save");
                    btnChange.setEnabled(false);
                }
                else {
                    btnEdit.setText("Edit");
                    btnChange.setEnabled(true);
                    name.setEnabled(false);
                    email.setEnabled(false);
                    address.setEnabled(false);
                    update(url1);
                }
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChange(url2);
            }
        });
        return view;
    }
    public void showInfo(String url,String user) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String suces = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(suces.equals("1")) {
                                JSONObject object = jsonArray.getJSONObject(0);
                                name.setText(object.getString("Name"));
                                email.setText(object.getString("Email"));
                                address.setText(object.getString("Address"));
                            }
                            else {
                                Toast.makeText(getActivity(), "Loi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Loi"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
                params.put("user",user);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void update(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(getActivity(),"Sửa thành công!",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(),"Sửa thất bại!",Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Xảy ra lỗi!!!",Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Lỗi!\n"+error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
                params.put("user",user);
                params.put("name",name.getText().toString().trim());
                params.put("email",email.getText().toString().trim());
                params.put("address",address.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void DialogChange(String url) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        TextView txtpass = (TextView) dialog.findViewById(R.id.dailog_change_pass);
        TextView txtnewpass = (TextView) dialog.findViewById(R.id.dailog_change_newpass);
        TextView txtnewpass1 = (TextView) dialog.findViewById(R.id.dailog_change_newpass1);
        Button btnChange = (Button) dialog.findViewById(R.id.dailog_change_btn);
        Button btnBack = (Button) dialog.findViewById(R.id.dailog_change_back);


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=txtpass.getText().toString().trim();
                String newpass=txtnewpass.getText().toString().trim();
                String newpass1=txtnewpass1.getText().toString().trim();
                if(pass.isEmpty()||newpass.isEmpty()||newpass1.isEmpty()){
                    Toast.makeText(getActivity(), "Bạn chưa điền đủ thông tin!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if(!newpass.equals(newpass1)){
                        Toast.makeText(getActivity(), "Mật khẩu mới không trùng nhau!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        ChangePass(url,pass,newpass);
                        dialog.dismiss();
                    }
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void ChangePass(String url,String pass,String newpass){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(getActivity(),"Đổi thành công!",Toast.LENGTH_SHORT).show();
                        } else {
                            if(response.trim().equals("errpass")) {
                                Toast.makeText(getActivity(), "Mật khẩu cũ không đúng!!!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Đổi thất bại!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Xảy ra lỗi!!!",Toast.LENGTH_SHORT).show();
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
                params.put("newpass",newpass);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
