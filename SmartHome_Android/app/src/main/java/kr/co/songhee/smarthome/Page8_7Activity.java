package kr.co.songhee.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by user on 2016-08-29.
 */
public class Page8_7Activity extends AppCompatActivity {
    Intent intent2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page8_7);
    }
    public void onButton2Clicked(View v){
        intent2 = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent2);
    }
}