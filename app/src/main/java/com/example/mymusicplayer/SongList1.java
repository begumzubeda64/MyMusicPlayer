package com.example.mymusicplayer;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SongList1 extends AppCompatActivity {
    ListView myListViewForSongs;
    //String items[];
    ArrayAdapter<String> myAdapter;
    ArrayList<File> list1;
    ArrayList<File> mySongs;
    String pn;
    String items[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list1);

        list1=new ArrayList<>();
        mySongs=new ArrayList<>();
        myListViewForSongs=(ListView)findViewById(R.id.mySongListView);
        display();
    }

    public void display()
    {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"playlisdisplay.php").newBuilder();
            Intent intent=getIntent();
            pn=intent.getStringExtra("songname");
            urlBuilder.addQueryParameter("pname", pn);

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

                                JSONArray jsonArray=new JSONArray(s1);
                                items=new String[jsonArray.length()];
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    items[i]=jsonObject.getString("mname").replace(".mp3","");
                                    list1.add(new File(jsonObject.getString("mname")));
                                }


                                mySongs.addAll(list1);
                                myAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,items);
                                myListViewForSongs.setAdapter(myAdapter);

                                myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String songName=myListViewForSongs.getItemAtPosition(i).toString();
                                        Intent intent=new Intent(getApplicationContext(),PlayerActivity3.class).putExtra("songs",mySongs).putExtra("songname",songName).putExtra("pos",i).putExtra("plname",pn);
                                        startActivity(intent);

                                    }
                                });




                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.search_song);
        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
