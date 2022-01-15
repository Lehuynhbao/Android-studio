package com.example.scanqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scanqr.fragment.Accountfragment;
import com.example.scanqr.fragment.Historyfragment;
import com.example.scanqr.fragment.Homefragment;
import com.example.scanqr.fragment.QRfragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_HOME=0;
    private static final int FRAGMENT_QR=1;
    private static final int FRAGMENT_HISTORY=2;
    private static final int FRAGMENT_ACCOUNT=3;
    private static final int FRAGMENT_LOGOUT=4;

    private int mCurrentFragment=FRAGMENT_HOME;
    private String url="http://192.168.1.6/android/showinfo.php";
    private DrawerLayout mDrawerLayout;
    TextView txtName;
    String username,info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=getIntent().getStringExtra("username");
        info=getIntent().getStringExtra("infor");
        if(username==null){
            username=getIntent().getStringExtra("user");
        }

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.navigation_view);
        txtName=navigationView.getHeaderView(0).findViewById(R.id.layout_header_hoten);
        showInfo(url,username);

        navigationView.setNavigationItemSelectedListener(this);

        Homefragment homefragment = new Homefragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", username);
        bundle.putString("info", info);
        homefragment.setArguments(bundle);
        replaceFragment(homefragment);

        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.nav_home:
                if(mCurrentFragment!=FRAGMENT_HOME){
                    Homefragment homefragment=new Homefragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user",username);
                    homefragment.setArguments(bundle);
                    replaceFragment(homefragment);
                    mCurrentFragment=FRAGMENT_HOME;
                }
                break;
            case R.id.nav_qr:
                if(mCurrentFragment!=FRAGMENT_QR){
                    QRfragment qRfragment=new QRfragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user",username);
                    qRfragment.setArguments(bundle);
                    replaceFragment(qRfragment);
                    mCurrentFragment=FRAGMENT_QR;
                }
                break;
            case R.id.nav_histori:
                if(mCurrentFragment!=FRAGMENT_HISTORY){
                    Historyfragment historyfragment=new Historyfragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user",username);
                    historyfragment.setArguments(bundle);
                    replaceFragment(historyfragment);
                    mCurrentFragment=FRAGMENT_HISTORY;
                }
                break;
            case R.id.nav_account:
                if(mCurrentFragment!=FRAGMENT_ACCOUNT){
                    Accountfragment accountfragment=new Accountfragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user",username);
                    accountfragment.setArguments(bundle);
                    replaceFragment(accountfragment);
                    mCurrentFragment=FRAGMENT_ACCOUNT;
                }
                break;
            case R.id.nav_logout:
                if(mCurrentFragment!=FRAGMENT_LOGOUT){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }
    public void showInfo(String url,String user) {

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                                txtName.setText(object.getString("Name"));
                                }
                            else {
                                Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Loi"+error.toString(), Toast.LENGTH_SHORT).show();
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
}