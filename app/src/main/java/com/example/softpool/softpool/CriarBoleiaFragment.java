package com.example.softpool.softpool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CriarBoleiaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CriarBoleiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriarBoleiaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Activity aa;
    Calendar currentDate;
    int mDay, mMonth, mShowMonth, mYear,hour, mhour, minute;
    TextView tvdataPartida,tvhoraPartida;
    TextView tvdataChegada,tvhoraChegada;
    String format;

   // AutoCompleteTextView autoTextViewPartida,autoTextViewChegada,t1,t2;
    int posPartidaCria=0;
    int posChegadaCria=0;
    String tempPartida="";
    String tempChegada="";




    //Switch SwitchSeguro;
    Switch SwitchMotivo;

    public ArrayList<HashMap<String, String>> ListaLocais;
    public ArrayList<HashMap<String, String>> listaViaturas;
    String array1[];
    int spinnerPos;
    int spinnerPosViatura;
    String spinnerStringViatura;
    Spinner spinnerLotacao;
    Spinner mySpinnerLotacao;
    EditText matricula;
    Object item;
    Object itemViatura;



    int posPartidaPes=0;
    int posChegadaPes=0;



    Spinner mySpinnerChegada, mySpinnerPartida,t1,t2;

    String spinnerChegStr, spinnerPartStr;



    public CriarBoleiaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CriarBoleiaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CriarBoleiaFragment newInstance(String param1, String param2) {
        CriarBoleiaFragment fragment = new CriarBoleiaFragment();
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

        VarGlobals g1=(VarGlobals) getActivity().getApplication();
        final String idfuncglobal=g1.idFuncGlobal;

        VarGlobals g3=(VarGlobals) getActivity().getApplication();
        listaViaturas=g3.ViaturasUser;

        VarGlobals g=(VarGlobals) getActivity().getApplication(); // variavel global para detetar se foi feita a decoração no recyclerview
        g.flagDecoration=false;

        VarGlobals gLocais=(VarGlobals) getActivity().getApplication(); /** Buscar a string para o autoextview**/
        ListaLocais=gLocais.listaLocais1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.criar_boleia_fragment, container, false);


        String arrayViaturas[] = new String[listaViaturas.size()]; //colca as viaturas num array para encher o spinner
        for (int i = 0; i < listaViaturas.size(); i++) {
            arrayViaturas[i] = listaViaturas.get(i).get("matricula");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayViaturas);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = view.findViewById(R.id.spinnerViatura);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                 item = adapterView.getItemAtPosition(position);
                itemViatura = adapterView.getItemAtPosition(position);
                spinnerPosViatura=position;
                spinnerStringViatura=itemViatura.toString();
                if (item != null) {
                    int nlotacao=lotacaoParaViatura(itemViatura.toString());
                    array1 = new String[nlotacao];
                    for (int i = 0; i < nlotacao; i++) {
                        array1[i] = String.valueOf(i+1); }
                    chamaSpinner(array1); //coloca lotação da viatura referente ao veiculo escolhido
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinnerLotacao = view.findViewById(R.id.spinnerLotacao);
        spinnerLotacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                //Code Here
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
     /**TODO   https://stackoverflow.com/questions/33297676/set-second-spinner-adapter-from-first-spinner*/



        SwitchMotivo=view.findViewById(R.id.switch_Motivo);
        SwitchMotivo.setChecked(false);


        String spinnerArrayLocais[] = new String[ListaLocais.size()]; // coloca os nomes das cidade em um array para apresentar no spinner
        for(int j =0;j<ListaLocais.size();j++){
            spinnerArrayLocais[j] = ListaLocais.get(j).get("nomecidade");
            Log.e(TAG, "DADOS SPINNER " + spinnerArrayLocais[j]);
        }


        final  ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerArrayLocais);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        mySpinnerPartida = view.findViewById(R.id.spinner_Partida);
        mySpinnerPartida.setAdapter(adapterSpinner);
        String localFunc= SharedPref.readStr(SharedPref.KEY_LOCAL, null);
        mySpinnerPartida.setSelection(adapterSpinner.getPosition(localFunc));

        mySpinnerPartida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);

                if (item != null) {
                posPartidaPes=position;
                spinnerPartStr=item.toString();
                }
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

                }
                Log.e("LOCAL CHEGADA : ",spinnerChegStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        tvdataPartida =  view.findViewById(R.id.textViewCriarDataBoleia);
        tvhoraPartida =  view.findViewById(R.id.textViewCriarHoraBoleia);
        tvdataChegada =  view.findViewById(R.id.textViewCriarDataBoleiaChegada);
        tvhoraChegada =  view.findViewById(R.id.textViewCriarHoraBoleiaChegada);
        currentDate = Calendar.getInstance();

        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = currentDate.get(Calendar.MONTH);
        mYear = currentDate.get(Calendar.YEAR);
        mhour = currentDate.get(Calendar.HOUR_OF_DAY);
        minute = currentDate.get(Calendar.MINUTE);

        mShowMonth=mMonth+1;


        tvdataChegada.setText(String.format("%02d/%02d/%02d",mDay,mShowMonth,mYear)); //mostra a data com zero à esquerda
        tvdataPartida.setText(String.format("%02d/%02d/%02d",mDay,mShowMonth,mYear));


        tvhoraPartida.setText(String.format("%02d : %02d",mhour,minute));//mostra a hora com zero à esquerda
        tvhoraChegada.setText(String.format("%02d : %02d",mhour,minute));


        mySpinnerLotacao = view.findViewById(R.id.spinner_Lotacao);
        matricula = view.findViewById(R.id.editText_Matricula);

        TextView buttonData = tvdataPartida;
        buttonData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        tvdataPartida.setText(String.format("%02d/%02d/%02d",i2,i1,i));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() +24*60*60*1000);
                datePickerDialog.show();
            }});

        TextView buttonDataChegada =  tvdataChegada;

        buttonDataChegada.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        tvdataChegada.setText(String.format("%02d/%02d/%02d",i2,i1,i));
                        //tvdataChegada.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() +24*60*60*1000);
                datePickerDialog.show();
            }});



        TextView buttonHora = tvhoraPartida;
        buttonHora.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //hourOfDay = selectedTimeFormat(hourOfDay);
                        tvhoraPartida.setText(String.format("%02d : %02d",hourOfDay,minute));
                    }
                }, hour , minute, true);
                timePickerDialog.show();
            }});

        TextView buttonHoraChegada = tvhoraChegada;
        buttonHoraChegada.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //hourOfDay = selectedTimeFormat(hourOfDay);
                        tvhoraChegada.setText(String.format("%02d : %02d",hourOfDay,minute));
                    }
                }, hour , minute, true);
                timePickerDialog.show();
            }});


        ImageView swapbutton =  view.findViewById(R.id.imageView_swap);

        swapbutton.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View arg0) {


                //String strtemp=tempPartida; // variavel temporaria para inverter as posições
                int posTemp=posPartidaPes;

                t1 = view.findViewById(R.id.spinner_Partida);
                mySpinnerPartida.setSelection(posChegadaPes);


                t2 = view.findViewById(R.id.spinner_Chegada);
                mySpinnerChegada.setSelection(posTemp);

                posPartidaPes=posChegadaPes;
                posChegadaPes=posTemp;

            }});

        final Spinner spinnerlotacao =  view.findViewById(R.id.spinnerLotacao);


