package com.woaiqw.abmob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.woaiqw.bmob.Bmob;
import com.woaiqw.bmob.IReceiver;
import com.woaiqw.bmob.Subscribe;
import com.woaiqw.bmob.Tag;
import com.woaiqw.bmob.thread.ThreadMode;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = findViewById(R.id.tv_name_2);


        Bmob.get().register(this, Constants.CHANGE_TEXT, new IReceiver() {
            @Override
            public void accept(Bundle bundle) {
                String s = bundle.getString("Home");
                Log.e(TAG, s);
                tv.setText(s);
            }
        });

        startActivity(new Intent(this, HomeActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bmob.get().unregister(this);
    }

    @Subscribe(thread = ThreadMode.BACKGROUND, tags = {@Tag(value = BusConstants.CHANGE_TEXT)})
    public void changeText() {

    }

}
