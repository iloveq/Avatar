package com.woaiqw.avatar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.avatar.thread.ThreadMode;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = findViewById(R.id.tv_name_2);
        Avatar.get().register(this);
        startActivity(new Intent(this, HomeActivity.class));

    }


    @Subscribe(thread = ThreadMode.BACKGROUND, tag = BusConstants.CHANGE_TEXT)
    public void changeText(String s) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, s);
                Log.e(TAG, "111");
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Avatar.get().unregister(this);
    }

}
