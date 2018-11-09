package com.example.elab_yang.mmk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.elab_yang.mmk.R;
import com.example.elab_yang.mmk.adapter.MyRecyclerAdapter;
import com.example.elab_yang.mmk.model.CardItem;
import com.example.elab_yang.mmk.model.DB;

import java.util.ArrayList;
import java.util.List;

public class ReceiveDataActivity extends AppCompatActivity {
    private final static String TAG = ReceiveDataActivity.class.getSimpleName();

    DB db;
    SQLiteDatabase sql;

    String data = "";
    String abc = "";
    List<CardItem> lists;
    private MyRecyclerAdapter mAdapter;
    RecyclerView recycler_view;
    String[] data_detail = {"", "", "", ""};
//    String[] data_detail2 = {"", "", "", ""};
    // data_detail[0] = 품목;
    // data_detail[1] = 하위 품명;
    // data_detail[2] = 단위;
    // data_detail[3] = 투약시간;

    int hh = 0;
    int flag = 0;
    int time_flag = 0;

    String time_now = "";

    String morning = "";
    String afternoon = "";
    String dinner = "";
    String night = "";

    String[] hr = {"", ""};
    String[] hrr = {"", "", "", ""};
    String[] hrr2 = {"", "", "", ""};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdb);
//        setRecyclerView();
        flag = getIntent().getIntExtra("flag", flag);
//        flag = 1 : 1개 사용
//        flag = 2 : 2개 사용

        Log.d(TAG, "onCreate: flag " + flag);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        int i_start = pref.getInt("INDEX", 0);
        Log.d(TAG, "i_start = " + i_start);
        Log.d(TAG, i_start + "번째부터 블루투스값 받으면 돼");

        db = new DB(this);
        Intent intent = getIntent();
        data = intent.getStringExtra("BLE");
        // & = end bit로 구분
        Log.d(TAG, "data = " + data);

        int i = getCharNumber(data, '&');
        Log.d(TAG, "몇개의 데이터가 있을까? : " + i + "개 데이터 존재");

        int i_end = getCharNumber(data, '&');

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("INDEX", i_end);
        Log.d(TAG, "INDEX = " + i_end + "번째 데이터까지 읽어왔어.. 기억하자");
        editor.apply();

        if (i_start == i_end) {
            // 동기화할 데이터가 없음
            Toast.makeText(getApplicationContext(), "동기화할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: 동기화할 거 없음");
            finish();
        } else {
            String[] str = data.split("&");
            sql = db.getWritableDatabase();

            // str = 201811062153

            for (int y = i_start; y < i; y++) {
                Log.d(TAG, "지금 " + (y + 1) + "번째 진행중");
                Log.d(TAG, "str[y] 전체 = " + str[y]);
                Log.d(TAG, "str[y] 년도 = " + str[y].substring(0, 4));
                Log.d(TAG, "str[y] 월   = " + str[y].substring(4, 6));
                Log.d(TAG, "str[y] 일   = " + str[y].substring(6, 8));
                Log.d(TAG, "str[y] 시   = " + str[y].substring(8, 10));
                Log.d(TAG, "str[y] 분   = " + str[y].substring(10, 12));

                abc = str[y].substring(0, 4) + "." + str[y].substring(4, 6) + "." + str[y].substring(6, 8) + " " + str[y].substring(8, 10) + ":" + str[y].substring(10, 12);
                //                   년도                       월                             일                             시                               분

                if ((hh >= 5) && (hh < 11)) {
                    // 아침
                    time_now = "아침전";

                } else if ((hh >= 11) && (hh < 16)) {
                    // 점심
                    time_now = "점심전";

                } else if ((hh >= 16) && (hh < 21)) {
                    // 저녁
                    time_now = "저녁전";
                } else {
                    // 취침전
                    time_now = "취침전";
                }

                if (flag == 1) {
                    // 난 인슐린 한개만 쓰는거야
                    String set_data = "";
                    String only_one_needle_data = "";

                    only_one_needle_data = pref.getString("SET_DATA", set_data);

                    Log.d(TAG, "onCreate: only_one_needle_data " + only_one_needle_data);

                    data_detail = only_one_needle_data.split("#");
//            data_detail[0] = 품목
//            data_detail[1] = 품명
//            data_detail[2] = 단위

                    // 지금 시간 = str[y].substring(8, 10)

                    // 21-05(8h) : 취침전;
                    // 05-11(6h) : 아침식전;
                    // 11-16(5h) : 점심식전;
                    // 16-21(5h) : 저녁식전;
                    hh = Integer.parseInt(str[y].substring(8, 10));
                    Log.d(TAG, "onCreate: hh = " + hh);

                    setDB(abc, data_detail[0], data_detail[1], data_detail[2], time_now);
                    abc = "";
                    data = "";

                } else if (flag == 2) {
                    // 난 2개 써
                    String a1 = "";
                    String a2 = "";
                    String a3 = "";
                    String a4 = "";

                    morning = pref.getString("cache_data_1", a1);
                    afternoon = pref.getString("cache_data_2", a2);
                    dinner = pref.getString("cache_data_3", a3);
                    night = pref.getString("cache_data_4", a4);

                    Log.d(TAG, "onCreate: morning " + morning);
                    Log.d(TAG, "onCreate: afternoon " + afternoon);
                    Log.d(TAG, "onCreate: dinner " + dinner);
                    Log.d(TAG, "onCreate: night " + night);

                    hh = Integer.parseInt(str[y].substring(8, 10));
//                Log.d(TAG, "onCreate: hh = " + hh);

                    if ((hh >= 5) && (hh < 11)) {
                        // 아침
                        morning = pref.getString("cache_data_1", a1);
                        if (morning.contains("&&")) {
                            // 중복인거고
                            hr = morning.split("&&");
                            hrr = hr[0].split("/");
                            hrr2 = hr[1].split("/");
                            setDB(abc, hrr[0] + "/" + hrr2[0], hrr[1] + "/" + hrr2[1], hrr[2] + "/" + hrr2[2], time_now);

                        } else if (morning.startsWith("&")) {
                            // true 라면 이건 &품목/품명/단위 라는거고
                            hr = morning.split("&");
                            hrr = hr[1].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);

                        } else {
                            // 이건 풍목/품명/단위&
                            hr = morning.split("&");
                            hrr = hr[0].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);

                        }

                    } else if ((hh >= 11) && (hh < 16)) {
                        // 점심
                        afternoon = pref.getString("cache_data_2", a2);
                        if (afternoon.contains("&&")) {
                            // 중복인거고
                            hr = afternoon.split("&&");
                            hrr = hr[0].split("/");
                            hrr2 = hr[1].split("/");
                            setDB(abc, hrr[0] + "/" + hrr2[0], hrr[1] + "/" + hrr2[1], hrr[2] + "/" + hrr2[2], time_now);

                        } else if (afternoon.startsWith("&")) {
                            // true 라면 이건 &품목/품명/단위 라는거고
                            hr = afternoon.split("&");
                            hrr = hr[1].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);

                        } else {
                            // 이건 풍목/품명/단위&
                            hr = afternoon.split("&");
                            hrr = hr[0].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);
                        }

                    } else if ((hh >= 16) && (hh < 21)) {
                        // 저녁
                        dinner = pref.getString("cache_data_3", a3);
                        if (dinner.contains("&&")) {
                            // 중복인거고
                            hr = dinner.split("&&");
                            hrr = hr[0].split("/");
                            hrr2 = hr[1].split("/");
                            setDB(abc, hrr[0] + "/" + hrr2[0], hrr[1] + "/" + hrr2[1], hrr[2] + "/" + hrr2[2], time_now);

                        } else if (dinner.startsWith("&")) {
                            // true 라면 이건 &품목/품명/단위 라는거고
                            hr = dinner.split("&");
                            hrr = hr[1].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);

                        } else {
                            // 이건 풍목/품명/단위&
                            hr = dinner.split("&");
                            hrr = hr[0].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);
                        }


                    } else {
                        // 취침전
                        night = pref.getString("cache_data_4", a4);
                        if (night.contains("&&")) {
                            // 중복인거고
                            hr = night.split("&&");
                            hrr = hr[0].split("/");
                            hrr2 = hr[1].split("/");
                            setDB(abc, hrr[0] + "/" + hrr2[0], hrr[1] + "/" + hrr2[1], hrr[2] + "/" + hrr2[2], time_now);

                        } else if (night.startsWith("&")) {
                            // true 라면 이건 &품목/품명/단위 라는거고
                            hr = night.split("&");
                            hrr = hr[1].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);

                        } else {
                            // 이건 풍목/품명/단위&
                            hr = night.split("&");
                            hrr = hr[0].split("/");
                            setDB(abc, hrr[0], hrr[1], hrr[2], time_now);
                        }
                    }
