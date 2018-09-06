package com.woaiqw.avatar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.tv_close_main_page).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        EventBus.getDefault().post(new MessageEvent("改变背景颜色"));
        Avatar.get().post(BusConstants.CHANGE_TEXT, "我是：MainActivity");
        Avatar.get().post( BusConstants.CHANGE_COLOR, "#FF0000");

        finish();

    }
}
