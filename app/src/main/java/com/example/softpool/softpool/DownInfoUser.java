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



public class DownInfoUser extends AsyncTask<String, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;

    public Activity aaa;
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    public ListView lv;



    public Activity ma;
    //  public BoleiasPesquisaListaFragment ma;

    public DownInfoUser(Activity ma_) { ma = ma_;  } //recebe activity por parametro
    // URL to get contacts JSON

    private static String urluser = "http://193.137.7.33/~estgv16287/index.php/getjson/getUserData/";

    int[] estadoIcons = new int[]{
            R.drawable.ic_menu_manage,
            R.drawable.completo,
            R.drawable.cancelado,
            R.drawable.progresso,
            R.drawable.espera,
            R.drawable.canceladmin};

    public ArrayList<HashMap<String, String>> listaprs;



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
    protected Void doInBackground(String... arg0) {
     HttpHandler sh = new HttpHandler();
     String target ="";


        int count;
        final String variableString = arg0[0];

        // Making a request to url and getting response

        String jsonStrUser = sh.makeServiceCall(urluser+variableString);



        Log.e(TAG, "Resposta do URL dados user" + jsonStrUser);


        if (jsonStrUser != null) {
            try {
                JSONArray boleiasUser = new JSONArray(jsonStrUser);
                for (int i = 0; i < boleiasUser.length(); i++) {
                    JSONObject pr = boleiasUser.getJSONObject(i);


                    String imgUser = pr.getString("FOTO");

                    if (imgUser.compareTo("")!=0) {
                        try {
                            URL url = stringToURL("http://193.137.7.33/~estgv16287/assets/fotos/" + imgUser);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            String targetFileName = imgUser;
                            int lengthOfFile = conn.getContentLength();
                            String PATH = Environment.getExternalStorageDirectory() + "/images/";

                            File folder = new File(PATH);
                            if (!folder.exists()) {
                                folder.mkdir();
                            }
                            File file = new File(folder.getAbsoluteFile(), targetFileName);
                            target = file.getAbsolutePath();
                            // download the file
                            InputStream input = new BufferedInputStream(url.openStream());

                            // Output stream
                            OutputStream output = new FileOutputStream(PATH + targetFileName);


                            byte data[] = new byte[1024];
                            long total = 0;
                            while ((count = input.read(data)) != -1) {
                                total += count;
                                output.write(data, 0, count);
                            }
                            output.flush();
                            output.close();
                            input.close();
                        } catch (Exception e) {
                            String aa = e.getMessage();
                        }
                    }

/**Guardar dados do utilizador nas sharedpreferences*/

                    VarGlobals g1=(VarGlobals) ma.getApplication(); // Criar Variavel global com ID utilizador
                    g1.idFuncGlobal=pr.getString("IDUTILIZADOR");

                    SharedPref.writeStr(SharedPref.KEY_USER, pr.getString("IDUTILIZADOR"));
                    SharedPref.writeStr(SharedPref.KEY_NUMFUNC, pr.getString("NFUNCIONARIO"));
                    SharedPref.writeStr(SharedPref.KEY_LOCAL, pr.getString("NOME_CIDADE"));
                    SharedPref.writeStr(SharedPref.KEY_NAME, pr.getString("NOME"));
                    SharedPref.writeStr(SharedPref.KEY_TEL, pr.getString("TELEFONE"));
                    SharedPref.writeStr(SharedPref.KEY_EMAIL, pr.getString("E_MAIL"));
                    SharedPref.writeStr(SharedPref.KEY_FOTO, pr.getString("FOTO"));




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
