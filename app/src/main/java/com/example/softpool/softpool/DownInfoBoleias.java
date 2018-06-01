package com.example.softpool.softpool;

import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import android.util.Log;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


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



public class DownInfoBoleias extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    String data ="";

    public ListView lv;
    public RecyclerView rv;
    public BoleiasActivasUserFragment ma;

    // URL to get contacts JSON
    private static String url = "http://193.137.7.33/~estgv16287/index.php/getjson/getuser/1";
    public ArrayList<HashMap<String, String>> listaprs;


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
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        String response = null;
        String target ="";

        int count;

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);


        Log.e(TAG, "Resposta do URL" + jsonStr);

        if (jsonStr != null) {
            try {
                //JSONObject jsonObj = new JSONObject(jsonStr);
                //JSONArray prs = jsonObj.getJSONArray("produtos");
                JSONArray boleiasUser = new JSONArray(jsonStr);
                for (int i = 0; i < boleiasUser.length(); i++) {
                    JSONObject pr = boleiasUser.getJSONObject(i);

                    String idboleia = pr.getString("IDBOLEIA");
                    String idviatura = pr.getString("IDVIATURA");
                    String origem = pr.getString("ORIGEM");
                    String destino = pr.getString("DESTINO");
                    String dtapartida = pr.getString("DATA_DE_PARTIDA");
                    String dtachegada = pr.getString("DATA_DE_CHEGADA");
                    String lugaresdisponiveis = pr.getString("LUGARES_DISPONIVEIS");
                    String objectivo = pr.getString("OBJECTIVO_PESSOAL");
                    String estado = pr.getString("ESTADO");
                    String nome = pr.getString("NOME");
                    String img = pr.getString("FOTO");

                    if (img.compareTo("")!=0) {
                        try {
                            URL url = stringToURL("http://193.137.7.33/~estgv16287/assets/fotos/" + img);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            String targetFileName = img;
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
                    HashMap<String, String> boleia = new HashMap();

                    String x1="";

                    Utils utils=new Utils();
                    String dataPartida=utils.stringParaData(String.valueOf(dtapartida),"dd MMMM");
                    String dataChegada=utils.stringParaData(String.valueOf(dtachegada),"dd MMMM");
                    String objectivo_=utils.objetivo(String.valueOf(objectivo));
                    //boleia.put("idboleia", String.valueOf(idboleia));
                    //boleia.put("idviatura", String.valueOf(idviatura));
                    boleia.put("origem", String.valueOf(origem));
                    boleia.put("destino", String.valueOf(destino));
                    boleia.put("dtapartida", dataPartida);
                    boleia.put("dtachegada", dataChegada);
                    boleia.put("lugaresdisponiveis", String.valueOf(lugaresdisponiveis));
                    boleia.put("objectivo",objectivo_);
                    //boleia.put("objectivo",String.valueOf(objectivo));
                    boleia.put("estado", String.valueOf(estado));
                    boleia.put("nome", String.valueOf(nome));
                    boleia.put("img", target);
                    listaprs.add(boleia);
                    target ="";

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
        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(ma.getActivity(), listaprs,
                R.layout.boleias_activas_user_fragment,
                new String[]{"origem","destino","dtapartida","dtachegada","lugaresdisponiveis",
                        "objectivo","estado","nome","img"}, new int[]{R.id.textView_Origem2,
                 R.id.textView_Destino2, R.id.textView_DtaPartida2, R.id.textView_DtaChegada2, R.id.textView_LugaresDisp2,
                 R.id.textView_Objectivo2, R.id.textView_Estado2, R.id.textView_NomeCondutor2, R.id.imageView_Condutor2});

        /*        ListAdapter adapter = new SimpleAdapter(ma.getActivity(), listaprs,
                R.layout.boleias_activas_user_fragment,
                new String[]{"idboleia", "idviatura","origem","destino","dtapartida","dtachegada","lugaresdisponiveis",
                        "objectivo","estado","nome","img"}, new int[]{R.id.textView_IDBoleia, R.id.textView_Viatura,
                 R.id.textView_Origem,
                 R.id.textView_Destino, R.id.textView_DtaPartida, R.id.textView_DtaChegada, R.id.textView_LugaresDisp
                 ,R.id.textView_Objectivo, R.id.textView_Estado, R.id.textView_NomeCondutor, R.id.imageView_Condutor});*/


        lv.setAdapter(adapter);
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
