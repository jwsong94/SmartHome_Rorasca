package kr.co.songhee.smarthome;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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
 *  형광등 제어
 *
 *
 */
public class Page3Activity  extends AppCompatActivity {

    static int num  = -1;
    ImageView imageView1, imageView2;
    Drawable drawable1, drawable2;
    Drawable text1, text2;
    static boolean flag = false;       // 0이면 off, 1이면 on
    ToggleButton tb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page3);

        imageView1 = (ImageView) findViewById(R.id.imageView18);
        imageView2 = (ImageView) findViewById(R.id.imageView19);

        tb = (ToggleButton) findViewById(R.id.imageButton3);

        new NetworkThread("led_check", "on").start();
        /*
        if(flag == false){
            drawable1 = getResources().getDrawable(R.drawable.led_on);
            text1 = getResources().getDrawable(R.drawable.list3_text1);
            imageView1.setImageDrawable(drawable1);
            imageView2.setImageDrawable(text1);
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button1));
            flag = true;
        }
        else{
            drawable2 = getResources().getDrawable(R.drawable.led_off);
            text2 = getResources().getDrawable(R.drawable.list3_text2);
            imageView1.setImageDrawable(drawable2);
            imageView2.setImageDrawable(text2);
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button2));
            flag = false;
        }
        */


    }

    public void onButtonClicked(View v){        /* 새로고침 버튼 */

        new NetworkThread("led_check", "on").start();
     /*
        if(flag == false){
            drawable1 = getResources().getDrawable(R.drawable.led_on);
            text1 = getResources().getDrawable(R.drawable.list3_text1);
            imageView1.setImageDrawable(drawable1);
            imageView2.setImageDrawable(text1);
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button1));
            flag = true;
        }
        else{
            drawable2 = getResources().getDrawable(R.drawable.led_off);
            text2 = getResources().getDrawable(R.drawable.list3_text2);
            imageView1.setImageDrawable(drawable2);
            imageView2.setImageDrawable(text2);
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button2));
            flag = false;
        }
*/
    }

    public void onButton1Clicked(View v){       /* on/off 제어 */
        if(flag == false){
            drawable1 = getResources().getDrawable(R.drawable.led_on);
            text1 = getResources().getDrawable(R.drawable.list3_text1);
            imageView1.setImageDrawable(drawable1);
            imageView2.setImageDrawable(text1);
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button1));
            flag = true;
            new NetworkThread("led", "on").start();

        }
        else{
            drawable2 = getResources().getDrawable(R.drawable.led_off);
            text2 = getResources().getDrawable(R.drawable.list3_text2);
            imageView1.setImageDrawable(drawable2);
            imageView2.setImageDrawable(text2);
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button2));
            flag = false;
            new NetworkThread("led", "off").start();
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

        @Override
        public void run() {
            while (!stopped) {
                try {

                  //  String urlStr = "http://172.20.10.9:7500/";
                    String urlStr = "http://192.168.43.29:7500/";

                 //   String urlStr = "http://172.20.10.12:7500/";

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

                    //num = Integer.parseInt(myResult);
                    
                    if(myResult.equals("1")){

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                drawable1 = getResources().getDrawable(R.drawable.led_on);
                                text1 = getResources().getDrawable(R.drawable.list3_text1);
                                imageView1.setImageDrawable(drawable1);
                                imageView2.setImageDrawable(text1);
                                tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button1));
                                flag = true;
                            //    Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else if(myResult.equals("0")){


                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                drawable2 = getResources().getDrawable(R.drawable.led_off);
                                text2 = getResources().getDrawable(R.drawable.list3_text2);
                                imageView1.setImageDrawable(drawable2);
                                imageView2.setImageDrawable(text2);
                                tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.list3_button2));
                                flag = false;
                            //    Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }




                } catch (Exception e) {
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


