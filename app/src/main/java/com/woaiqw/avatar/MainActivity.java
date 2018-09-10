package com.woaiqw.avatar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.woaiqw.avatar.annotation.Subscribe;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_name);
        Avatar.get().register(this);
        EventBus.getDefault().register(this);
        tv.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HomeActivity.class)));

    }

    @Subscribe(tag = BusConstants.CHANGE_TEXT)
    public void changeText(String s) {
        Log.e(TAG, s);
        tv.setText(s);
    }

    @Subscribe(tag = BusConstants.CHANGE_COLOR)
    public void changeColor(String s) {
        Log.e(TAG, s);
        tv.setTextColor(Color.parseColor(s));
    }


    @org.greenrobot.eventbus.Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e("EventBus", Thread.currentThread().getName() + "- - -" + event.text);
        tv.setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Avatar.get().unregister(this);
    }
}
