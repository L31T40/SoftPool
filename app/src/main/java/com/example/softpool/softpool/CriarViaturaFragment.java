package com.example.softpool.softpool;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CriarViaturaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CriarViaturaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriarViaturaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public Activity aa;

    int spinnerPos;
    Switch switchViaturaPropria, switchSeguro;
    Spinner spinnerLotacao;
    Spinner spinnerCombustivel;
    EditText edittextMarca, edittextModelo, edittextMatricula;
    ArrayList<String> ListaCombustivel=null;


    public CriarViaturaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CriarViaturaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CriarViaturaFragment newInstance(String param1, String param2) {
        CriarViaturaFragment fragment = new CriarViaturaFragment();
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
        View view= inflater.inflate(R.layout.criar_viatura_fragment, container, false);


        VarGlobals gLocais=(VarGlobals) getActivity().getApplication(); /** Buscar a string para o autoextview**/
        ListaCombustivel=gLocais.listaCombustivel;


        VarGlobals g=(VarGlobals) view.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
        g.flagDecoration=false;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, ListaCombustivel);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCombustivel = view.findViewById(R.id.spinner_Combustivel);
        spinnerCombustivel.setAdapter(adapter);
        spinnerCombustivel.setSelection(0);
        spinnerCombustivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                spinnerPos=position;
                if (item != null) {
                    Toast.makeText(getActivity(), item.toString(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(), "Selected:"+ item.toString(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(getActivity(), "posição:"+ spinnerPos,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        edittextMarca =  view.findViewById(R.id.editText_Marca);
        edittextModelo =  view.findViewById(R.id.editText_Modelo);
        edittextMatricula =  view.findViewById(R.id.editText_Matricula);
        spinnerLotacao =  view.findViewById(R.id.spinner_Lotacao);
        switchSeguro =  view.findViewById(R.id.switch_Seguro);
        switchViaturaPropria =  view.findViewById(R.id.switch_ViaturaPropria);



        /**botao para criação de boleia
         *
         * **/
        Button btncriar =  view.findViewById(R.id.btnCriar);
        btncriar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String swSeguro="1", swViaturaPropria="1";
                String _idutilizador=SharedPref.readStr(SharedPref.KEY_USER, null);

                if (!switchSeguro.isChecked()){
                    swSeguro="0";
                }
                if (!switchViaturaPropria.isChecked()) {
                    swViaturaPropria = "0";
                    _idutilizador="0000001";//Utilizador SOFTINSA
                }

                String _marca = edittextMarca.getText().toString();
                String _modelo =  edittextModelo.getText().toString();
                String _matricula = edittextMatricula.getText().toString();
                String _combustivel = String.valueOf(spinnerPos+1);
                String _lotacao = spinnerLotacao.getSelectedItem().toString();
                String _seguro = swSeguro;
                String _viaturapropria = swViaturaPropria;



                /** passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment**/


                try{

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("IDUTILIZADOR",_idutilizador);
                    jsonObject.put("MARCA",_marca);
                    jsonObject.put("MODELO",_modelo);
                    jsonObject.put("MATRICULA",_matricula);
                    jsonObject.put("LOTACAO",_lotacao);
                    jsonObject.put("IDCOMBUSTIVEL",_combustivel);
                    jsonObject.put("SEGURO_CONTRA_TODOS_OS_RISCOS",_seguro);

                    aa=getActivity();
                    EnviarJSONparaBD dp = new EnviarJSONparaBD(aa); //Criar viaturas
                    dp.jsonObjSend=jsonObject;
                    dp.urljson= "http://soft.allprint.pt/index.php/criar/inserirveiculoandroid";
                    dp.execute();


                }catch (JSONException ex){
                    //TODO handle Error here
                    Log.e(TAG, "ERRO " + ex);
                }


            }});

        Button btncancelar =  view.findViewById(R.id.btnCancelar);
        btncancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
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
