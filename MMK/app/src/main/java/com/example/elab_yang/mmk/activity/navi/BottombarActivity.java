package com.example.elab_yang.mmk.activity.navi;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.elab_yang.mmk.CallLogFragment;
import com.example.elab_yang.mmk.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class BottombarActivity extends FragmentActivity {
    private CallLogFragment callLogFragment;


    @Override
    protected void onCreate(Bundle saveedInstanceState){
        super.onCreate(saveedInstanceState);
        setContentView(R.layout.activity_bottombar);

        callLogFragment = new CallLogFragment();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(tabId -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if(tabId == R.id.tab_call_log){
                transaction.replace(R.id.contentContainer, callLogFragment).commit();
            }
        });

    }

}
