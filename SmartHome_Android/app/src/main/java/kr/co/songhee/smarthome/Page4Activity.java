package kr.co.songhee.smarthome;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-17.
 *
 *  배식가
 *
 */

public class Page4Activity  extends AppCompatActivity {

    ImageView imageView1;
    ImageView imageView2;
    Drawable text1, text2;
    Drawable text3, text4;
    ToggleButton tb1;
    ToggleButton tb2;
    boolean flag1 = false;
    boolean flag2 = false;
    ImageButton imageButton2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page4);

        imageView1 = (ImageView)findViewById(R.id.imageView28);
        imageView2 = (ImageView)findViewById(R.id.imageView29);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);

        tb1 = (ToggleButton)findViewById(R.id.imageButton8);
        tb2 = (ToggleButton)findViewById(R.id.imageButton9);

        new NetworkThread("food_check", "on").start();
    }

    public void onButtonClicked(View v){

    }

    public void onButton1Clicked(View v){
        if(flag1 == true){
            text1 = getResources().getDrawable(R.drawable.list4_text2);
            imageView1.setImageDrawable(text1);
            text4 = getResources().getDrawable(R.drawable.list_text3_color);
            imageView2.setImageDrawable(text4);
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_no));
            tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_ok_grey));
            flag1 = false;
            new NetworkThread("FoodOn", "on").start();
        }
        else {
            text2 = getResources().getDrawable(R.drawable.list4_text2_color);
            imageView1.setImageDrawable(text2);
            text3 = getResources().getDrawable(R.drawable.list4_text3);
            imageView2.setImageDrawable(text3);
            tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_ok));
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_no_grey));
            flag1 = true;
            new NetworkThread("FoodOn", "on").start();
        }

    }
    public void onButton2Clicked(View v){
        if(flag2 == true){
            text3 = getResources().getDrawable(R.drawable.list4_text3);
            imageView2.setImageDrawable(text3);
            text2 = getResources().getDrawable(R.drawable.list4_text2_color);
            imageView1.setImageDrawable(text2);
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_no_grey));
            tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_ok));
            flag2 = false;
            new NetworkThread("FoodOff", "on").start();
        }
        else{
            text4 = getResources().getDrawable(R.drawable.list_text3_color);
            imageView2.setImageDrawable(text4);
            text1 = getResources().getDrawable(R.drawable.list4_text2);
            imageView1.setImageDrawable(text1);
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_no));
            flag2 = true;
            tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_ok_grey));
            new NetworkThread("FoodOff", "on").start();
        }
    }

    class NetworkThread extends Thread {
        String name = "";
        String state = "";
        boolean stopped = false;

        public NetworkThread(String name, String state) {
            this.name = name;
            this.state = state;
        }

        public String putName() {
            return this.name;

        }

        @Override
        public void run() {
            while (!stopped) {
                try {

                  // String urlStr = "http://172.20.10.9:7500/";
                    String urlStr = "http://192.168.43.29:7500/";

                   // String urlStr = "http://172.20.10.12:7500/";
                    //String urlStr = "http://192.168.200.127:7500/";

                    // 요청 보내기
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // POST Methcd (Post 방식을 사용)
                    conn.setRequestMethod("POST");

                    // Body, URLEncoded
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(name).append("=").append(URLEncoder.encode(state, "UTF-8"));
                    buffer.append("&");

                    String bodyStr = buffer.toString();


                    // 요청 메세지 헤더
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(bodyStr.length()));
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(bodyStr.getBytes("UTF-8"));
                    os.flush();
                    os.close();


                /* 데이터 받아오는 방법 */
                    String Inbuffer = null;
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder builder = new StringBuilder();

                    while ((Inbuffer = in.readLine()) != null) {
                        builder.append(Inbuffer);

                    }

                    final String myResult = builder.toString();

                    if(myResult.equals("1")){

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                text3 = getResources().getDrawable(R.drawable.list4_text3);
                                imageView2.setImageDrawable(text3);
                                text2 = getResources().getDrawable(R.drawable.list4_text2_color);
                                imageView1.setImageDrawable(text2);
                                tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_no_grey));
                                tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_ok));
                                //Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else if(myResult.equals("0")){


                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                text4 = getResources().getDrawable(R.drawable.list_text3_color);
                                imageView2.setImageDrawable(text4);
                                text1 = getResources().getDrawable(R.drawable.list4_text2);
                                imageView1.setImageDrawable(text1);
                                tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_no));

                                tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.list4_ok_grey));

                           //nToast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }



                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });


                    /////////////////////////////////////////////////////


                } catch (Exception e) {
                    //Log.e(TAG, "Excpetion", e);
                    e.printStackTrace();
                }
                stopping();

            }
        }

        public void stopping() {
            stopped = true;
        }

        public Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                //AlertDialog.Builder ab = new AlertDialog.Builder(Page2Activity.this);
            }


        };
    }
}