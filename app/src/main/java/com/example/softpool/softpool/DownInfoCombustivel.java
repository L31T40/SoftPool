package com.example.softpool.softpool;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;



public class DownInfoCombustivel extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;



    public String _iduser=  "";
    public Boolean flag=false;

    String data ="";
    String dataParsed = "";
    String singleParsed ="";






    public Activity ma;
    //  public BoleiasPesquisaListaFragment ma;

    public DownInfoCombustivel(Activity ma_) { ma = ma_;  } //recebe activity por parametro
    // URL to get contacts JSON

    private static String url = "http://193.137.7.33/~estgv16287/index.php/getjson/getcombustivel";


    //public ArrayList<HashMap<String, String>> listaprs;
    public ArrayList<String> listaprscomb;



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog

        pDialog = new ProgressDialog(ma);
        pDialog.setMessage("Aguarde...");
        pDialog.setCancelable(false);
        pDialog.show();

    }




    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        String target ="";


        int count;


        String jsonStrcomb = sh.makeServiceCall(url);

        Log.e(TAG, "Resposta do URL dados Combustivel" + jsonStrcomb);


        if (jsonStrcomb != null) {
            try {
                JSONArray combustivel = new JSONArray(jsonStrcomb);
                for (int i = 0; i < combustivel.length(); i++) {
                    JSONObject pr = combustivel.getJSONObject(i);
                    String nomecomb = pr.getString("COMBUSTIVEL");

                    listaprscomb.add(nomecomb);

                }
            } catch (final JSONException e) {
                /* Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getApplicationContext(),
                         //       "Json parsing error: " + e.getMessage(),
                           //     Toast.LENGTH_LONG)
                            //    .show();
                    }
                });
*/
            }
        } else {
           /* Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
*/
        }



        Log.e(TAG, "COMBUSTIVEL: " + listaprscomb);
        return null;
    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

        flag=true;

        Log.e("FLAG COMBUSTIVEL POST: ",String.valueOf(flag));

    }



}
