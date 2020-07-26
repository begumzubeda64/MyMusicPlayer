package com.example.mymusicplayer;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedbackActivity extends AppCompatActivity {

    EditText ed_email,ed_feed;
    Button btn;
    String emailvalid;
    //String emailPattern;
    //boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ed_email=(EditText)findViewById(R.id.edemail);
        ed_feed=(EditText)findViewById(R.id.feedmain);
        btn=(Button)findViewById(R.id.btnfeed);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailvalid = ed_email.getText().toString().trim();

                if (ed_email.getText().toString().length() <= 50 && ed_feed.getText().toString().length() <= 300) {

                    if (!ed_email.getText().toString().equals("") && !ed_feed.getText().toString().equals("")) {
                        if (Patterns.EMAIL_ADDRESS.matcher(emailvalid).matches()) {
                            try {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                OkHttpClient client = new OkHttpClient();

                                HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "feedback.php").newBuilder();
                                urlBuilder.addQueryParameter("emailid", ed_email.getText().toString());
                                urlBuilder.addQueryParameter("feed", ed_feed.getText().toString());


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
                                                    Toast.makeText(FeedbackActivity.this, "Thank you for your Feedback !", Toast.LENGTH_LONG).show();
                                                    sendMail();
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
                        } else {
                            Toast.makeText(FeedbackActivity.this, "Invalid E-mail Id!", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(FeedbackActivity.this, "Length of email-id should be not more than 50 characters and feedback should not be more than 300 characters", Toast.LENGTH_LONG).show();
                }
            }



        });

    }
    void sendMail()
    {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "sendmail.php").newBuilder();
            urlBuilder.addQueryParameter("email", ed_email.getText().toString());



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
    }
}
