package com.example.softpool.softpool;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasAdicionaDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasAdicionaDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasAdicionaDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Switch SwitchMotivo;
    String swMotivo="1";
    public Activity aa;



    private OnFragmentInteractionListener mListener;

    public BoleiasAdicionaDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoleiasFiltrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoleiasAdicionaDialogFragment newInstance(String param1, String param2) {
        BoleiasAdicionaDialogFragment fragment = new BoleiasAdicionaDialogFragment();
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
        final View view =  inflater.inflate(R.layout.boleias_adiciona_dialog_fragment, container, false);




        SwitchMotivo=view.findViewById(R.id.switch_Motivo);
        SwitchMotivo.setChecked(false);



        if (!SwitchMotivo.isChecked()){
            swMotivo="0";
        }

        Button btnsim =  view.findViewById(R.id.btnPesquisar); //Cria a boleia
        btnsim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {


                /** passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment**/

                Bundle bundle=new Bundle();
                bundle.putString("_idboleia",getArguments().getString("_idboleia"));
             // bundle.putString("_idutilizador",SharedPref.readStr(SharedPref.KEY_USER, null));
                bundle.putString("_motivo",swMotivo);

                try{
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("IDBOLEIA",getArguments().getString("_idboleia"));
                    jsonObject.put("IDUTILIZADOR",SharedPref.readStr(SharedPref.KEY_USER, null));
                    jsonObject.put("OBJETIVO",swMotivo);
                    aa=getActivity();
                    EnviarJSONparaBD dp = new EnviarJSONparaBD(aa); //Criar viaturas
                    dp.jsonObjSend=jsonObject;
                    dp.urljson= "http://193.137.7.33/~estgv16287/index.php/adicionaraboleia/adicionarMobile";
                    dp.execute();
                }catch (JSONException ex){
                    Utils.minhaTosta(aa,  R.drawable.cancelado, "INSERÇÃO INVÁLIDA", "long", "erro").show();
                }

                Utils.minhaTosta(aa,  R.drawable.completo, "BOLEIA ADICIONADA", "long", "sucesso").show();

                Fragment prev = getFragmentManager().findFragmentByTag("BoleiasAdicionaDialogFragment");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
            }});

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
