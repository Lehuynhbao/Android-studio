package com.example.scanqr.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scanqr.Account;
import com.example.scanqr.AccountAdapter;
import com.example.scanqr.LoginActivity;
import com.example.scanqr.MainActivity;
import com.example.scanqr.R;
import com.example.scanqr.scannerView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Homefragment extends Fragment {

    ListView listAccount;
    ArrayList<Account> arrayAccount;
    AccountAdapter adapter;
    private String url="http://192.168.130.117/android/getdata.php";
    private String url1="http://192.168.130.117/android/showinfo.php";
    private String url2="http://192.168.130.117/android/insert.php";
    private String url3="http://192.168.130.117/android/delete.php";
    String user,info;
    SearchView searchView;
    Button btnQR;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        Bundle bundle=getArguments();
        if(bundle!=null){
            user=bundle.getString("user");
            info=bundle.getString("info");
        }
        getdata(url);
        listAccount=(ListView) view.findViewById(R.id.home_list);
        arrayAccount=new ArrayList<>();
        adapter=new AccountAdapter(getActivity(),R.layout.list_home_layout,arrayAccount);
        listAccount.setAdapter(adapter);

        searchView=(SearchView) view.findViewById(R.id.home_search);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        btnQR=(Button) view.findViewById(R.id.btnqr);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), scannerView.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name=arrayAccount.get(position).getName();
                String email=arrayAccount.get(position).getEmail();
                String address=arrayAccount.get(position).getAddress();
                String day=arrayAccount.get(position).getDate();
                DialogDelete(url3,name,email,address,day);
            }
        });
        if(info!=null){
            DialogShow(url1,info);
            info=null;
        }
        return view;
    }
    private void getdata(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        arrayAccount.clear();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String suces=jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if(suces.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    arrayAccount.add(new Account(
                                            object.getString("Name"),
                                            object.getString("Address"),
                                            object.getString("Email"),
                                            object.getString("Image"),
                                            object.getString("QR"),
                                            object.getString("Ngay")
                                    ));
                                    adapter.notifyDataSetChanged();
                                }
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
    public void DialogShow(String url,String info1) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_showinfor);

        TextView txtname = (TextView) dialog.findViewById(R.id.dailog_showinfor_hoten);
        TextView txtemail = (TextView) dialog.findViewById(R.id.dailog_showinfor_email);
        TextView txtdiachi = (TextView) dialog.findViewById(R.id.dailog_showinfor_diachi);
        Button btnContinue=(Button) dialog.findViewById(R.id.dailog_showinfor_btnContinue);
        Button btnadd=(Button) dialog.findViewById(R.id.dailog_showinfor_btnOut);

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
                                txtname.setText(object.getString("Name"));
                                txtemail.setText("Email: "+object.getString("Email"));
                                txtdiachi.setText("Address: "+object.getString("Address"));
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
                params.put("user",info);

                return params;
            }
        };

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(url2,info1);
                Intent intent= new Intent(getActivity(), MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), scannerView.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        requestQueue.add(stringRequest);
        dialog.show();
    }
    private void insert(String url,String info1){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("false")){
                            Toast.makeText(getActivity(),"Thêm thành công",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Xảy ra lỗi!!!"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
                params.put("user1",user);
                params.put("user2",info1);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void DialogDelete(String url,String name,String email,String address,String day){
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);

        TextView txtname = (TextView) dialog.findViewById(R.id.dailog_delete_hoten);
        TextView txtemail = (TextView) dialog.findViewById(R.id.dailog_delete_email);
        TextView txtdiachi = (TextView) dialog.findViewById(R.id.dailog_delete_diachi);
        Button btnDelete=(Button) dialog.findViewById(R.id.dailog_delete_btndelete);
        Button btnBack=(Button) dialog.findViewById(R.id.dailog_delete_btnOut);

        txtname.setText(name);
        txtemail.setText(email);
        txtdiachi.setText(address);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(!response.trim().equals("false")){
                                    Toast.makeText(getActivity(),"Xóa thành công!",Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(),"Xóa thất bại!",Toast.LENGTH_SHORT).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),"Xảy ra lỗi!!!"+error.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> params=new HashMap<>();
                        params.put("user1",user);
                        params.put("ngay",day);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                Intent intent=new Intent(getActivity(),MainActivity.class);
                intent.putExtra("username",user);
                startActivity(intent);
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

}
