package kr.co.songhee.smarthome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-30.
 */
public class Page7_1Activity  extends AppCompatActivity {
    EditText editText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page7_2);
        editText = (EditText) findViewById(R.id.textView8);

    }

    public void name_resist(View v)
    {
        String input_words;
        input_words= editText.getText().toString();
        new NetworkThread("name_resist",input_words).start();
        Toast.makeText(getApplicationContext(),input_words+" 등록 되었습니다.", Toast.LENGTH_SHORT).show();


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
                   // String urlStr = "http://172.20.10.12:7500/";

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

                    /* nodejs에서 "온도/습도" (ex : 33/55) 이런식으로 데이터를 보냄 */
                    /* 따라서 split을 통해 '/' 으로 온도와 습도를 구분*/
                    /* 그러므로 temperature[0]은 온도, temperature[1]은 습도 */
                    final String temperature[] = myResult.split("/");



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
