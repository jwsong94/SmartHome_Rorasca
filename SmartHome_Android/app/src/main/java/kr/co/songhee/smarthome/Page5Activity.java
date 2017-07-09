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
 */
public class Page5Activity  extends AppCompatActivity {

    int OnOff = 0;      /* 0이면 밸브가 off 상태 */
                         /* 1이면 밸브 on 상태  */

    ImageView imageView1, imageView2;
    Drawable drawable1, drawable2;
    Drawable text1, text2;
    ToggleButton imageButton3;
    String on = "true";
    String off = "false";
    String myResult="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page5);

        imageView1 = (ImageView) findViewById(R.id.imageView18);
        imageView2 = (ImageView) findViewById(R.id.imageView19);
        imageButton3 = (ToggleButton)findViewById(R.id.imageButton3);

        new NetworkThread("gas_check", "on").start();
    }

    public void onButtonClicked(View v){


        if(myResult.equals(off)){
            drawable1 = getResources().getDrawable(R.drawable.list5_image2);
            text1 = getResources().getDrawable(R.drawable.list5_text2);
            imageView1.setImageDrawable(drawable1);
            imageView2.setImageDrawable(text1);
            imageButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.list5_button2));
            OnOff = 1;
            myResult = on;
        }
        else if(myResult.equals(on)){
            drawable2 = getResources().getDrawable(R.drawable.list5_image1);
            text2 = getResources().getDrawable(R.drawable.list5_text1);
            imageView1.setImageDrawable(drawable2);
            imageView2.setImageDrawable(text2);
            imageButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.list5_button1));
            OnOff = 0;
            myResult = off;
        }


    }

    public void onButton1Clicked(View v){       /* 밸브 On,Off 버튼 */

        if(OnOff == 0) {        // 만약 현재 밸브가 off 라면 == 밸브 On 하고 싶을때
            drawable1 = getResources().getDrawable(R.drawable.list5_image2);
            text1 = getResources().getDrawable(R.drawable.list5_text2);
            imageView1.setImageDrawable(drawable1);
            imageView2.setImageDrawable(text1);
            imageButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.list5_button2));
            OnOff = 1;      // 현재 On했으니깐 상태 업데이트
            myResult = on;
            new NetworkThread("gas", "on").start();
        }
        else if(OnOff == 1) {       // 만약 현재 밸브가 on이라면 == 밸브 Off 하고 싶을 때
            drawable2 = getResources().getDrawable(R.drawable.list5_image1);
            text2 = getResources().getDrawable(R.drawable.list5_text1);
            imageView1.setImageDrawable(drawable2);
            imageView2.setImageDrawable(text2);
            imageButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.list5_button1));
            OnOff = 0;      // 현재 Off 했으니깐 상태 업데이트
            myResult = off;
            new NetworkThread("gas", "off").start();
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

                   // String urlStr = "http://172.20.10.9:7500/";
                    String urlStr = "http://192.168.43.29:7500/";

                  //  String urlStr = "http://172.20.10.12:7500/";

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

                    myResult = builder.toString();

                    if(myResult.equals("0")){

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                drawable2 = getResources().getDrawable(R.drawable.list5_image1);
                                text2 = getResources().getDrawable(R.drawable.list5_text1);
                                imageView1.setImageDrawable(drawable2);
                                imageView2.setImageDrawable(text2);
                                imageButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.list5_button1));
                                OnOff = 0;      // 현재 Off 했으니깐 상태 업데이트
                                myResult = off;
                                //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else if(myResult.equals("1")){


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                drawable1 = getResources().getDrawable(R.drawable.list5_image2);
                                text1 = getResources().getDrawable(R.drawable.list5_text2);
                                imageView1.setImageDrawable(drawable1);
                                imageView2.setImageDrawable(text1);
                                imageButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.list5_button2));
                                OnOff = 1;      // 현재 On했으니깐 상태 업데이트
                                myResult = on;

                             //Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
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
