package com.example.softpool.softpool;

import android.app.Activity;


import android.content.Context;

import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasPesquisaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasPesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasPesquisaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public Activity ma;

    int posPartidaPes=0;
    int posChegadaPes=0;
    String tempPartida="";



    Spinner mySpinnerChegada, mySpinnerPartida,t1,t2;
    String spinnerChegStr, spinnerPartStr;

    public ArrayList<HashMap<String, String>> ListaLocais;

    public BoleiasPesquisaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoleiasPesquisaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoleiasPesquisaFragment newInstance(String param1, String param2) {
        BoleiasPesquisaFragment fragment = new BoleiasPesquisaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate do layout para este fragmento
        final View view = inflater.inflate(R.layout.pesquisa_boleias_fragment, container, false);


        VarGlobals gLocais=(VarGlobals) getActivity().getApplication(); // Buscar a string para o autoextview
        ListaLocais=gLocais.listaLocais1;


        String spinnerArray[] = new String[ListaLocais.size()]; // coloca os nomes das cidade em um array para apresentar no spinner
        if (ListaLocais.size()>0) {
            spinnerArray[0] = " ";
            for (int j = 1; j < ListaLocais.size(); j++) {
                spinnerArray[j] = ListaLocais.get(j).get("nomecidade");
                Log.e(TAG, "DADOS SPINNER " + spinnerArray[j]);
            }
        }else {
            Utils.minhaTosta(getContext(),  R.drawable.cancelado, "ERRO no Servidor", "short", "erro").show();
        }



        final  ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerArray);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mySpinnerPartida = view.findViewById(R.id.spinner_FiltraData);
        mySpinnerPartida.setAdapter(adapterSpinner);
        String localFunc= SharedPref.readStr(SharedPref.KEY_LOCAL, null);
        mySpinnerPartida.setSelection(adapterSpinner.getPosition(localFunc));
        mySpinnerPartida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    posPartidaPes = position;
                    spinnerPartStr = item.toString();
                    if(position==0){
                        spinnerPartStr="%20";
                    }

                }
                //Toast.makeText(getActivity(), "Local Partida : " +spinnerPartStr, Toast.LENGTH_SHORT).show();
                Log.e("LOCAL PARTIDA : ",spinnerPartStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });



        mySpinnerChegada = view.findViewById(R.id.spinner_Chegada);
        mySpinnerChegada.setAdapter(adapterSpinner);
        mySpinnerChegada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                if (item != null) {
                    posChegadaPes=position;
                    spinnerChegStr=item.toString();
                    if(position==0){
                        spinnerChegStr="%20";
                    }
                    Log.e("LOCAL CHEGADA : ",item.toString());
                }

                Log.e("LOCAL CHEGADA : ",spinnerChegStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


//ivertes as posições do local de partida com o de chegada


        ImageView swapbutton =  view.findViewById(R.id.imageView_swap);

        swapbutton.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View arg0) {



                String strtemp=tempPartida; // variavel temporaria para inverter as posições
                int posTemp=posPartidaPes;

                t1 = view.findViewById(R.id.spinner_FiltraData);
                mySpinnerPartida.setSelection(posChegadaPes);


                t2 = view.findViewById(R.id.spinner_Chegada);
                mySpinnerChegada.setSelection(posTemp);


                posPartidaPes=posChegadaPes;
                posChegadaPes=posTemp;


            }});




        Button btnpesquisa =  view.findViewById(R.id.btnPesquisar);
        btnpesquisa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {


                String partidaArray[] = new String[ListaLocais.size()]; // vai buscar o IDlocal das coidades escolhidas
                for(int j =0;j<ListaLocais.size();j++){
                    partidaArray[j] = ListaLocais.get(j).get("nomecidade");
                    if (partidaArray[j].equals(spinnerPartStr)){
                        spinnerPartStr=ListaLocais.get(j).get("idlocal");
                        Log.e(TAG, "PARAMETRO PARTIDA " + spinnerPartStr);
                    };
                    if (partidaArray[j].equals(spinnerChegStr)){
                        spinnerChegStr=ListaLocais.get(j).get("idlocal");
                        Log.e(TAG, "PARAMETRO CHEGADA " + spinnerChegStr);
                    };
                }



            /** passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment**/

                Bundle bundle=new Bundle();
                bundle.putString("_partidapes",spinnerPartStr);
                bundle.putString("_chegadapes",spinnerChegStr);
                bundle.putString("_tipodata",null);
                bundle.putString("_tipohora",null);
                bundle.putString("_datafiltro",null);
                bundle.putString("_horafiltro",null);

                BoleiasPesquisaListaFragment newFragment = new BoleiasPesquisaListaFragment();
                newFragment.setArguments(bundle);



                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, newFragment)
                        .addToBackStack("UserDetailsFfragment") /**coloca fragmento na stack para poder voltar atras**/
                        .commit();



       }});


        Button btnfiltrar =  view.findViewById(R.id.btnFiltro);
        btnfiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String partidaArray[] = new String[ListaLocais.size()]; // vai buscar o IDlocal das coidades escolhidas
                for(int j =0;j<ListaLocais.size();j++){
                    partidaArray[j] = ListaLocais.get(j).get("nomecidade");
                    if (partidaArray[j].equals(spinnerPartStr)){
                        spinnerPartStr=ListaLocais.get(j).get("idlocal");
                        Log.e(TAG, "PARAMETRO PARTIDA " + spinnerPartStr);
                    };
                    if (partidaArray[j].equals(spinnerChegStr)){
                        spinnerChegStr=ListaLocais.get(j).get("idlocal");
                        Log.e(TAG, "PARAMETRO CHEGADA " + spinnerChegStr);
                    };
                }


                Bundle bundle=new Bundle();
                bundle.putString("_partidapes",spinnerPartStr);
                bundle.putString("_chegadapes",spinnerChegStr);



                android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();

                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new BoleiasFiltrarFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(ft, "dialog");
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
        public void onFragmentInteraction(Uri uri);
    }




}


   /*     final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaLocaiss);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinnerPartida = view.findViewById(R.id.spinner_Partida);
        mySpinnerChegada = view.findViewById(R.id.spinner_Chegada);


        mySpinnerPartida.setAdapter(adapter1);
        mySpinnerPartida.setSelection(listaLocaiss.indexOf(1));

        mySpinnerChegada.setAdapter(adapter1);


        mySpinnerPartida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // TODO Auto-generated method stub
                String Partida=mySpinnerPartida.getSelectedItem().toString();

                Log.e("LOCAL PARTIDA : ",Partida);

                VarGlobals g1=(VarGlobals) getActivity().getApplication(); // Buscar a string para o autoextview
                final ArrayList<String>  listaLocaiz=g1.listaLocais;
                String ss = mySpinnerPartida.getSelectedItem().toString();
                posPartidaPes=listaLocaiz.indexOf(mySpinnerPartida.getSelectedItem().toString())+1; //Verifica a posição da escolha no autotextview incrementa 1 valor, pois o array começa a 0
                tempPartida = mySpinnerPartida.getSelectedItem().toString();// atribui a string da posição escolhida à variavel

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


/*

        autoTextViewPartida = view.findViewById(R.id.autoCompleteTextView_localPartida);
        autoTextViewChegada = view.findViewById(R.id.autoCompleteTextView_localChegada);
        autoTextViewPartida.setThreshold(1);
        autoTextViewChegada.setThreshold(1);
        autoTextViewPartida.setAdapter(adapterSpinner);
        autoTextViewChegada.setAdapter(adapterSpinner);

        this.autoTextViewPartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VarGlobals g1=(VarGlobals) getActivity().getApplication(); // Buscar a string para o autoextview
                final ArrayList<String>  listaLocaiz=g1.listaLocais;

                posPartidaPes=listaLocaiz.indexOf(autoTextViewPartida.getText().toString())+1;// Verifica a posição da escolha no autotextview incrementa 1 valor, pois o array começa a 0
                tempPartida = autoTextViewPartida.getText().toString();// atribui a string da posição escolhida à variavel
                //Toast.makeText(getActivity(), "posição: "+pos,
                //        Toast.LENGTH_LONG).show(); // should be your desired number
            }
        });

        this.autoTextViewChegada.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VarGlobals g1=(VarGlobals) getActivity().getApplication(); // Buscar a string para o autoextview
                final ArrayList<String>  listaLocaiz=g1.listaLocais;

                posChegadaPes=listaLocaiz.indexOf(autoTextViewChegada.getText().toString())+1; //Verifica a posição da escolha no autotextview incrementa 1 valor, pois o array começa a 0
                tempChegada = autoTextViewChegada.getText().toString(); // atribui a string da posição escolhida à variavel

                //Toast.makeText(getActivity(), "posição: "+pos,
                 //       Toast.LENGTH_LONG).show(); // should be your desired number
            }
        });
*/


    /*    tvdata =  view.findViewById(R.id.textViewDataBoleiaChegada);
        tvdata.setText("DD/MM/AAAA");
        TextView button =  view.findViewById(R.id.textViewDataBoleiaChegada);
       // getActivity().findViewById(R.id.textViewDataBoleia).setOnClickListener(this);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                tvdata =  view.findViewById(R.id.textViewDataBoleiaChegada);
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);

                mShowMonth = mMonth + 1;

                tvdata.setText(mDay + "/" + mShowMonth + "/" + mYear);

                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        tvdata.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }});*/

