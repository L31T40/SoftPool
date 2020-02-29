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


public class DownInfoLocais extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;

    public Activity ma;
    String data ="";

    public DownInfoLocais(Activity ma_) {
        ma = ma_;
    } /**recebe activity por parametro**/


   public ArrayList<HashMap<String, String>> listaprs;


    // URL to get contacts JSON

    private static String urlocais = "http://soft.allprint.pt/index.php/getjson/getLocais/";



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
        String response = null;


        String jsonStrlocais = sh.makeServiceCall(urlocais);

        Log.e(TAG, "Resposta do URL dados user" + jsonStrlocais);

        if (jsonStrlocais != null) {
            try {

                JSONArray locais = new JSONArray(jsonStrlocais);
                for (int i = 0; i < locais.length(); i++) {
                    JSONObject pr = locais.getJSONObject(i);

                    HashMap<String, String> Hashlocal = new HashMap();

                    Hashlocal.put("idlocal", pr.getString("IDLOCAL"));
                    Hashlocal.put("nomecidade", pr.getString("NOME_CIDADE"));

                    listaprs.add(Hashlocal);

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
