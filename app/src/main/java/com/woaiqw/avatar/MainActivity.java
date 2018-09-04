package com.woaiqw.avatar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.avatar.thread.ThreadMode;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_name);
        Avatar.get().register(this);
        //startActivity(new Intent(this, Main2Activity.class));
        startActivity(new Intent(this, HomeActivity.class));

    }

    @Subscribe(thread = ThreadMode.BACKGROUND, tag = BusConstants.CHANGE_TEXT)
    public void changeText(String s) {
        Log.e(TAG, s);
        Log.e(TAG, tv.toString());
        tv.setText(s);
        Log.e(TAG, "111");
    }

    @Subscribe(thread = ThreadMode.BACKGROUND, tag = BusConstants.CHANGE_COLOR)
    public void changeColor(String s) {
        Log.e(TAG, s);
        tv.setTextColor(Color.RED);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Avatar.get().unregister(this);
    }
}
