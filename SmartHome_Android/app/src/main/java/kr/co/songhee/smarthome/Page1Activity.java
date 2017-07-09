package kr.co.songhee.smarthome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
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
 *  냉장고
 */

public class Page1Activity  extends AppCompatActivity {
    WebView webView;
    ImageView imageView;
    TextView textView;
    TextView textView12;
    TextView textView122;


    int nununu;

    TextView textView_milk;
    TextView textView_water;
    TextView textView_choco;
    TextView textView_carrot;

    TextView textView_ham;
    TextView textView_coke;
    TextView textView_egg;



    //Button aaaaa;

    ImageButton milk, water, bbaebbaero, salad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page1);

        milk = (ImageButton)findViewById(R.id.milk);
        water = (ImageButton)findViewById(R.id.water);
        bbaebbaero = (ImageButton)findViewById(R.id.chocolate);
        salad = (ImageButton)findViewById(R.id.carrot);
        webView = (WebView)findViewById(R.id.webView2);

        textView12 = (TextView)findViewById(R.id.imageView9);
        textView122 = (TextView) findViewById(R.id.textView9);
        textView_milk = (TextView)findViewById(R.id.textView_milk);
        textView_water = (TextView)findViewById(R.id.textView_water);
        textView_carrot = (TextView)findViewById(R.id.textView_carrot);
        textView_choco = (TextView)findViewById(R.id.textView_choco);
        textView_coke = (TextView)findViewById(R.id.textView_coke);
        textView_egg = (TextView)findViewById(R.id.textView_egg);
        textView_ham = (TextView)findViewById(R.id.textView_ham);


        webView.setWebViewClient(new WebViewClient());

        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        webView.loadUrl("http://172.20.10.12:7500/img");
       // webView.loadUrl("http://192.168.43.29:7500/img");

       // new NetworkThread("img_state","on").start(); // 냉장고 내부 사진

        new NetworkThread("in_check","on").start();
        new NetworkThread("data_state", "on").start();

    }


    public void onButtonClicked(View v){    // 새로고침
        webView.loadUrl("http://172.20.10.12:7500/img");

       // webView.loadUrl("http://192.168.43.29:7500/img");

        new NetworkThread("data_state","on").start();   //냉장고 날짜 최신화
        /* 밑에 소스 말고 갱신하는 소스 추가 */
        new NetworkThread("in_check","on").start();

    }

    public void onMilkClicked(View v){
        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ssg.com/item/itemView.ssg?itemId=0000006615474&siteNo=6001&salestrNo=2034&tlidSrchWd=%EC%84%9C%EC%9A%B8%EC%9A%B0%EC%9C%A0&srchPgNo=1"));
        startActivity(intent1);
    }
    public void onWaterClicked(View v){
        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ssg.com/item/itemView.ssg?itemId=0000008978637&siteNo=6001&salestrNo=2034&tlidSrchWd=%EC%82%BC%EB%8B%A4%EC%88%98&srchPgNo=1"));
        startActivity(intent2);
    }
    public void onChocolateClicked(View v){
        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ssg.com/item/itemView.ssg?itemId=0000004232179&siteNo=6004&salestrNo=6005&tlidSrchWd=%ED%97%88%EC%89%AC&srchPgNo=2&src_area=ssglist"));
        startActivity(intent3);
    }
    public void onCarrotClicked(View v){
        Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ssg.com/item/itemView.ssg?itemId=1000005699299&siteNo=6001&salestrNo=6005&tlidSrchWd=%EB%8B%B9%EA%B7%BC&srchPgNo=1"));
        startActivity(intent4);
    }
    public void onHamClicked(View v){
        Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ssg.com/item/itemView.ssg?itemId=1000016913730&siteNo=6001&salestrNo=6005&tlidSrchWd=%EC%BC%84%ED%84%B0%ED%82%A4%ED%9B%84%EB%9E%91%ED%81%AC&srchPgNo=1"));
        startActivity(intent5);
    }
    public void onCokeClicked(View v){
        Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ssg.com/item/itemView.ssg?itemId=0000007707091&siteNo=6001&salestrNo=2034&tlidSrchWd=%EC%BD%94%EC%B9%B4%EC%BD%9C%EB%9D%BC&srchPgNo=1"));
        startActivity(intent6);
    }
    public void onEggClicked(View v){
        Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ssg.com/item/itemView.ssg?itemId=0000007150042&siteNo=6001&salestrNo=2034&tlidSrchWd=%EA%B3%84%EB%9E%80&srchPgNo=1"));
        startActivity(intent7);
    }

    class NetworkThread extends Thread {
        String name = "";
        String state = "";
        boolean stopped = false;
        int count;


        public NetworkThread(String name, String state) {
            this.name = name;
            this.state = state;
        }
        int count_plus()
        {
            this.count = this.count + 1;
            return count;
        }

        int putCount()
        {
            return count;
        }

        String putName()
        {
            return this.name;
        }

        String putState()
        {
            return this.state;
        }

        @Override
        public void run() {
            while (!stopped) {
                try {
                 //   String urlStr = "http://192.168.200.127:7500/";
                    //String urlStr = "http://172.20.10.9:7500/";
                    String urlStr = "http://192.168.43.29:7500/";
                   // String urlStr = "http://172.20.10.12:7500/";
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

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                   //         Toast.makeText(getApplication(), "+" + myResult, Toast.LENGTH_SHORT).show();

                        }
                    });



                    if(putName() == "data_state") {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView12.setText(" "+myResult);
                            }
                        });
                    }


                        //String food_name = "";

                        handler.post(new Runnable() {



                            @Override
                            public void run() {

                                int number_ = 0;

                                String water_num ="0";
                                String milk_num = "0";
                                String choco_num = "0";
                                String carrot_num = "0";
                                String ham_num = "0";
                                String coke_num = "0";
                                String egg_num = "0";

                                final String x_y1[] = myResult.split(",");

                                for(int j = 0; j < x_y1.length; j = j+2){

                                    if(x_y1[j].equals("water"))
                                        water_num = x_y1[j+1];
                                    else if(x_y1[j].equals("milk"))
                                        milk_num = x_y1[j+1];
                                    else if(x_y1[j].equals("chocolate"))
                                        choco_num = x_y1[j+1];
                                    else if(x_y1[j].equals( "carrot"))
                                        carrot_num = x_y1[j+1];
                                    else if(x_y1[j].equals("egg"))
                                        egg_num = x_y1[j+1];
                                    else if(x_y1[j].equals("coke"))
                                        coke_num = x_y1[j+1];
                                    else if(x_y1[j].equals( "ham"))
                                        ham_num = x_y1[j+1];
                                }

                    //            Toast.makeText(getApplicationContext(), "1number : "+x_y1.length, Toast.LENGTH_SHORT).show();

//                                Toast.makeText(getApplicationContext(), "grape : "+grape_num+ ", "+ milk_num+", "+ juice_num+", "+Kimchi_num, Toast.LENGTH_SHORT).show();

                                textView_milk.setText(milk_num);
                                textView_water.setText(water_num);
                                textView_choco.setText(choco_num);
                                textView_carrot.setText(carrot_num);

                                textView_egg.setText(egg_num);
                                textView_ham.setText(ham_num);
                                textView_coke.setText(coke_num);
                                //Toast.makeText(getApplicationContext(), x_y1[0]+x_y1[1]+x_y1[2]+x_y1[3]+x_y1[4]+x_y1[5]+
                                  //      x_y1[6]+x_y1[7], Toast.LENGTH_SHORT).show();

                            }
                        });
/*
                        for(int j = 0; j < number_ ; j++) {
                            String yy[]= x_y1[j].split(",");
                            if(yy[0] == "grape")
                                grape_num = yy[1];
                            else if(yy[0] == "milk")
                                milk_num = yy[1];
                            else if(yy[0] == "juice")
                                juice_num = yy[1];
                            else if(yy[0] == "Kimchi")
                                Kimchi_num = yy[1];

                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                textView_milk.setText(""+milk_num);
                                textView_choco.setText(""+grape_num);
                                textView_carrot.setText(""+juice_num);
                                textView_water.setText(""+Kimchi_num);

                            }
                        });

*/
                    handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getApplication(), putName(), Toast.LENGTH_SHORT).show();


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