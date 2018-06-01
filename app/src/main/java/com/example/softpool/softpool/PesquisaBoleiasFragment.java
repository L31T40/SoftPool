package com.example.softpool.softpool;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PesquisaBoleiasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PesquisaBoleiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaBoleiasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Calendar currentDate;
    int mDay, mMonth, mShowMonth, mYear,hour, mhour, minute;
    TextView tvdata,tvhora;
    String format;



    public PesquisaBoleiasFragment() {
        // Required empty public constructor
    }





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaBoleiasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaBoleiasFragment newInstance(String param1, String param2) {
        PesquisaBoleiasFragment fragment = new PesquisaBoleiasFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pesquisa_boleias_fragment, container, false);

        tvdata =  view.findViewById(R.id.textViewDataBoleia);
        tvhora =  view.findViewById(R.id.textViewHoraBoleia);
        currentDate = Calendar.getInstance();

        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = currentDate.get(Calendar.MONTH);
        mYear = currentDate.get(Calendar.YEAR);
        hour = currentDate.get(Calendar.HOUR_OF_DAY);
        minute = currentDate.get(Calendar.MINUTE);

        mhour = selectedTimeFormat(hour);
        mShowMonth = mMonth + 1;

        tvdata.setText(mDay + "/" + mShowMonth + "/" + mYear);
        tvhora.setText(mhour + " : " + minute + " " /*+ format*/);

        TextView button = (TextView) view.findViewById(R.id.textViewDataBoleia);
       // getActivity().findViewById(R.id.textViewDataBoleia).setOnClickListener(this);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        tvdata.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }});

        TextView buttonHora = (TextView) view.findViewById(R.id.textViewHoraBoleia);
        buttonHora.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //hourOfDay = selectedTimeFormat(hourOfDay);
                        tvhora.setText(hourOfDay + " : " + minute + " " /*+ format*/);
                    }
                }, hour , minute, true);
                timePickerDialog.show();
            }});
    return view;
    }


    public int selectedTimeFormat(int hour){

        if(hour == 0){
            hour += 12;
            format = "AM";
        } else if(hour == 12){
            format = "PM";
        } else if(hour > 12){
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        return hour;

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
