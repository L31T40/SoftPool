package com.example.softpool.softpool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DownActivasBoleiasDetalhes extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    private String mParam1;

    public Activity aa;
    private String data ="", dataParsed = "", singleParsed ="";

    public String _idboleia=  "";

    private String idboleia,idviatura,origem,destino,dtapartida,dtachegada,lugaresdisponiveis,objectivo,imgobjectivo,estado,nomecondutor,img;
    private String dataPartida, dataChegada, horaPartida , horaChegada,lotacaoviatura, target;


    public ListView lv;

    public Activity ma;
  //  public BoleiasPesquisaListaFragment ma;

    public DownActivasBoleiasDetalhes(Activity ma_) {
      ma = ma_;
  } //recebe activity por parametro


    private BoleiasPesquisaRecyclerView adapter;
    public List<infoBoleias> Boleia=new ArrayList<>();


    // URL para receber JSON
    private static String url = "http://193.137.7.33/~estgv16287/index.php/getjson/getBoleiaByid/";
    private static String urlpassageiro = "http://193.137.7.33/~estgv16287/index.php/getjson/getPassageirosBoleia/";

    private LinearLayout mGaleria;
    private View views;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;

    public ArrayList<HashMap<String, String>> listaprs=new ArrayList<>();
    public ArrayList<HashMap<String, String>> listaprsPassageiro=new ArrayList<>();


    ResponseListener listener;


    Utils utils=new Utils(ma);

  /*  public void setOnResponseListener(ResponseListener listener) {
        this.listener = listener;
    }*/


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

        String jsonStrBoleia="";
        String jsonStrPassageiro="";

        int count;

        jsonStrBoleia = sh.makeServiceCall(url+_idboleia);
        jsonStrPassageiro = sh.makeServiceCall(urlpassageiro+_idboleia);

        Log.e(TAG, "Resposta do URL BOLEIAS" + jsonStrBoleia);
        Log.e(TAG, "Resposta do URL BOLEIAS" + jsonStrPassageiro);


