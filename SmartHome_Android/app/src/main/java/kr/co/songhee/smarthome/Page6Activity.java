package kr.co.songhee.smarthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-17.
 */

public class Page6Activity extends AppCompatActivity {

    Intent intent1, intent2, intent3, intent4, intent5, intent7;
    WebView webView;
    static DrawingPointView view;
    static Canvas mCanvas;
    LinearLayout linearLayout;
    ImageButton go, turnLeft, turnRight;
    Switch mySwitch;
    static int x_ = 450;
    static int y_ = 450;

    TextView textView_x;        /* x좌표 */
    TextView textView_y;        /* y좌표 */

    CountDownTimer mCountDown = new CountDownTimer(0, 0) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
        }
    };

    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page6);

        textView_y = (TextView)findViewById(R.id.textView6);
        textView_x = (TextView)findViewById(R.id.textView4);

        go = (ImageButton) findViewById(R.id.imageButton5);
        turnLeft = (ImageButton) findViewById(R.id.imageButton6);
        turnRight = (ImageButton) findViewById(R.id.imageButton7);


        go.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:{
                        new NetworkThread("u","on").start();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        new NetworkThread("s","on").start();
                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        });

        turnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:{
                        new NetworkThread("l","on").start();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        new NetworkThread("s","on").start();
                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        });

        turnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:{
                        new NetworkThread("r","on").start();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        new NetworkThread("s","on").start();
                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        });


        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        view = new DrawingPointView(this);
        linearLayout.addView(view);


        /* 웹캠 스트리밍 영상 */
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        webView.loadUrl("http://172.20.10.3:7500/cam");

        /* 로봇 좌표*/
        textView_x.setText("00");
        textView_y.setText("00");

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void onButtonList1Clicked(View v){
        intent1 = new Intent(getApplicationContext(), Page1Activity.class);
        startActivity(intent1);
    }
    public void onButtonList2Clicked(View v){
        intent2 = new Intent(getApplicationContext(), Page2Activity.class);
        startActivity(intent2);
    }
    public void onButtonList3Clicked(View v){
        intent3 = new Intent(getApplicationContext(), Page3Activity.class);
        startActivity(intent3);
    }
    public void onButtonList4Clicked(View v){
        intent4 = new Intent(getApplicationContext(), Page4Activity.class);
        startActivity(intent4);
    }
    public void onButtonList5Clicked(View v){
        intent5 = new Intent(getApplicationContext(), Page5Activity.class);
        startActivity(intent5);
    }
    public void onButtonList7Clicked(View v){
        intent7 = new Intent(getApplicationContext(), Page7Activity.class);
        startActivity(intent7);
    }


    public void onButton(View v){

        mCountDown = new CountDownTimer(3000000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                new NetworkThread("Robot_refresh","on").start();

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void offButton(View v){
      //  new NetworkThread("Robot_refresh","on").start();
        mCountDown.cancel();
    }

    public void onButtonClicked(View v){
        new NetworkThread("Robot_refresh","on").start();
    }

    public void Reset(View v){
        new NetworkThread("Robot_reset","on").start();

    }

    public class DrawingPointView extends View {
        public DrawingPointView(Context context) {
            super(context);
        }

        public DrawingPointView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public DrawingPointView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mCanvas = canvas;
            Paint paint = new Paint();
            paint.setColor(Color.RED);

        /*  점찍기  */
            canvas.drawCircle(x_, y_, 20, paint);
            invalidate();
        }

        public void Updating(){
            invalidate();
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
                    // String urlStr = "http://172.20.10.12:7500/";
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

                            // Toast.makeText(getApplicationContext(), x_y[0] + "," + x_y[1], Toast.LENGTH_SHORT).show();
                            textView_x.setText(x_y[0]);     /* 바로 x,y 의 값을 업데이트 */
                            textView_y.setText(x_y[1]);
                            //textView_x.setText(aa);     /* 바로 x,y 의 값을 업데이트 */
                            //textView_y.setText(bb);
                        }
                    });

                    x_ = Integer.parseInt(x_y[0]);
                    y_ = Integer.parseInt(x_y[1]);
                    view.Updating();
                    //final int aa = x_;
                    //final int bb = y_;




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
