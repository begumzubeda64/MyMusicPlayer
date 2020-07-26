package com.example.mymusicplayer;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewPlaylist extends AppCompatActivity {

    EditText edn;
    Button cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_playlist);

        edn=(EditText)findViewById(R.id.edname);
        cr=(Button)findViewById(R.id.btncreate);

        cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edn.getText().toString().equals("")) {

                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        OkHttpClient client = new OkHttpClient();

                        HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "playlistadd.php").newBuilder();
                        urlBuilder.addQueryParameter("pname", edn.getText().toString());


                        String url = urlBuilder.build().toString();

                        Request request = new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            String s1 = response.body().string();
                                            Log.e("message", s1);
                                            Toast.makeText(NewPlaylist.this, "Playlist Created !", Toast.LENGTH_LONG).show();
                                            Intent i=new Intent(NewPlaylist.this,SongList.class);
                                            i.putExtra("songname",edn.getText().toString());
                                            startActivity(i);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }


                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(NewPlaylist.this, "Please enter all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