/**botao para criação de boleia
 *
 * falta acrescentar mais cenas nomeadamente coisas**/
        Button btncriar =  view.findViewById(R.id.btnCriar);
        btncriar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                String partidaArray[] = new String[ListaLocais.size()]; // vai buscar o IDlocal das coidades escolhidas
                for(int j =0;j<ListaLocais.size();j++){
                    partidaArray[j] = ListaLocais.get(j).get("nomecidade");
                    if (partidaArray[j].equals(spinnerPartStr)){
                        spinnerPartStr=ListaLocais.get(j).get("idlocal");
                        Log.e(TAG, "PARAMETRO PARTIDA " + spinnerPartStr);
                    }
                    if (partidaArray[j].equals(spinnerChegStr)){
                        spinnerChegStr=ListaLocais.get(j).get("idlocal");
                        Log.e(TAG, "PARAMETRO CHEGADA " + spinnerChegStr);
                    }
                }

                String swMotivo="0",swSeguro="0";

                if (SwitchMotivo.isChecked()){
                    swMotivo="1";
                }

                String idViatura = indexParaViatura(itemViatura.toString());
                String PartidaCria = String.valueOf(spinnerPartStr);
                String ChegadaCria = String.valueOf(spinnerChegStr);
                String DataBoleiaPartida  = tvdataPartida.getText().toString() + " " +  tvhoraPartida.getText().toString();
                String DataBoleiaChegada  = tvdataChegada.getText().toString() + " " +  tvhoraChegada.getText().toString();
                Utils utils=new Utils(aa);
                String dataPartida = utils.stringParaData(String.valueOf(DataBoleiaPartida),"dd/MM/yyyy HH : mm","yyyy-MM-dd HH:mm:ss"); //devolve data no formato indicado
                String dataChegada = utils.stringParaData(String.valueOf(DataBoleiaChegada),"dd/MM/yyyy HH : mm","yyyy-MM-dd HH:mm:ss"); //devolve data no formato indicado
                String lotacao = spinnerlotacao.getSelectedItem().toString();

                try{
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("IDVIATURA",idViatura);
                    jsonObject.put("ORIGEM",PartidaCria);
                    jsonObject.put("DESTINO",ChegadaCria);
                    jsonObject.put("DATA_DE_PARTIDA",dataPartida);
                    jsonObject.put("DATA_DE_CHEGADA",dataChegada);
                    jsonObject.put("LUGARES_DISPONIVEIS",lotacao);
                    jsonObject.put("OBJECTIVO_PESSOAL",swMotivo);
                    jsonObject.put("IDUTILIZADOR",SharedPref.readStr(SharedPref.KEY_USER, null));
                    aa=getActivity();
                    EnviarJSONparaBD dp = new EnviarJSONparaBD(aa); //Criar viaturas

                    dp.jsonObjSend=jsonObject;
                    dp.urljson= "http://193.137.7.33/~estgv16287/index.php/criar/criarboleiamobile";
                    dp.execute();
                    Utils.minhaTosta(aa,  R.drawable.completo, "Boleia Criada", "short", "sucesso").show();
                }catch (JSONException ex){
                    //TODO handle Error here
                    Utils.minhaTosta(aa,  R.drawable.cancelado, "ERRO no Servidor", "short", "erro").show();
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



    /**Procura o ID da viatura na Arraylist, de acordo com a matricula e volve o resultado**/
    String indexParaViatura(String strMatricula) {

        for (int i = 0; i < listaViaturas.size(); i++) { /**percorre a lista dos veiculos do utilizador*/
            HashMap<String, String> map = listaViaturas.get(i);
            if (map.containsValue(strMatricula)) {
                String idviat = map.get("idviatura");
                return idviat;
            }
        }

        return "-1"; // não encontrado
    }

  protected void chamaSpinner(String lotacao[]) {
        // TODO Auto-generated method stub
        ArrayAdapter  ad2=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,lotacao);
        ad2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLotacao.setAdapter(ad2);
       // spinnerLotacao.setSelection(ad2.getPosition(lotacao.length));
    }


    /**Procura a lotacao da viatura na Arraylist, de acordo com a matricula e devolve o resultado**/
   protected int lotacaoParaViatura(String strMatricula) {

        for (int i = 0; i < listaViaturas.size(); i++) { /**percorre a lista dos veiculos do utilizador*/
            HashMap<String, String> map = listaViaturas.get(i);
            if (map.containsValue(strMatricula)) {

                return Integer.parseInt(map.get("lotacao"));
            }
        }


        return -1; // não encontrado
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


  /*
        // insere o array dos locais nas 2 autotextview do layout
        autoTextViewPartida =view.findViewById(R.id.autoCompleteTextView_criarLocalPartida);
        autoTextViewChegada = view.findViewById(R.id.autoCompleteTextView_criarLocalChegada);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaLocaiss);
        autoTextViewPartida.setThreshold(1);
        autoTextViewChegada.setThreshold(1);
        autoTextViewPartida.setAdapter(adapter1);
        autoTextViewChegada.setAdapter(adapter1);


       //SwitchSeguro=view.findViewById(R.id.switch_Seguro);
       // SwitchSeguro.setChecked(false);

        this.autoTextViewPartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VarGlobals g1=(VarGlobals) getActivity().getApplication(); // Buscar a string para o autoextview
                final ArrayList<String>  listaLocaiz=g1.listaLocais;

                posPartidaCria=listaLocaiz.indexOf(autoTextViewPartida.getText().toString())+1; //Verifica a posição da escolha no autotextview incrementa 1 valor, pois o array começa a 0
                tempPartida = autoTextViewPartida.getText().toString();// atribui a string da posição escolhida à variavel
               //Toast.makeText(getActivity(), "posição: "+pos,         Toast.LENGTH_LONG).show(); // should be your desired number
            }
        });

        this.autoTextViewChegada.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VarGlobals g1=(VarGlobals) getActivity().getApplication(); //Buscar a string para o autoextview
                final ArrayList<String>  listaLocaiz=g1.listaLocais;

                posChegadaCria=listaLocaiz.indexOf(autoTextViewChegada.getText().toString())+1; //Verifica a posição da escolha no autotextview incrementa 1 valor, pois o array começa a 0
                tempChegada = autoTextViewChegada.getText().toString(); // atribui a string da posição escolhida à variavel

                //Toast.makeText(getActivity(), "posição: "+pos,                        Toast.LENGTH_LONG).show(); // should be your desired number
            }
        });*/
