package com.example.softpool.softpool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class DownInfoViaturas extends AsyncTask<String, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;


    String data ="";

    public BoleiasActivasUserFragment ma;

    public ArrayList<HashMap<String, String>> listaprs;

    // URL to get contacts JSON

    private static String urlviaturasuser = "http://193.137.7.33/~estgv16287/index.php/getjson/pesquisaviaturasbyuserid/";


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog

        pDialog = new ProgressDialog(ma.getActivity());
        pDialog.setMessage("Aguarde...");
        pDialog.setCancelable(false);
        pDialog.show();

    }




    @Override
    protected Void doInBackground(String... arg0) {
        HttpHandler sh = new HttpHandler();
        String response = null;

        final String variableString = arg0[0]; // numero de funcionario
        final String tipo = arg0[1];// passagem por argumento do tipo de informação a fazer o download



        // Making a request to url and getting response

        String jsonStrviaturas = sh.makeServiceCall(urlviaturasuser+variableString);

        Log.e(TAG, "Resposta do URL dados VIATURA user" + jsonStrviaturas);

        if (jsonStrviaturas != null) {
            try {
                JSONArray viaturas = new JSONArray(jsonStrviaturas);
                for (int i = 0; i < viaturas.length(); i++) {
                    JSONObject pr = viaturas.getJSONObject(i);

                    HashMap<String, String> viatura = new HashMap();
                    if (tipo=="1") { //apenas para seleccionar o tipo de informação para download
                        viatura.put("idviatura", pr.getString("IDVIATURA"));
                        viatura.put("matricula", pr.getString("MATRICULA"));
                        viatura.put("lotacao", pr.getString("LOTACAO"));
                        listaprs.add(viatura);
                    }
                    else {
                        viatura.put("idviatura", pr.getString("IDVIATURA"));
                        viatura.put("marca", pr.getString("MARCA"));
                        viatura.put("modelo", pr.getString("MODELO"));
                        viatura.put("matricula", pr.getString("MATRICULA"));
                        viatura.put("seguro", pr.getString("SEGURO_CONTRA_TODOS_OS_RISCOS"));
                        viatura.put("ativo", pr.getString("ACTIVO"));
                        viatura.put("combustivel", pr.getString("COMBUSTIVEL"));
                        viatura.put("lotacao", pr.getString("LOTACAO"));
                        listaprs.add(viatura);
                    }

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



    }




    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

}
