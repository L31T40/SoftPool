package com.example.softpool.softpool;




import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {


/**devolve String data no formato pretendido**/
    public String stringParaData(String aData,String aFormato) {

        if(aData==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); /** Formato original**/
        Date date = dt.parse(aData,pos);
        SimpleDateFormat DataOut = new SimpleDateFormat(aFormato); /** transforma no formato dado por parametro**/
        return DataOut.format(date);

    }

    public String objetivo(String obj) {

        if(obj==null) return null;
        String _objetivo="";
        if (obj=="1")
            _objetivo="Pessoal";
        else
            _objetivo="Profissional";


        return _objetivo;

    }



}