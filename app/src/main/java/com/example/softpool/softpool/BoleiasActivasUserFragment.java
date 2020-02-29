package com.example.softpool.softpool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

//TODO:         PARA APAGAR

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoleiasActivasUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoleiasActivasUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoleiasActivasUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private SwipeRefreshLayout swipeLayout;
    private BoleiasActivasUserFragment.OnFragmentInteractionListener mListener;


    public Activity ma;

    DownInfoUserBoleias dpBoleias = new DownInfoUserBoleias(ma);



    public BoleiasActivasUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoleiasActivasUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoleiasActivasUserFragment newInstance(String param1, String param2) {
        BoleiasActivasUserFragment fragment = new BoleiasActivasUserFragment();
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
        final View view = inflater.inflate(R.layout.boleias_activas_user_recyclerview_fragment, container, false);

        ma=getActivity();

        VarGlobals g=(VarGlobals) getActivity().getApplication();
        final String nfuncglobal=g.NFuncGlobal;

        VarGlobals g1=(VarGlobals) getActivity().getApplication();
        final String idfuncglobal=g1.idFuncGlobal;


        DownInfoViaturas dpViaturas = new DownInfoViaturas();

        dpViaturas.ma = this;
        dpViaturas.listaprs =  new ArrayList<>();
        dpViaturas.execute(idfuncglobal,"1");
        g1.ViaturasUser=dpViaturas.listaprs;


        /**inicia a tarefa asynctask de acordo com a pesquisa efectuada*/

        swipeLayout =  view.findViewById(R.id.swipeRefreshLayout_Activas);//faz refresh ao fazer um swipe no ecra
        //termina a tarefa asynctask antes de iniciar outra
        swipeLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        swipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh( ) {
              dpBoleias.cancel(true);
              dpBoleias=new DownInfoUserBoleias(ma);
              dpBoleias.listaBoleias = new ArrayList<>();
              dpBoleias.recyclerview =  view.findViewById(R.id.RecyclerViewActivas);
              dpBoleias.execute(idfuncglobal);

              Log.d("REFRESH","Vai Refrescar :)");
              swipeLayout.setRefreshing(false);//termina o refresh senão a animação nao termina

          }});



        if(dpBoleias!= null && dpBoleias.getStatus() == AsyncTask.Status.FINISHED){
            Log.d("ASYNCTASK dpBoleias","TERMINOU vou cancelar");
            dpBoleias.cancel(true);// a asynctask termina e chama o onPostExecute
            dpBoleias = new DownInfoUserBoleias(ma);

        }

        /**inicia a tarefa asynctask para fazer download da informação de boleia referente ao utilizador que fez o login**/
        dpBoleias=new DownInfoUserBoleias(ma);
        dpBoleias.listaBoleias = new ArrayList<>();
        dpBoleias.recyclerview =  view.findViewById(R.id.RecyclerViewActivas);
        dpBoleias.execute(idfuncglobal);




        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View nView =  navigationView.getHeaderView(0);

        TextView nav_user = nView.findViewById(R.id.nav_nomeUser);
        TextView nav_nfunc = nView.findViewById(R.id.nav_numUser);

        // retira a informação das shared prefrences
        nav_user.setText(SharedPref.readStr(SharedPref.KEY_NAME, null));
        nav_nfunc.setText(SharedPref.readStr(SharedPref.KEY_NUMFUNC, null));

        String img=SharedPref.readStr(SharedPref.KEY_FOTO, null);
        String path = Environment.getExternalStorageDirectory()+ "/Images/"+img;

        File imgFile = new File(path);
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = nView.findViewById(R.id.imageView_User);
            imageView.setImageBitmap(myBitmap);
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
        void onFragmentInteraction(Uri uri);
    }
}
