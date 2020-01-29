package com.example.softpool.softpool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;


/**
 * Created by Jorge Leitão on 30/06/2018.
 **/
public class BoleiasPesquisaRecyclerView extends RecyclerView.Adapter<BoleiasPesquisaRecyclerView.ViewHolder> {



    private Context mContext;

    public List<infoBoleias> ilBoleias;
    public static infoBoleias ilBoleiasPos=null;


    public infoBoleias iBoleias = new infoBoleias();
    private LayoutInflater inflater;

    private  View view;



    /************************************************************
     * Constructor
     ************************************************************/
    public BoleiasPesquisaRecyclerView(Context mContext, List<infoBoleias> ilBoleias) {
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("_idboleia", ilBoleias.get(position).getIDboleia());
                    //Log.e(TAG, "CARDVIEW CARDVIEW: " +  String.valueOf(ilBoleias.get(position).getOrigem()));
                    // Utils.minhaTosta(mContext,  R.drawable.completo, String.valueOf(ilBoleias.get(position).getNome()), "short", "sucesso").show();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new BoleiasDetalhesFragment();
                    myFragment.setArguments(bundle);

                    VarGlobals g = (VarGlobals) v.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                    g.flagDecoration = false;

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, myFragment)
                            .addToBackStack("BoleiasPesquisaRecyclerView")
                            .commit();
                }
        });

        holder.btnDireita.setOnClickListener(new View.OnClickListener() { // mostra detalhes da boleia
            @Override
            public void onClick(View view) {
                // passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment
                Bundle bundle=new Bundle();
                bundle.putString("_idboleia",ilBoleias.get(position).getIDboleia());
                //Log.e(TAG, "CARDVIEW CARDVIEW: " +  String.valueOf(ilBoleias.get(position).getOrigem()));
                //Utils.minhaTosta(mContext,  R.drawable.completo, String.valueOf(ilBoleias.get(position).getNome()), "short", "sucesso").show();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Fragment myFragment = new BoleiasDetalhesFragment();
                myFragment.setArguments(bundle);

                VarGlobals g=(VarGlobals) view.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                g.flagDecoration=false;

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, myFragment)
                        .addToBackStack("BoleiasPesquisaRecyclerView")
                        .commit();


            }
        });



        holder.btnEsquerda.setOnClickListener(new View.OnClickListener() { //Adiona utilizador à boleia
            @Override
            public void onClick(View view) {

                if(!(ilBoleias.get(position).getLugaresdisponiveis()).equals("0")) {
                    // passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("_idboleia", ilBoleias.get(position).getIDboleia());

                    //Utils.minhaTosta(mContext, R.drawable.completo, String.valueOf(ilBoleias.get(position).getNome()), "short", "sucesso").show();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    VarGlobals g = (VarGlobals) view.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                    g.flagDecoration = false;

                    BoleiasAdicionaDialogFragment newFragment = BoleiasAdicionaDialogFragment.newInstance("x", "x1");
                    newFragment.setArguments(bundle);
                    newFragment.show(((MainActivity) mContext).getSupportFragmentManager(), "BoleiasAdicionaDialogFragment");
                }else{
                    Utils.minhaTosta(mContext,  R.drawable.cancelado, "Lotação Esgotada", "short", "erro").show();
                }


            }
        });




    }

    //endregion

    /************************************************************
     * ViewHolder
     ************************************************************/
    //region ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public  TextView mDestino, mOrigem,mDtapartida,mHorapartida,mHhorachegada, mLugares, mObjetivo,mEstado,mNomecondutor;
        public  ImageView mImgcondutor,mImgestado,mImgobj;
        public Button btnEsquerda, btnDireita;
        public CardView cardView;




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
                btnEsquerda.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_mb_addboleia, 0, 0, 0);
                btnEsquerda.setText("INSCREVER");
                btnEsquerda.setPadding(35,1,35,1);
                btnEsquerda.setTextColor(Color.parseColor("#303f9f"));
            this.btnDireita =  view.findViewById(R.id.btnCardViewDireita);
                btnDireita.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_details, 0);
                btnDireita.setText("DETALHES");
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
                case 0:
                    break;
            }
        }
        //endregion
    }

    //endregion
}