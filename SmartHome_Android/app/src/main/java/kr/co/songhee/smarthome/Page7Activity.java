package kr.co.songhee.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-29.
 */
public class Page7Activity extends AppCompatActivity {
    Intent intent;
    WebView webView;
    TextView textView3;
    TextView textView11;


    CountDownTimer mCountDown = new CountDownTimer(0, 0) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page7);
        webView = (WebView)findViewById(R.id.webView3);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView11 = (TextView) findViewById(R.id.textView11);
        webView.setWebViewClient(new WebViewClient());

        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        webView.loadUrl("http://172.20.10.12:7500/img_bell");


    }

    public void onButtonClicked(View v){
        webView.loadUrl("http://172.20.10.12:7500/img_bell");

    }

    public void onButtonClicked2(View v) {
        intent = new Intent(getApplicationContext(), Page7_1Activity.class);
        startActivity(intent);
    }

    public void BellOn(View v){
        mCountDown = new CountDownTimer(3000000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                new NetworkThread("bell1","on").start();

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }




    public void BellOff(View v){
        mCountDown.cancel();
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

                    //String urlStr = "http://172.20.10.9:7500/";
                    //String urlStr = "http://192.168.43.29:7500/";
                  //  String urlStr = "http://172.20.10.12:7500/";

                    String urlStr = "http://192.168.43.29:7500/";

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


                    final String x_y[] = myResult.split("/");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView11.setText(""+myResult);
                            Toast.makeText(getApplicationContext(), myResult, Toast.LENGTH_SHORT).show();

                        }
                    });


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


