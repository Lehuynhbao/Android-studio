package com.example.scanqr.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scanqr.Account;
import com.example.scanqr.AccountAdapter;
import com.example.scanqr.MainActivity;
import com.example.scanqr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Historyfragment extends Fragment {
    TextView fist;
    Calendar calendar;
    ListView listAccount;
    ArrayList<Account> arrayAccount;
    AccountAdapter adapter;
    Button btnTK;
    private String url2="http://192.168.130.117/android/histori.php";
    private String url1="http://192.168.130.117/android/delete.php";
    String user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history,container,false);
        fist=(TextView) view.findViewById(R.id.fist);
        btnTK=(Button) view.findViewById(R.id.btnHistory);

        fist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTK.setText("Thống kê");
                chonngay(fist);
            }
        });

        Bundle bundle=getArguments();
        if(bundle!=null){
            user=bundle.getString("user");
        }

        getdata(url2,null);
        listAccount=(ListView) view.findViewById(R.id.history_list);
        arrayAccount=new ArrayList<>();
        adapter=new AccountAdapter(getActivity(),R.layout.list_home_layout,arrayAccount);
        listAccount.setAdapter(adapter);

        btnTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnTK.getText().equals("Thống kê")) {
                    String dayf = fist.getText().toString().trim();
                    if (dayf.isEmpty()) {
                        Toast.makeText(getActivity(), "Bạn phải chọn ngày", Toast.LENGTH_SHORT).show();
                    } else {
                        getdata(url2, dayf);
                        btnTK.setText("Hủy");
                    }
                } else {
                    fist.setText("");
                    btnTK.setText("Thống kê");
                    getdata(url2,null);
                }
            }
        });
        listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name=arrayAccount.get(position).getName();
                String email=arrayAccount.get(position).getEmail();
                String address=arrayAccount.get(position).getAddress();
                String day=arrayAccount.get(position).getDate();
                DialogDelete(url1,name,email,address,day);
            }
        });
        return view;
    }
    private void chonngay(TextView textView) {
        calendar=Calendar.getInstance();
        int ngay=calendar.get(Calendar.DATE);
        int thang=calendar.get(Calendar.MONDAY);
        int nam=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                textView.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
    private void getdata(String url,String f){
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
                if(f==null) {
                    params.put("user", user);
                    return params;
                }
                else {
                    params.put("user", user);
                    params.put("dayfist", f);
                    return params;
                }
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
                dialog.dismiss();
                getdata(url2,null);
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
