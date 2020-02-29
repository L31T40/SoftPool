package com.example.softpool.softpool;

import android.app.Activity;
import android.app.ProgressDialog;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import android.util.TypedValue;
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
import java.util.List;

import android.widget.Toast;


import static android.content.ContentValues.TAG;



public class DownInfoUserBoleias extends AsyncTask<String, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;



    public Activity ma;
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    //private ListView lv;
    //public BoleiasActivasUserFragment ma;


    public RecyclerView recyclerview;
    private BoleiasActivasUserRecyclerView adapter;
    public List<infoBoleias> listaBoleias=new ArrayList<>();

    Utils utils=new Utils(ma);

    // URL to get contacts JSON
    private static String url = "http://soft.allprint.pt/index.php/getjson/getuser/";
    public DownInfoUserBoleias(Activity ma_) {
        ma = ma_;
    } //recebe activity por parametro



    public ArrayList<HashMap<String, String>> listaprs=new ArrayList<>();



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
        String jsonStr = sh.makeServiceCall(url+variableString);

        Log.e(TAG, "Resposta do URL" + jsonStr);


/**
*
* Insere informação das boleias onde o utilizador esta inserido
*
* **/
        if (jsonStr != null) {
            try {
                JSONArray boleias = new JSONArray(jsonStr);
                for (int i = 0; i < boleias.length(); i++) {
                    JSONObject pr = boleias.getJSONObject(i);

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
                    String idcondutor = pr.getString("IDCONDUTOR");
                    String img = pr.getString("FOTO");

                    if (img.compareTo("")!=0) {
                        try {
                            URL url = utils.stringToURL("http://soft.allprint.pt/assets/fotos/" + img);
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
                    infoBoleias iBoleias=new infoBoleias();
                    String x1="";


                    String dataPartida=utils.stringParaData(String.valueOf(dtapartida),"yyyy-MM-dd hh:mm:ss","dd MMMM"); //devolve data no formato indicado
                    String dataChegada=utils.stringParaData(String.valueOf(dtachegada),"yyyy-MM-dd hh:mm:ss","dd MMMM"); //devolve data no formato indicado
                    String horaPartida=utils.stringParaData(String.valueOf(dtapartida),"yyyy-MM-dd hh:mm:ss","HH:mm"); //devolve data no formato indicado
                    String horaChegada=utils.stringParaData(String.valueOf(dtachegada),"yyyy-MM-dd hh:mm:ss","HH:mm"); //devolve data no formato indicado


                    String objectivo_=utils.objetivo(String.valueOf(objectivo));

                    iBoleias.setIDboleia(String.valueOf(idboleia));
                    iBoleias.setOrigem(String.valueOf(origem));
                    iBoleias.setDestino(String.valueOf(destino));
                    iBoleias.setDtapartida(dataPartida);
                    iBoleias.setHorapartida(horaPartida);
                    iBoleias.setHorachegada(horaChegada);
                    iBoleias.setLugaresdisponiveis(String.valueOf(lugaresdisponiveis));
                    iBoleias.setObjetivo(objectivo_);
                    iBoleias.setImgestado(Integer.toString(utils.estadoIcons[Integer.parseInt(estado)]));
                    iBoleias.setEstado(utils.estadoString[Integer.parseInt(estado)]);
                    iBoleias.setImgObjetivo(Integer.toString(utils.estadoObj[Integer.parseInt(objectivo)]));
                    iBoleias.setNome(String.valueOf(nome));
                    iBoleias.setIDcondutor(String.valueOf(idcondutor));
                    iBoleias.setImg(target);

                    listaBoleias.add(iBoleias);


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
    //protected void onPostExecute(Void result) {
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        /**
         * Updating parsed JSON data into ListView
         * */


        recyclerview = ma.findViewById(R.id.RecyclerViewActivas);
        GridLayoutManager mLayoutManager = new GridLayoutManager(ma, 1);
        recyclerview.setLayoutManager(mLayoutManager);
        adapter = new BoleiasActivasUserRecyclerView(ma, listaBoleias);
        recyclerview.setAdapter(adapter);
        Utils utils=new Utils(ma);

        VarGlobals g=(VarGlobals) ma.getApplication(); // variavel global para detetar se foi feita a decoração no recyclerview
        final Boolean flagdecoration=g.flagDecoration;



        if(!flagdecoration) // caso decoração feita impede que volte a faze-la reduzindo o tamanho da cardview
        {
            int dptox= utils.dpToPx(10);
            recyclerview.addItemDecoration(new  Utils.GridSpacingItemDecoration(1, dptox, true));
            g.flagDecoration = true;
        }

        recyclerview.setItemAnimator(new DefaultItemAnimator());




    }







}
