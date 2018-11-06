package com.example.elab_yang.mmk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.elab_yang.mmk.R;
import com.example.elab_yang.mmk.adapter.DeviceAdapter;
import com.example.elab_yang.mmk.model.Device;

import java.util.ArrayList;
import java.util.HashSet;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    Context mContext;
    RecyclerView recyclerView;
    DeviceAdapter deviceAdapter;
    HashSet<Device> deviceDatabase = new HashSet<>();
    ArrayList<Device> deviceArrayList;
    //    ImageView big_size_image;
    private Handler mHandler;
    // 처음 클리어 플래그
    int first_clear_flag = 0;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        setStatusbar();
        check_first();
        setNavi();
        Paper.init(this);
        setDevice();
//        hoxy_first();
    }

    // 처음인지 확인하자
    public void check_first() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        first_clear_flag = pref.getInt("first_clear_flag", first_clear_flag);
        Log.d(TAG, "first_clear_flag = " + first_clear_flag);
        if (first_clear_flag == 1) {
            // 너는 처음이 아니다.
            Log.d(TAG, "check_first: 너는 처음이 아니야");
        } else {
            // 너는 처음이야
            Log.d(TAG, "check_first: 너는 처음이야");
            hoxy_first();
        }
    }

    // 상태바
    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        // 앱 아이콘 검정색으로 변경
//        View view = getWindow().getDecorView();
//        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    // 네비게이션메뉴 설정
    public void setNavi() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mytoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // deviceAdapter 불러오기
    public void setDevice() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        deviceDatabase = Paper.book("device").read("user_device");
        if (deviceDatabase != null) {
            if (deviceDatabase.size() != 0) {
                deviceArrayList = new ArrayList<>(deviceDatabase);
                deviceAdapter = new DeviceAdapter(this, deviceArrayList);
                recyclerView.setAdapter(deviceAdapter);
                for (int i = 0; i < deviceArrayList.size(); i++) {
                    Device device = deviceArrayList.get(i);
                }
            }
        } else {
            Log.d(TAG, "setDevice: 장치x");
        }
    }

    // 혹시.. 처음이라면.. 가이드를 해주자
    public void hoxy_first() {
        mHandler = new Handler();
        runOnUiThread(() -> {
            // 1초 후
            mHandler.postDelayed(() -> {
                try {
                    showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
    }

    // 사용법을 알려드리까요?
    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                // 바깥 터치 잠금
                .setCancelable(false)
                .setTitle("HOXY..")
                .setMessage("사용법을 알려드리겠습니다.")
                .setPositiveButton("확인", (dialogInterface, which) -> {
                    first_clear();
                    Log.d(TAG, "onClick: 사용법에 대해 ㄱㄱ");
                    Intent intent = new Intent(MainActivity.this, AppGuidenceActivity.class);
                    startActivity(intent);
                    alert.dismiss();
                })
                .setNegativeButton("아니오", (dialog1, which) -> {
                    Log.d(TAG, "onClick: 필요없으시구나..");
                    first_clear();
                    alert.dismiss();
                });
        alert = dialog.create();
        alert.show();
    }

    // 처음인지 아닌지 구분하자
    public void first_clear() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        first_clear_flag = 1;
        // first_clear_flag == 1 : 처음 사용하느 유저가 아니다~
        editor.putInt("first_clear_flag", first_clear_flag);
        editor.apply();
//        finish();
    }

    // 뒤로가기 버튼 클릭시
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 네비게이션메뉴 클릭 이벤트
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_profile:
                // 호구조사
//                Toast.makeText(getApplicationContext(),"장치 추가", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.nav_view_insulin:
                // 인슐린 보기
//                Toast.makeText(getApplicationContext(),"인슐린 보기", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ViewInsulinActivity.class));
                break;

            case R.id.nav_set_insulin:
                // 인슐린 설정
                onCreateDialog();
                Toast.makeText(getApplicationContext(), "인슐린 설정", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_add_device:
                // 장치 추가
                Toast.makeText(getApplicationContext(), "장치 추가", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, NeedleScanActivity.class));
                break;

            case R.id.nav_view_database:
                // VIEW DATABASE
                Toast.makeText(getApplicationContext(), "VIEW DATABASE", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DataBaseActivity.class));
                break;

            case R.id.nav_education:
                // 유튜브 영상 페이지 설정
//                Toast.makeText(getApplicationContext(), "인슐린 설정", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, EduYoutubeActivity.class));
                break;

            case R.id.nav_edit_profile:
                // 개인정보 입력
//                Toast.makeText(getApplicationContext(), "개인정보 입력", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.nav_delete_database:
                // DATABASE + CACHE CLEAR;
                Toast.makeText(getApplicationContext(), "DATABASE + CACHE CLEAR", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;

            case R.id.nav_its_me:
                // 개발자 정보
                Toast.makeText(getApplicationContext(), "개발자 정보", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;

//            case R.id.nav_setting:
//                //환경설정
////                Toast.makeText(getApplicationContext(),"환경설정", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, SettingActivity.class));
//                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void onCreateDialog() {
        final String[] items = {"인슐린 1개", "인슐린 2개", "알약"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("선택해");
        builder.setSingleChoiceItems(items, 0, (DialogInterface dialog, int which) -> {
            Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();
            if (which == 0) {
                // 약 1개
//                    Toast.makeText(getApplicationContext(),"인슐린 1개", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SettingInsulinActivity.class));
            } else if (which == 1) {
                // 약 2개
//                    Toast.makeText(getApplicationContext(),"인슐린 2개", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, TwoInsulinActivity.class));
            } else {
                // 알약
//                    Toast.makeText(getApplicationContext(),"알약설정페이지로", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "만드는 중", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, SettingInsulinActivity.class));

            }
            dialog.dismiss(); // 누르면 바로 닫히는 형태
        })
                .show();
    }
}
