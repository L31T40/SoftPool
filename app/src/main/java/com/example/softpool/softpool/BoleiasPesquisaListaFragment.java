package com.example.softpool.softpool;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasPesquisaListaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasPesquisaListaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasPesquisaListaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private OnFragmentInteractionListener mListener;

    public Activity aa;


    DownPesquisaBoleias dpesq;

    String resultado;

    public RecyclerView recyclerview;




    public BoleiasPesquisaListaFragment() {
        // Required empty public constructor
    }


    public static BoleiasPesquisaListaFragment newInstance(String title) {
        BoleiasPesquisaListaFragment frag = new BoleiasPesquisaListaFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
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
    public static BoleiasPesquisaListaFragment newInstance(String param1,String param2, String param3) {
        BoleiasPesquisaListaFragment fragment = new BoleiasPesquisaListaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
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
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /** faz o inflate do layou para este fragmento**/
        final View view = inflater.inflate(R.layout.boleias_pesquisa_recyclerview_fragment, container, false);
        aa=getActivity();

        final Utils utils=new Utils(aa);

        Bundle bundle = this.getArguments();/**recebe a informação da pesquisa de boleias**/
        if(bundle != null){


            dpesq = new DownPesquisaBoleias(aa);
            dpesq.setOnResponseListener(new ResponseListener() {
                @Override
                public void onResponseReceive(String data) {
                    resultado=data; /** Recebe o resultado da asynctask  pelo metodo onPostExecute**/
                }
            });

            /**inicia a tarefa asynctask de acordo com a pesquisa efectuada*/
            dpesq.listaBoleias = new ArrayList<>();
            dpesq.recyclerview =  view.findViewById(R.id.RecyclerViewPesq);
            dpesq.PartidaPes = getArguments().getString("_partidapes");
            dpesq.ChegadaPes = getArguments().getString("_chegadapes");
            dpesq._tipodata = getArguments().getString("_tipodata");
            dpesq._tipohora = getArguments().getString("_tipohora");
            dpesq._datafiltro = getArguments().getString("_datafiltro");
            dpesq._horafiltro = getArguments().getString("_horafiltro");
            dpesq.execute();



        }



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
