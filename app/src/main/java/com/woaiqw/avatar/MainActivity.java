package com.woaiqw.avatar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.avatar.thread.ThreadMode;

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
        Intent intent = new Intent(this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //startActivity(new Intent(this, HomeActivity.class));

    }

    @Subscribe(thread = ThreadMode.MAIN, tag = BusConstants.CHANGE_TEXT)
    public void changeText(String s) {
        Log.e(TAG, Thread.currentThread().getName() + "- - -" + s);
        Log.e(TAG, Thread.currentThread().getName() + "- - - end");
    }

    @Subscribe(thread = ThreadMode.MAIN, tag = BusConstants.CHANGE_COLOR)
    public void changeColor(String s) {
        Log.e(TAG, Thread.currentThread().getName() + "- - -" + s);
        Log.e(TAG, Thread.currentThread().getName() + "- - - end");
    }


    @org.greenrobot.eventbus.Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e("EventBus", Thread.currentThread().getName() + "- - -" + event.text);
        tv.setText(event.text);
        Log.e("EventBus", Thread.currentThread().getName() + "- - - end");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Avatar.get().unregister(this);
    }
}
