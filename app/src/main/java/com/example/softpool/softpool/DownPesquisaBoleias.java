package com.example.softpool.softpool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.String;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DownPesquisaBoleias extends AsyncTask<Void, Void, String> {
    private ProgressDialog pDialog;
    private String mParam1;

    public Activity aa;
    String data ="";


    String PartidaPes=  "";
    String ChegadaPes=  "";
    String _tipohora=  "";
    String _tipodata=  "";
    String _datafiltro=  "";
    String _horafiltro=  "";




    public Activity ma;
  //  public BoleiasPesquisaListaFragment ma;

    public DownPesquisaBoleias(Activity ma_) {
      ma = ma_;
  } //recebe activity por parametro

    public Utils utils=new Utils(ma);
    public RecyclerView recyclerview;
    private BoleiasPesquisaRecyclerView adapter;
    public List<infoBoleias> listaBoleias=new ArrayList<>();


    // URL to get contacts JSON
    //http://193.137.7.33/~estgv16287/index.php/getjson/PesquisaBoleiasLocais/2/3
    private static String url = "http://193.137.7.33/~estgv16287/index.php/getjson/PesquisaBoleiasLocais/";
   // private static String urlData = "http://193.137.7.33/~estgv16287/index.php/getjson/PesquisaBoleiasLocaisData/";


    public ArrayList<HashMap<String, String>> listaprs=new ArrayList<>();

    public String pasString;
    ResponseListener listener;

    public void setOnResponseListener(ResponseListener listener) {
        this.listener = listener;
    }


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
    protected String doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        String response = null;
        String target ="";
        String jsonStr="";


        String IDutilizador=SharedPref.readStr(SharedPref.KEY_USER, null);
        int count;

      final String urlfinal;
      if(_datafiltro!=null){
          urlfinal=url+IDutilizador+"/"+PartidaPes+"/"+ChegadaPes+"/"+_datafiltro+"/"+_tipodata+"/"+_horafiltro+"/"+_tipohora;
      }
        else{
           urlfinal=url+IDutilizador+"/"+PartidaPes+"/"+ChegadaPes;
      }
        jsonStr = sh.makeServiceCall(urlfinal);


        /** para debug String jsonStr = sh.makeServiceCall(url+"Porto/Lisboa/20180529");**/

        Log.e(TAG, "Resposta do URL" + jsonStr);


/**
*
* Insere informação das boleias onde o utilizador esta inserido

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
                    String img = pr.getString("FOTO");

                    if (img.compareTo("")!=0) {
                        try {
                            URL url = utils.stringToURL("http://193.137.7.33/~estgv16287/assets/fotos/" + img);
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
                            String aaa = e.getMessage();
                        }
                    }
                    HashMap<String, String> boleiapesq = new HashMap();

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
                    iBoleias.setDtachegada(dataChegada);
                    iBoleias.setHorapartida(horaPartida);
                    iBoleias.setHorachegada(horaChegada);
                    iBoleias.setLugaresdisponiveis(String.valueOf(lugaresdisponiveis));
                    iBoleias.setObjetivo(objectivo_);
                    iBoleias.setImgestado(Integer.toString(utils.estadoIcons[Integer.parseInt(estado)]));
                    iBoleias.setEstado(utils.estadoString[Integer.parseInt(estado)]);
                    iBoleias.setImgObjetivo(Integer.toString(utils.estadoObj[Integer.parseInt(objectivo)]));
                    iBoleias.setNome(String.valueOf(nome));
                    iBoleias.setImg(target);

                    listaBoleias.add(iBoleias);

                    target ="";


                }
            } catch (final JSONException e) {
                 Log.e(TAG, "Json parsing error: " + e.getMessage());
                /*runOnUiThread(new Runnable() {
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

            Log.e(TAG, "Couldn't get json from server.");/*
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
        String resultadoJson=String.valueOf(listaBoleias.size());
        return resultadoJson;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pDialog.isShowing()) /**Termina o dialogo se ainda aberto*/
            pDialog.dismiss();

        if (result.equals("0")) { //se resultado igual a 0
            Utils.minhaTosta(ma,  R.drawable.cancelado, "Sem Registos para mostrar", "short", "erro").show();


        } else {
            Utils.minhaTosta(ma,  R.drawable.favoritos, "Mostrando "+result+" registos", "short", "sucesso").show();

        }


        recyclerview = ma.findViewById(R.id.RecyclerViewPesq);
        GridLayoutManager mLayoutManager = new GridLayoutManager(ma, 1);
        recyclerview.setLayoutManager(mLayoutManager);
        adapter = new BoleiasPesquisaRecyclerView(ma, listaBoleias);
        recyclerview.setAdapter(adapter);
        Utils utils=new Utils(ma);

        int dptox= utils.dpToPx(10);
        recyclerview.addItemDecoration(new  Utils.GridSpacingItemDecoration(1, dptox, true));
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        pasString=result;
        listener.onResponseReceive(pasString);
    }

}
