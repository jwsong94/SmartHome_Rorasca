package kr.co.songhee.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by user on 2016-08-29.
 */
public class Page8Activity extends AppCompatActivity {
    Intent intent1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page8);
    }
    public void onButtonClicked(View v){
        intent1 = new Intent(getApplicationContext(), Page8_2Activity.class);
        startActivity(intent1);
    }
}