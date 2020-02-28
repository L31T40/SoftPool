package com.example.softpool.softpool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasActivasDetalhesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasActivasDetalhesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasActivasDetalhesFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //private OnFragmentInteractionListener mListener;
    private BoleiasActivasDetalhesFragment.OnFragmentInteractionListener mListener;
    public Activity ma;
    DownActivasBoleiasDetalhes dDetalhes;
    DownInfoUserBoleias dpBoleias;
    DownInfoUser dPassageiro;
    public String numPassageiros;
    private Button button;


    public BoleiasActivasDetalhesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoleiasDetalhesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoleiasActivasDetalhesFragment newInstance(String param1, String param2) {
        BoleiasActivasDetalhesFragment fragment = new BoleiasActivasDetalhesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.boleias_activas_detalhes_fragment, container, false);

        ma=getActivity();

        Bundle bundle = this.getArguments();//recebe a informação da pesquisa de boleias
        if(bundle != null){
            dDetalhes = new DownActivasBoleiasDetalhes(ma);
            //inicia a tarefa asynctask de acordo com a pesquisa efectuad
            dDetalhes.listaprs = new ArrayList<>();
            dDetalhes.listaprsPassageiro = new ArrayList<>();
            dDetalhes._idboleia = getArguments().getString("_idboleia");//passa idboleia para pesquisa
            dDetalhes.execute();
        }



        Button btnaddboleia =  view.findViewById(R.id.btnCardDetalhe); //Cria a boleia

        btnaddboleia.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_boleia_cancel, 0, 0, 0);
        btnaddboleia.setText("SAIR BOLEIA");
        btnaddboleia.setPadding(10,1,5,1);
        btnaddboleia.setTextColor(Color.parseColor("#ffffff"));


        btnaddboleia.setOnClickListener(new View.OnClickListener() { //Adiona utilizador à boleia
            @Override
            public void onClick(View view) {
                dpBoleias = new DownInfoUserBoleias(ma);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Deseja sair da boleia?");
                alertDialogBuilder.setPositiveButton("SIM",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                try{

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("IDUTILIZADOR",SharedPref.readStr(SharedPref.KEY_USER, null));
                                    jsonObject.put("IDBOLEIA", getArguments().getString("_idboleia"));

                                    String x2=SharedPref.readStr(SharedPref.KEY_USER, null);
                                    String x3=getArguments().getString("_idcondutor");
                                    if(!x2.equals(x3)){
                                        EnviarJSONparaBD dp = new EnviarJSONparaBD(ma); //Criar viaturas
                                        dp.jsonObjSend=jsonObject;
                                        dp.urljson= "http://193.137.7.33/~estgv16287/index.php/activas/sairboleiamobile";
                                        dp.execute();
                                        Utils.minhaTosta(ma, R.drawable.completo, "Boleia Removida", "long", "sucesso").show();

                                        VarGlobals g1=(VarGlobals) ma.getApplicationContext();
                                        final String idfuncglobal=g1.idFuncGlobal;
                                        dpBoleias.cancel(true);
                                        dpBoleias=new DownInfoUserBoleias(ma);
                                        dpBoleias.listaBoleias = new ArrayList<>();
                                        dpBoleias.recyclerview =  ma.findViewById(R.id.RecyclerViewActivas);
                                        dpBoleias.execute(idfuncglobal);
                                    } else {
                                        Utils.minhaTosta(getActivity(), R.drawable.cancelado, "É Condutor, não pode Cancelar", "long", "erro").show();
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



        return view;




    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
