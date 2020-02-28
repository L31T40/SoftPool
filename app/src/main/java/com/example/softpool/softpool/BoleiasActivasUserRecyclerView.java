
  package com.example.softpool.softpool;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

  /**
     * Created by Jorge Leitão on 30/06/2018.
     **/
    public class BoleiasActivasUserRecyclerView extends RecyclerView.Adapter<BoleiasActivasUserRecyclerView.ViewHolder> {


      public Activity ma;
      private LayoutInflater inflater;
      private  View view;

      public infoBoleias iBoleias = new infoBoleias();
      DownInfoUserBoleias dpBoleias = new DownInfoUserBoleias(ma);

      public List<infoBoleias> ilBoleias;
      public static infoBoleias ilBoleiasPos=null;

      private Context mContext;


        /************************************************************
         * Constructor
         ************************************************************/
        public BoleiasActivasUserRecyclerView(Context mContext, List<infoBoleias> ilBoleias) {
            this.mContext=mContext;
            inflater= LayoutInflater.from(mContext);
            this.ilBoleias=ilBoleias;


        }


        /************************************************************
         * Adapter
         ************************************************************/
        //region Adapter
        @Override
        public int getItemCount() {
            return ilBoleias.size();
        }

        @Override
        public int getItemViewType(int position) {

            return R.layout.boleias_cardview_fragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.boleias_cardview_fragment, viewGroup, false);

            ViewHolder holder=new ViewHolder(view);
            return holder;


        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            ilBoleiasPos  = ilBoleias.get(position);

            File imgFile = new File(ilBoleiasPos.getImg()); ///caminho completo da imagem do utilizador
            if(imgFile.exists())
            {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.mImgcondutor.setImageBitmap(myBitmap);

            }


            holder.mEstado.setText(ilBoleiasPos.getEstado());
            holder.mImgestado.setImageDrawable(view.getResources().getDrawable(Integer.valueOf(ilBoleiasPos.getImgestado())));
            holder.mDestino.setText(ilBoleiasPos.getDestino());
            holder.mOrigem.setText(ilBoleiasPos.getOrigem());
            holder.mDtapartida.setText(ilBoleiasPos.getDtapartida());
            holder.mHhorachegada.setText(ilBoleiasPos.getHorachegada());
            holder.mHorapartida.setText(ilBoleiasPos.getHorapartida());
            holder.mLugares.setText(ilBoleiasPos.getLugaresdisponiveis());

            holder.mNomecondutor.setText(ilBoleiasPos.getNome());



            holder.btnDireita.setOnClickListener(new View.OnClickListener() { //Sair da boleia
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setMessage("Deseja sair da boleia?");
                    alertDialogBuilder.setPositiveButton("SIM",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    try{

                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("IDUTILIZADOR",SharedPref.readStr(SharedPref.KEY_USER, null));
                                        jsonObject.put("IDBOLEIA",ilBoleias.get(position).getIDboleia());
                                         ma = (Activity) mContext;
                                         String x2=SharedPref.readStr(SharedPref.KEY_USER, null);
                                         String x3=ilBoleias.get(position).getIDcondutor();
                                        if(!x2.equals(x3)){
                                            EnviarJSONparaBD dp = new EnviarJSONparaBD(ma); //Criar viaturas
                                            dp.jsonObjSend=jsonObject;
                                            dp.urljson= "http://193.137.7.33/~estgv16287/index.php/activas/sairboleiamobile";
                                            dp.execute();
                                            Utils.minhaTosta(mContext, R.drawable.completo, "Boleia Removida", "long", "sucesso").show();

                                            VarGlobals g1=(VarGlobals) mContext.getApplicationContext();
                                            final String idfuncglobal=g1.idFuncGlobal;
                                            dpBoleias.cancel(true);
                                            dpBoleias=new DownInfoUserBoleias(ma);
                                            dpBoleias.listaBoleias = new ArrayList<>();
                                            dpBoleias.recyclerview =  view.findViewById(R.id.RecyclerViewActivas);
                                            dpBoleias.execute(idfuncglobal);
                                        } else {
                                            Utils.minhaTosta(mContext, R.drawable.cancelado, "É Condutor, não pode Cancelar", "long", "erro").show();
                                        }


                                    }catch (JSONException ex){
                                        //TODO handle Error here
                                        Log.e(TAG, "ERRO " + ex);
                                    }


                                    }

                            });
                    alertDialogBuilder.setNegativeButton("NÃO",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            });


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment
                    Bundle bundle=new Bundle();
                    bundle.putString("_idboleia",ilBoleias.get(position).getIDboleia());
                    bundle.putString("_idcondutor",ilBoleias.get(position).getIDcondutor());

                   // Utils.minhaTosta(mContext,  R.drawable.completo, String.valueOf(ilBoleias.get(position).getNome()), "short", "sucesso").show();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new BoleiasActivasDetalhesFragment();
                    myFragment.setArguments(bundle);

                    VarGlobals g=(VarGlobals) v.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                    g.flagDecoration=false;

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, myFragment)
                            .addToBackStack("BoleiasActivasUserRecyclerView")
                            .commit();


                }
            });

            holder.btnEsquerda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment
                    Bundle bundle=new Bundle();
                    bundle.putString("_idboleia",ilBoleias.get(position).getIDboleia());

                    //Utils.minhaTosta(mContext,  R.drawable.completo, String.valueOf(ilBoleias.get(position).getNome()), "short", "sucesso").show();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    Fragment myFragment = new BoleiasDetalhesFragment();
                    myFragment.setArguments(bundle);

                    VarGlobals g=(VarGlobals) v.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                    g.flagDecoration=false;

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, myFragment)
                            .addToBackStack("BoleiasActivasUserRecyclerView")
                            .commit();


                }
            });


        }

        //endregion

        /************************************************************
         * ViewHolder
         ************************************************************/
        //region ViewHolder
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public  TextView mDestino, mOrigem,mDtapartida,mHorapartida,mHhorachegada, mLugares, mObjetivo,mEstado,mNomecondutor;
            public  ImageView mImgcondutor,mImgestado,mImgobj;
            public Button btnEsquerda, btnDireita;
            public CardView cardView;

        //    MyClickListener listener;


            //public View view;

            /************************************************************
             * Constructor
             ************************************************************/
            //region Constructor
            public ViewHolder(View view) {
                super(view);

                this.mDestino= view.findViewById(R.id.textView_Destino);
                this.mOrigem= view.findViewById(R.id.textView_Origem);
                this.mDtapartida= view.findViewById(R.id.textView_DtaPartida);
                this.mHorapartida= view.findViewById(R.id.textView_horaPartida);
                this.mHhorachegada= view.findViewById(R.id.textView_horaChegada);
                this.mLugares= view.findViewById(R.id.textView_LugaresDisp);
                this.mEstado= view.findViewById(R.id.textView_Estado);
                this.mImgestado= view.findViewById(R.id.imageView_estado);
                this.mNomecondutor= view.findViewById(R.id.textView_NomeCondutor);
                this.mImgcondutor= view.findViewById(R.id.imageView_Condutor);

                this.btnEsquerda =  view.findViewById(R.id.btnCardViewEsquerda);
                     btnEsquerda.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_details, 0, 0, 0);
                     btnEsquerda.setText("DETALHES");
                     btnEsquerda.setPadding(35,1,35,1);
                     btnEsquerda.setTextColor(Color.parseColor("#303f9f"));
                this.btnDireita =  view.findViewById(R.id.btnCardViewDireita);
                     btnDireita.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_cancel, 0);
                     btnDireita.setText("SAIR DA BOLEIA");
                     btnDireita.setPadding(35,1,35,1);
                     btnDireita.setTextColor(Color.parseColor("#303f9f"));
                this.cardView = view.findViewById(R.id.CardView);

            }

            //endregion

            /************************************************************
             * Update
             ************************************************************/
            //region Update
            public void update(int position, List<infoBoleias> s) {

            }

            //endregion

            /************************************************************
             * OnClick
             ************************************************************/
            //region OnClick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnCardDetalhe:
                     //   Log.e(TAG, "DETALHES CARDVIEW : " +  String.valueOf(ilBoleiasPos.getNome()));
                        //listener.onEdit(this.getLayoutPosition());
                        break;
                    case R.id.CardView:
                       // Log.e(TAG, "COISA e CENAS DO  CARDVIEW : " +  String.valueOf(ilBoleiasPos.getOrigem()));
                      //  listener.onEdit(this.getLayoutPosition());
                        break;
                    case R.id.btnCardCancela:
                      //  Log.e(TAG, "CANCELA CARDVIEW : " +  String.valueOf(ilBoleiasPos.getDestino()));
                       // listener.onDelete(this.getLayoutPosition());
                        break;
                    default:
                        break;
                }
            }


          /* public interface MyClickListener {
                void onEdit(int p);
                void onDelete(int p);
            }*/
            //endregion
        }


        //endregion
    }