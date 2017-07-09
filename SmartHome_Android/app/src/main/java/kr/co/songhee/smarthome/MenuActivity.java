package kr.co.songhee.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by user on 2016-08-17.
 */
public class MenuActivity extends AppCompatActivity {

    Intent intent1, intent2, intent3, intent4, intent5, intent6, intent7, intent8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
    }

    public void onButton1Clicked(View v){
       intent1 = new Intent(getApplicationContext(), Page1Activity.class);
        startActivity(intent1);
    }

    public void onButton2Clicked(View v){
        intent2 = new Intent(getApplicationContext(), Page2Activity.class);
        startActivity(intent2);
    }

    public void onButton3Clicked(View v){
        intent3 = new Intent(getApplicationContext(), Page3Activity.class);
        startActivity(intent3);
    }

    public void onButton4Clicked(View v){
        intent4 = new Intent(getApplicationContext(), Page4Activity.class);
        startActivity(intent4);
    }

    public void onButton5Clicked(View v){
        intent5 = new Intent(getApplicationContext(), Page5Activity.class);
        startActivity(intent5);
    }

    public void onButton6Clicked(View v){
        intent6 = new Intent(getApplicationContext(), Page6Activity.class);
        startActivity(intent6);
    }

    public void onButton7Clicked(View v){
        intent7 = new Intent(getApplicationContext(), Page7Activity.class);
        startActivity(intent7);
    }

    public void onButton8Clicked(View v){
        intent8 = new Intent(getApplicationContext(), Page8Activity.class);
        startActivity(intent8);
    }
}