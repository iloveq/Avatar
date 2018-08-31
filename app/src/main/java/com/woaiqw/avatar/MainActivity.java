package com.woaiqw.avatar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_name);
        Bmob.get().register(this, Constants.CHANGE_TEXT, new IReceiver() {
            @Override
            public void accept(Bundle bundle) {
                String s = bundle.getString("Home");
                Log.e(TAG, s);
                tv.setText(s);
            }
        });
        startActivity(new Intent(this, Main2Activity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bmob.get().unregister(this);
    }
}
