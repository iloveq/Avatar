package com.woaiqw.abmob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.woaiqw.bmob.Bmob;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.tv_close_main_page).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("Home","change!!!");
        Bmob.get().post(this,Constants.CHANGE_TEXT,bundle);
        finish();
    }
}
