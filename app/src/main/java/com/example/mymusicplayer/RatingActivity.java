package com.example.mymusicplayer;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RatingActivity extends AppCompatActivity {

    Button btn_rate;
    EditText email_id;
    RatingBar rb_rate;
    String emailvalid;
    String rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        btn_rate=(Button)findViewById(R.id.btnrate);
        email_id=(EditText)findViewById(R.id.edemail);
        rb_rate=(RatingBar)findViewById(R.id.rbrate);


        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rb = String.valueOf(rb_rate.getRating());
                emailvalid = email_id.getText().toString().trim();
                if (email_id.getText().toString().length() <= 50) {
                    if (!email_id.getText().toString().equals("") && !rb.equals("0.0")) {
                        if (Patterns.EMAIL_ADDRESS.matcher(emailvalid).matches()) {

                            try {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                OkHttpClient client = new OkHttpClient();

                                HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "rating.php").newBuilder();
                                urlBuilder.addQueryParameter("emailid", email_id.getText().toString());
                                urlBuilder.addQueryParameter("rate", rb);


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
                                                    Toast.makeText(RatingActivity.this, "Thank you for rating us", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(RatingActivity.this, "Invalid E-mail Id!", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(RatingActivity.this, "Please enter all the fields ", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(RatingActivity.this, "Length of email-id should be not more than 50 characters", Toast.LENGTH_LONG).show();
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

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "sendmail1.php").newBuilder();
            urlBuilder.addQueryParameter("email", email_id.getText().toString());



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
