package com.smuexample.img_management;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TalkActivity extends AppCompatActivity {

    ListView listView;
    TalkAdapter talkAdapter;

    ArrayList<TalkItem> talkItems= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        //데이터를 서버에서 읽어오기
        loadDB();

        listView= findViewById(R.id.listView);
        talkAdapter= new TalkAdapter(getLayoutInflater(),talkItems);
        listView.setAdapter(talkAdapter);

    }

    void loadDB(){
        //volley library로 사용 가능
        //이 예제에서는 전통적 기법으로 함.
        new Thread(){
            @Override
            public void run() {

                String serverUri="http://umul.dothome.co.kr/Android/loadDB.php";

                try {
                    URL url= new URL(serverUri);

                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    //connection.setDoOutput(true);// 이 예제는 필요 없다.
                    connection.setUseCaches(false);

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr= new InputStreamReader(is);
                    BufferedReader reader= new BufferedReader(isr);

                    final StringBuffer buffer= new StringBuffer();
                    String line= reader.readLine();
                    while (line!=null){
                        buffer.append(line+"\n");
                        line= reader.readLine();
                    }

                    //읽어오는 작업이 성공 했는지 확인
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(TalkActivity.this).setMessage(buffer.toString()).create().show();
                        }
                    });

                } catch (MalformedURLException e) { e.printStackTrace(); } catch (IOException e) {e.printStackTrace();}
            }
        }.start();
    }//loadDB() ..
}

