package com.example.mymusicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerActivity1 extends AppCompatActivity {
    Button btn_pause,btn_next,btn_previous,btn_fav,btn_share;
    TextView songTextLabel;
    SeekBar songSeekbar;
    static MediaPlayer myMediaPlayer;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player1);
        btn_pause=(Button)findViewById(R.id.pause);
        btn_fav=(Button)findViewById(R.id.fav);
        btn_share=(Button)findViewById(R.id.sharesong);
        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        songSeekbar=(SeekBar)findViewById(R.id.seekBar);
        songTextLabel=(TextView)findViewById(R.id.songLabel);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname=mySongs.get(position).getName().toString();
                String sharePath = Environment.getExternalStorageDirectory().getPath()+"/UCDownloads/"+sname;
                Uri uri = Uri.parse(sharePath);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Sound File"));

            }
        });

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname=mySongs.get(position).getName().toString();


                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"del.php").newBuilder();
                    urlBuilder.addQueryParameter("mname",sname);



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
                                        Toast.makeText(PlayerActivity1.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                                        myMediaPlayer.stop();

                                        Intent intent=new Intent(PlayerActivity1.this,FavouriteActivity.class);
                                        startActivity(intent);
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
        });

        updateseekBar=new Thread(){
            @Override
            public void run() {
                int totalDuration=myMediaPlayer.getDuration();

                int currentPosition=0;

                while (currentPosition<totalDuration)
                {
                    try {

                        sleep(500);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);


                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };
        if (myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        final Intent i=getIntent();
        Bundle bundle=i.getExtras();


        mySongs=(ArrayList)bundle.getParcelableArrayList("songs");


        String songName=i.getStringExtra("songname");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position=bundle.getInt("pos",0);
        sname=mySongs.get(position).getName().toString();
        Uri u=Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/UCDownloads/"+sname);
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());
        updateseekBar.start();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"play.php").newBuilder();
            urlBuilder.addQueryParameter("mname",sname);



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

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname=mySongs.get(position).getName().toString();
                songSeekbar.setMax(myMediaPlayer.getDuration());
                if (myMediaPlayer.isPlaying())
                {

                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }
                else
                {

                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                btn_pause.setBackgroundResource(R.drawable.icon_pause);
                position=((position+1)%mySongs.size());
                sname=mySongs.get(position).getName().toString();
                Uri u=Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/UCDownloads/"+sname);

                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                songTextLabel.setText(sname);
                myMediaPlayer.start();


            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                btn_pause.setBackgroundResource(R.drawable.icon_pause);
                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                sname=mySongs.get(position).getName().toString();
                Uri u=Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/UCDownloads/"+sname);

                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                songTextLabel.setText(sname);
                myMediaPlayer.start();


            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
