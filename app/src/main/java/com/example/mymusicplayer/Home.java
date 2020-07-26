package com.example.mymusicplayer;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Home extends AppCompatActivity {

    ImageView songs,fav,mp,rp,cp,ply;
    Button feed,rate1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        songs=(ImageView)findViewById(R.id.songicon);
        mp=(ImageView)findViewById(R.id.mostplay);
        rp=(ImageView)findViewById(R.id.recentplay);
        cp=(ImageView)findViewById(R.id.creatplay);
        ply=(ImageView)findViewById(R.id.playlistv);
        fav=(ImageView)findViewById(R.id.favicon);
        rate1=(Button)findViewById(R.id.ratebtn);
        feed=(Button)findViewById(R.id.feedbackbtn);

        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,MainActivity1.class);
                startActivity(i);
            }
        });

        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this,RatingActivity.class);
                startActivity(i);
            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,FeedbackActivity.class);
                startActivity(i);
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,FavouriteActivity.class);
                startActivity(i);

            }
        });

        mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,MostPlayActivity.class);
                startActivity(i);
            }
        });

        rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,RecentPlayActivity.class);
                startActivity(i);

            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this,CreatePlaylistHome.class);
                startActivity(i);

            }
        });
        ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,DisplayPlaylist.class);
                startActivity(i);
            }
        });

    }
}
