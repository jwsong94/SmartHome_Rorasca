package kr.co.songhee.smarthome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-17.
 *
 *  우리집 온습도 확인 page
 *
 */
public class Page2Activity  extends AppCompatActivity {


    TextView textView1;
    TextView textView2;
    TextView textView7;
    TextView textView1010;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page2);

        textView1 = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView1010 = (TextView)findViewById(R.id.textView10);
        new NetworkThread("temp", "Temperature Check").start();       /* 꼭 주석 제거해라 */
    }


    public void refresh(View v){        /* 새로고침 버튼 */
        new NetworkThread("temp", "Temperature Check").start();
    }

    public void Temp_up(View v){
        new NetworkThread1("TempUp", "on").start();
    }

    public void Temp_down(View v){
        new NetworkThread1("TempDown", "on").start();
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
                    String urlStr = "http://192.168.43.29:7500/";

                 //   String urlStr = "http://172.20.10.12:7500/";
                   // String urlStr = "http://172.20.10.9:7500/";
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

                    /* nodejs에서 "온도/습도" (ex : 33/55) 이런식으로 데이터를 보냄 */
                    /* 따라서 split을 통해 '/' 으로 온도와 습도를 구분*/
                    /* 그러므로 temperature[0]은 온도, temperature[1]은 습도 */
/*

                    if(putName().equals("TempUp"))
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView7.setText(myResult);
                            }
                        });


                    }
*/

                    if(putName().equals("temp")){

                        final String temperature[] = myResult.split("/");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                  //              Toast.makeText(getApplicationContext(), putName(), Toast.LENGTH_SHORT).show();
                                textView1.setText(temperature[0] + "°C");     /* 바로 textview의 값을 업데이트 */
                                textView2.setText(temperature[1] + "%");

                            }
                        });
                    }
/*
                    else{

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"+"+ myResult, Toast.LENGTH_SHORT).show();
                    //            textView1010.setText("");
                                textView7.setText(myResult);

                            }
                        });
                    }
*/
/*
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), putName(), Toast.LENGTH_SHORT).show();
                            if (putName().equals("temp")) {

                            } else if (putName().equals("TempUp")) {
                                Toast.makeText(getApplicationContext(), putName(), Toast.LENGTH_SHORT).show();
                                textView7.setText(myResult);
                            }
                        }
                    });
                    */


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



    class NetworkThread1 extends Thread {
        String name = "";
        String state = "";
        boolean stopped = false;

        public NetworkThread1(String name, String state) {
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

                   // String urlStr = "http://172.20.10.12:7500/";
                    String urlStr = "http://192.168.43.29:7500/";
                   // String urlStr = "http://192.168.200.127:7500/";
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

                    /* nodejs에서 "온도/습도" (ex : 33/55) 이런식으로 데이터를 보냄 */
                    /* 따라서 split을 통해 '/' 으로 온도와 습도를 구분*/
                    /* 그러므로 temperature[0]은 온도, temperature[1]은 습도 */

                    handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView7.setText(myResult);

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