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



public class DownInfoUserBoleiasVerifica extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;



    public String _iduser=  "";
    public Boolean flag=false;

    String data ="";
    String dataParsed = "";
    String singleParsed ="";


    public Activity ma;
    //  public BoleiasPesquisaListaFragment ma;

    public DownInfoUserBoleiasVerifica(Activity ma_) { ma = ma_;  } //recebe activity por parametro
    // URL to get contacts JSON

    private static String urluser = "http://193.137.7.33/~estgv16287/index.php/getjson/getuser/";

    //public ArrayList<HashMap<String, String>> listaprs;
    public ArrayList<String> listaprs;



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
        VarGlobals g2=(VarGlobals) ma.getApplication(); // Buscar a string para o autoextview
        _iduser=g2.idFuncGlobal;

        // Making a request to url and getting response

        String jsonStrUser = sh.makeServiceCall(urluser+_iduser);

        Log.e(TAG, "Resposta do URL dados user" + jsonStrUser);


        if (jsonStrUser != null) {
            try {
                JSONArray boleiasUser = new JSONArray(jsonStrUser);
                for (int i = 0; i < boleiasUser.length(); i++) {
                    JSONObject pr = boleiasUser.getJSONObject(i);

                    String idboleia = pr.getString("IDBOLEIA");
                    listaprs.add(idboleia);
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




        return null;
    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

        flag=true;

        Log.e("FLaginha: ",String.valueOf(flag));

    }



}
