package com.example.mymusicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreatePlaylistHome extends AppCompatActivity {

    Button n,ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist_home);
        n=(Button)findViewById(R.id.newp);
        ex=(Button)findViewById(R.id.existp);

        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CreatePlaylistHome.this,NewPlaylist.class);
                startActivity(i);
            }
        });

        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CreatePlaylistHome.this,ExistingPlaylist.class);
                startActivity(i);

            }
        });
    }
}
