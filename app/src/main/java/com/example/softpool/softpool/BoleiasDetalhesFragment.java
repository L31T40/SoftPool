package com.example.softpool.softpool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasDetalhesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasDetalhesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasDetalhesFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //private OnFragmentInteractionListener mListener;
    private BoleiasDetalhesFragment.OnFragmentInteractionListener mListener;
    public Activity aa;
    DownActivasBoleiasDetalhes dDetalhes;
    DownInfoUser dPassageiro;
    public String numPassageiros;
    private Button button;


    public BoleiasDetalhesFragment() {
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
    public static BoleiasDetalhesFragment newInstance(String param1, String param2) {
        BoleiasDetalhesFragment fragment = new BoleiasDetalhesFragment();
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

        aa=getActivity();

        Bundle bundle = this.getArguments();//recebe a informação da pesquisa de boleias
        if(bundle != null){
            dDetalhes = new DownActivasBoleiasDetalhes(aa);
            //inicia a tarefa asynctask de acordo com a pesquisa efectuad
            dDetalhes.listaprs = new ArrayList<>();
            dDetalhes.listaprsPassageiro = new ArrayList<>();
            dDetalhes._idboleia = getArguments().getString("_idboleia");//passa idboleia para pesquisa
            dDetalhes.execute();


        }



        Button btnaddboleia =  view.findViewById(R.id.btnCardDetalhe); //Cria a boleia
        btnaddboleia.setOnClickListener(new View.OnClickListener() { //Adiona utilizador à boleia
            @Override
            public void onClick(View view) {

                TextView ldisponivel = view.findViewById(R.id.textView_LugaresDisp);
                String ldisponivel_= (String)ldisponivel.getText();

                if(ldisponivel_!="0") {
                    // passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("_idboleia", getArguments().getString("_idboleia"));

                   // Utils.minhaTosta(view, R.drawable.completo, String.valueOf(ilBoleias.get(position).getNome()), "short", "sucesso").show();
                    //AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    VarGlobals g = (VarGlobals) view.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                    g.flagDecoration = false;

                    android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();

                    Fragment prev = getFragmentManager().findFragmentByTag("BoleiaDetalhesFragment");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new BoleiasAdicionaDialogFragment();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(ft, "BoleiaDetalhesFragment");

                }else{
                    Utils.minhaTosta(getActivity(),  R.drawable.cancelado, "Lotação Esgotada", "short", "erro").show();
                }


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
