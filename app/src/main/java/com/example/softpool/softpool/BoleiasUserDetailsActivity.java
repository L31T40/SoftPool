package com.example.softpool.softpool;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class BoleiasUserDetailsActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String url = "http://madscout.ddns.net/consumingJson/index.php/cimovie/get/";
    private int id_ = 0;
    String name;
    String description;

    TextView txtid;
    TextView txtnome;
    TextView txtdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boleias_activas_detail_activity);

        Intent intent = getIntent();
        id_ = intent.getIntExtra("key",0);
        url = url + id_;

      //txtid =  findViewById(R.id.textView_id);
      //txtnome = findViewById(R.id.textView_name);
      //txtdesc =  findViewById(R.id.textView_desc);

        txtid.setText("asdasd");

        new GetDetail().execute();
    }



    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray movies = new JSONArray(jsonStr);

                    //Loop para o filme em expecifico
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);

                        name = c.getString("nome");
                        description = c.getString("descricao");


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            txtid.setText( String.valueOf(id_));
            txtdesc.setText(String.valueOf(description));
            txtnome.setText(String.valueOf(name));

        }

    }


}
