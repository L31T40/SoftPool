package com.example.softpool.softpool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasFiltrarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasFiltrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasFiltrarFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Calendar currentDate;
    int mDay, mMonth, mShowMonth, mYear;
    TextView tvdata;
    public Activity ma;

    int tipohora=0,tipodata=0;
    int  hour, mhour, minute;
    TextView  tvhora ;


    private OnFragmentInteractionListener mListener;

    public BoleiasFiltrarFragment() {
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
    public static BoleiasFiltrarFragment newInstance(String param1, String param2) {
        BoleiasFiltrarFragment fragment = new BoleiasFiltrarFragment();
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
        final View view =  inflater.inflate(R.layout.boleias_filtrar_fragment, container, false);



        tvdata =  view.findViewById(R.id.textViewFiltroData);
        tvhora  =  view.findViewById(R.id.textViewFiltraHora);



        currentDate = Calendar.getInstance();

        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = currentDate.get(Calendar.MONTH);
        mYear = currentDate.get(Calendar.YEAR);
        mhour = currentDate.get(Calendar.HOUR_OF_DAY);
        minute = currentDate.get(Calendar.MINUTE);

        mShowMonth=mMonth+1;


        TextView button =  view.findViewById(R.id.textViewFiltroData);
        // getActivity().findViewById(R.id.textViewDataBoleia).setOnClickListener(this);
        tvdata.setText(String.format("%02d/%02d/%02d",mDay,mShowMonth,mYear));
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                tvdata =  view.findViewById(R.id.textViewFiltroData);
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
                        tvdata.setText(String.format("%02d/%02d/%02d",i2,i1,i));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() +24*60*60*1000);
                datePickerDialog.show();
            }});




        Spinner spinnerFiltraHora =  view.findViewById(R.id.spinner_FiltraHora);
        spinnerFiltraHora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    tipohora=position;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });



        Spinner spinnerFiltraData =  view.findViewById(R.id.spinner_Partida);
        spinnerFiltraData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    tipodata=position;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });




        tvhora.setText(String.format("%02d : %02d",mhour,minute));//mostra a hora com zero à esquerda
        TextView buttonHora = tvhora;
        buttonHora.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //hourOfDay = selectedTimeFormat(hourOfDay);
                        tvhora.setText(String.format("%02d : %02d",hourOfDay,minute));
                      //  tvhora.setText(hourOfDay + "%02d : %02d" + minute);
                    }
                }, hour , minute, true);
                timePickerDialog.show();
            }});






        Button btnpesquisa =  view.findViewById(R.id.btnSim);
        btnpesquisa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Utils utils=new Utils(ma);
                final String dataFiltro = utils.stringParaData(tvdata.getText().toString(),"dd/MM/yyyy","yyyyMMdd"); //devolve data no formato indicado
                final String horaFiltro = utils.stringParaData(tvhora.getText().toString(),"HH : mm","HHmm"); //devolve data no formato indicado

                String spinnerPartStr = getArguments().getString("_partidapes");

                String spinnerChegStr = getArguments().getString("_chegadapes");


                /** passa as strings por parametro usando o metodo para BoleiasPesquisaListaFragment**/

                Bundle bundle=new Bundle();
                bundle.putString("_partidapes",spinnerPartStr);
                bundle.putString("_chegadapes",spinnerChegStr);
                bundle.putString("_tipodata",String.valueOf(tipodata));
                bundle.putString("_tipohora",String.valueOf(tipohora));
                bundle.putString("_datafiltro",dataFiltro);
                bundle.putString("_horafiltro",horaFiltro);


                BoleiasPesquisaListaFragment newFragment = new BoleiasPesquisaListaFragment();
                newFragment.setArguments(bundle);

                VarGlobals g=(VarGlobals) view.getContext().getApplicationContext(); // variavel global para detetar se foi feita a decoração no recyclerview
                g.flagDecoration=false;

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, newFragment)
                        .addToBackStack("UserDetailsFfragment") /**coloca fragmento na stack para poder voltar atras**/
                        .commit();

                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
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