//                setDB(abc, null, null, null, null);
                }
                abc = "";
                data = "";
            }
//        abc = "";
//        data = "";
            finish();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        if (flag == 1) {
//            // 난 인슐린 한개만 쓰는거야
//            String set_data = "";
//            String only_one_needle_data = "";
//
//            only_one_needle_data = pref.getString("SET_DATA", set_data);
//
//            Log.d(TAG, "onCreate: only_one_needle_data " + only_one_needle_data);
//
//            data_detail = only_one_needle_data.split("/");
////            data_detail[0] = 품목
////            data_detail[1] = 품명
////            data_detail[2] = 단위
//
//        } else if (flag == 2) {
//            // 난 2개 써
//            String a1 = "";
//            String a2 = "";
//            String a3 = "";
//            String a4 = "";
//
//            morning = pref.getString("cache_data_1", a1);
//            afternoon = pref.getString("cache_data_2", a2);
//            dinner = pref.getString("cache_data_3", a3);
//            night = pref.getString("cache_data_4", a4);
//
//            Log.d(TAG, "onCreate: morning " + morning);
//            Log.d(TAG, "onCreate: afternoon " + afternoon);
//            Log.d(TAG, "onCreate: dinner " + dinner);
//            Log.d(TAG, "onCreate: night " + night);
//
//        }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//    // 시간에 따른 식사상태 구분;
//    // 형식 : 00 ~ 24시
//    // 21-05(8h) : 취침전;
//    // 05-11(6h) : 아침식전;
//    // 11-16(5h) : 점심식전;
//    // 16-21(5h) : 저녁식전;
//    public String what_hh(int hh) {
//        if ((hh >= 05) && (hh < 11)) {
//            return "아침식전";
//        } else if ((hh >= 11) && (hh < 16)) {
//            return "점심식전";
//        } else if ((hh >= 16) && (hh < 21)) {
//            return "저녁식전";
//        } else {
//            return "취침전";
//        }
//    }

    // 특정문자 반복 갯수 확인
    int getCharNumber(String str, char c) {
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                cnt++;
        }
        return cnt;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    public void setDB(String str1, String str2, String str3, String str4, String str5) {
        sql = db.getWritableDatabase();
        sql.execSQL(String.format("INSERT INTO tb_needle VALUES(null, '%s', '%s', '%s', '%s', '%s')", str1, str2, str3, str4, str5));
        Toast.makeText(getApplicationContext(), "동기화햇구요", Toast.LENGTH_SHORT).show();
        sql.close();
        finish();
    }
}