/**
*
* Faz download da informação dos passageiros da boleia

* **/



        if (jsonStrPassageiro != null) {
            try {
                JSONArray boleiasUser = new JSONArray(jsonStrPassageiro);
                for (int i = 0; i < boleiasUser.length(); i++) {
                    JSONObject pr = boleiasUser.getJSONObject(i);

                    String idutilizador = pr.getString("IDUTILIZADOR");
                    String nome = pr.getString("NOME");
                    String nfuncionario = pr.getString("NFUNCIONARIO");
                    String localtrabalho = pr.getString("NOME_CIDADE");
                    String telefone = pr.getString("TELEFONE");
                    String email = pr.getString("E_MAIL");
                    String objpessoal = pr.getString("OBJECTIVO_PESSOAL");
                    String imgUser = pr.getString("FOTO");



                    if (imgUser.compareTo("")!=0) {
                        try {
                            URL url = utils.stringToURL("http://193.137.7.33/~estgv16287/assets/fotos/" + imgUser);
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

                    HashMap<String, String> boleiaPassageiro = new HashMap();
                    boleiaPassageiro.put("idutilizador",idutilizador);
                    boleiaPassageiro.put("nome", nome);
                    boleiaPassageiro.put("localtrabalho", localtrabalho);
                    boleiaPassageiro.put("telefone", telefone);
                    boleiaPassageiro.put("email", email);
                    boleiaPassageiro.put("objpessoal", objpessoal);
                    boleiaPassageiro.put("imgUser", imgUser);

                    listaprsPassageiro.add(boleiaPassageiro);



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


/**
*
* Faz download da informação das boleias onde o utilizador esta inserido

* **/

        if (jsonStrBoleia != null) {
            try {
                JSONArray boleias = new JSONArray(jsonStrBoleia);
                for (int i = 0; i < boleias.length(); i++) {
                    JSONObject pr = boleias.getJSONObject(i);

                     idboleia = pr.getString("IDBOLEIA");
                     idviatura = pr.getString("IDVIATURA");
                     origem = pr.getString("ORIGEM");
                     destino = pr.getString("DESTINO");
                     dtapartida = pr.getString("DATA_DE_PARTIDA");
                     dtachegada = pr.getString("DATA_DE_CHEGADA");
                     lugaresdisponiveis = pr.getString("LUGARES_DISPONIVEIS");
                     lotacaoviatura = pr.getString("LOTACAO");
                     objectivo = pr.getString("OBJECTIVO_PESSOAL");
                     estado = pr.getString("ESTADO");
                     nomecondutor = pr.getString("NOME_UTIL");
                     img = pr.getString("FOTO");

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

                            Log.e(TAG, "error: " + e.getMessage());
                        }
                    }



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

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (pDialog.isShowing()) /**Termina o dialogo se ainda aberto*/
            pDialog.dismiss();

        /**
         * upadte dos dados do  parsed JSON para a  RecycleView
         * */
        Log.e(TAG, "PASSAGEIROS: "+listaprsPassageiro.size());

        Utils utils=new Utils(ma);

        dataPartida=utils.stringParaData(String.valueOf(dtapartida),"yyyy-MM-dd hh:mm:ss","dd MMMM"); //devolve data no formato indicado
        dataChegada=utils.stringParaData(String.valueOf(dtachegada),"yyyy-MM-dd hh:mm:ss","dd MMMM"); //devolve data no formato indicado
        horaPartida=utils.stringParaData(String.valueOf(dtapartida),"yyyy-MM-dd hh:mm:ss","HH:mm"); //devolve data no formato indicado
        horaChegada=utils.stringParaData(String.valueOf(dtachegada),"yyyy-MM-dd hh:mm:ss","HH:mm"); //devolve data no formato indicado

        TextView nomecondutor_ =  ma.findViewById(R.id.textView_NomeCondutor); nomecondutor_.setText(nomecondutor);
        TextView dtapartida_ =  ma.findViewById(R.id.textView_DtaPartida); dtapartida_.setText(dataPartida);
        TextView origem_ =  ma.findViewById(R.id.textView_Origem); origem_.setText(origem);
        TextView destino_ =  ma.findViewById(R.id.textView_Destino); destino_.setText(destino);
        TextView horapartida_ =  ma.findViewById(R.id.textView_horaPartida); horapartida_.setText(horaPartida);
        TextView horachegada_ =  ma.findViewById(R.id.textView_horaChegada); horachegada_.setText(horaChegada);
        TextView lugaresdisponiveis_ =  ma.findViewById(R.id.textView_LugaresDisp); lugaresdisponiveis_.setText(lugaresdisponiveis);
        //ImageView objectivoimg_ =  ma.findViewById(R.id.imageView_Lugares); objectivoimg_.setImageResource(utils.estadoObj[Integer.parseInt(objectivo)]);
        TextView estado_ =  ma.findViewById(R.id.textView_Estado); estado_.setText(estado);
        ImageView estadoimg_ =  ma.findViewById(R.id.imageView_estado); estadoimg_.setImageResource(utils.estadoIcons[Integer.parseInt(estado)]);


        File imgFile = new File(target);
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = ma.findViewById(R.id.imageView_Condutor);
            imageView.setImageBitmap(myBitmap);
        }
        carregaPassageiros();



    }

private void carregaPassageiros(){ //Carrega imagens de passageiros para o fragment dos detalhes
    Bitmap myBitmap=null;
    mGaleria =  (LinearLayout) ma.findViewById(R.id.Gallery_Passageiros);
    LayoutInflater inflater=LayoutInflater.from(ma);

    for(int i=0;i<listaprsPassageiro.size();i++) {

        File imgPassageirosFile = new File(Environment.getExternalStorageDirectory()+ "/Images/"+listaprsPassageiro.get(i).get("imgUser"));
        if(imgPassageirosFile.exists())
        {
             myBitmap = BitmapFactory.decodeFile(imgPassageirosFile.getAbsolutePath());

        }

        View view = inflater.inflate(R.layout.imagens_passageiros, mGaleria, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 2, 5, 2);
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);

        TextView textView = view.findViewById(R.id.textView_NomePassageiro);
        textView.setText(listaprsPassageiro.get(i).get("nome"));
        ImageView imageView = view.findViewById(R.id.ImageView_Objetivo);
        ImageView imageView1 = view.findViewById(R.id.ImageView_Passageiro);
        imageView.setImageResource(utils.estadoIcons[i]);

        imageView.setImageResource(utils.estadoObj[Integer.parseInt(listaprsPassageiro.get(i).get("objpessoal"))]);
        imageView1.setImageBitmap(myBitmap);
        mGaleria.addView(view);
        Log.e(TAG, "Passageiro: " + i);
        }
    }




}
