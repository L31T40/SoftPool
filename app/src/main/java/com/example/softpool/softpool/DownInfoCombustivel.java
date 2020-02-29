package com.example.softpool.softpool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class DownInfoCombustivel extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;

    String data ="";


    public Activity ma;

    ArrayList<String> listaprs = new ArrayList<>();



    // URL to get contacts JSON

    private static String url = "http://soft.allprint.pt/index.php/getjson/getcombustivel";

    public DownInfoCombustivel(Activity ma_) {
        ma = ma_;
    } //recebe activity por parametro




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



        String jsonStrcomb = sh.makeServiceCall(url);

        Log.e(TAG, "Resposta do URL dados user" + jsonStrcomb);

        if (jsonStrcomb != null) {
            try {

                JSONArray combustiveis = new JSONArray(jsonStrcomb);
                for (int i = 0; i < combustiveis.length(); i++) {
                    JSONObject pr = combustiveis.getJSONObject(i);

                    String nomecomb = pr.getString("COMBUSTIVEL");
                    HashMap<String, String> combustivel = new HashMap();

                    combustivel.put("nomecomb", String.valueOf(nomecomb));
                    listaprs.add(nomecomb);

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
