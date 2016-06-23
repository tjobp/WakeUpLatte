package com.tjob.wakeuplatte;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    private String piURL = "http://192.168.0.12:1337/brewcoffeeTRUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBrewCoffee = (Button) findViewById(R.id.buttonBrewCoffee);
        buttonBrewCoffee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //When button is clicked log that button is click
                Log.d("buttonBrewCoffee", "onClick: ");
                brewCoffee();
            }
        });
    }

    private void brewCoffee() {
        new PostClass(this).execute();
    }

    private class PostClass extends AsyncTask<String, Void, Void>{
        private final Context context;

        public PostClass(Context c){
            this.context = c;
        }

        @Override
        protected Void doInBackground(String... params){
            HttpURLConnection connection = null;
            try{

                //set up and open connection to pi server
                URL url = new URL(piURL);
                connection = (HttpURLConnection)url.openConnection();

                Log.d("at post", "doinbackground: ");
                //post
                String urlParameters="brewcoffeeTRUE";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true); //send output a request body
                OutputStream outputPost = new BufferedOutputStream(connection.getOutputStream());
                outputPost.write(urlParameters.getBytes());
                outputPost.flush();


            }catch(MalformedURLException e){
                Log.e("log_tag", "Error in URL" + e.toString());
            }catch(IOException e){
                Log.e("log_tag", "IO error" + e.toString());
            }finally {
                if(connection != null) // Make sure the connection is not null.
                    connection.disconnect();
            }
            return null;
        }
    }
}
