package com.example.elab_yang.mmk.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.elab_yang.mmk.R;
import com.example.elab_yang.mmk.model.DB;

public class DeleteDataBaseActivity extends AppCompatActivity {
    private static final String TAG = "DeleteDataBaseActivity";
    Context mContext;
    DB db;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedb);
        mContext = this;
        setStatusbar();
        db = new DB(this);

        Button btnbtn = (Button) findViewById(R.id.btnbtn);
        btnbtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("진짜루")
                    .setMessage("저장된 내용을 전부 지울까요?")
                    .setPositiveButton("네", (dialog, which) -> {
                        Log.d(TAG, "onClick: db 클리어;;;");
                        sql = db.getWritableDatabase();
                        db.onUpgrade(sql, 1, 2);
                    })
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: db 휴 다행"))
                    .show()
                    .create();
        });
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}
