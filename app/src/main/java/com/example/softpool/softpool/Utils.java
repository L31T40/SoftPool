package com.example.softpool.softpool;




import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


import static android.content.Context.MODE_PRIVATE;


public class Utils{




     Activity ma;

    public Utils(Activity ma_) {
        ma = ma_;
    }

    int[] estadoIcons = new int[]{
            R.drawable.ic_menu_manage,
            R.drawable.progresso,
            R.drawable.completo,
            R.drawable.cancelado,
            R.drawable.canceladmin,
            R.drawable.espera};

    int[] estadoObj = new int[]{
            R.drawable.ic_viajoemlazer,
            R.drawable.ic_viajoemtrabalho};

    String[] estadoString = new String[]{
            "",
            "Progresso",
            "Completo",
            "Cancelado",
            "Cancelado Admin"};




    /**devolve String data no formato pretendido**/
    public String stringParaData(String aData,String FormatoOriginal, String FormatoNovo) {

        if(aData==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat dt = new SimpleDateFormat(FormatoOriginal); /** Formato original**/

        Date date = dt.parse(aData,pos);
        SimpleDateFormat DataOut = new SimpleDateFormat(FormatoNovo); /** transforma no formato dado por parametro**/
        String _data=DataOut.format(date);
        return _data;

    }


    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = ma.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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


    public class userid {
        private  userid mInstance= null;

        public int UserID;

        protected userid(){}

        public  synchronized userid getInstance() {
            if(null == mInstance){
                mInstance = new userid();
            }
            return mInstance;
        }
    }


    public int formatoHora(int hour){
        String format;
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




    public static Toast minhaTosta(Context mAct, int imageResId, String toastText, String toastLength, String succTypeColor) {

        final Toast toast;


        if (toastLength.equals("short")) {
            toast = Toast.makeText(mAct, toastText+"   ", Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(mAct, toastText+"   ", Toast.LENGTH_LONG);
        }

        View tView = toast.getView();


        TextView mText =  tView.findViewById(android.R.id.message);



/*****************************************/

        LinearLayout linearLayout = null;

        float density = mAct.getResources().getDisplayMetrics().density;
        int imageSize = (int) (density * 25 + 1f);
        int imageMargin = (int) (density * 15 + 1);
        linearLayout = (LinearLayout) tView;

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageSize, imageSize);
        imageParams.setMargins(imageMargin+10, 0, imageMargin, 0);
        imageParams.gravity = Gravity.CENTER_VERTICAL;


        ImageView imageView = new ImageView(mAct);
        imageView.setImageResource(imageResId);
        imageView.setLayoutParams(imageParams);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(imageView, 0);
/*****************************************/

        ViewGroup.LayoutParams textParams = mText.getLayoutParams();
        ((LinearLayout.LayoutParams) textParams).gravity = Gravity.CENTER_VERTICAL | (Gravity.CENTER_HORIZONTAL);
        mText.setTypeface(null, Typeface.NORMAL);
        mText.setTextSize(17);
        //mText.setTypeface(applyFont(mAct));
        mText.setShadowLayer(10, 2, 2, 0);
        tView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toast.cancel();
            }
        });
        tView.invalidate();
        if (succTypeColor.equals("erro")) {
            mText.setTextColor(Color.parseColor("#ffffff"));
            tView.setBackground(mAct.getResources().getDrawable(R.drawable.ic_tosta_erro));
            // mensagem de erro
        }
        if (succTypeColor.equals("sucesso")) {
            mText.setTextColor(Color.parseColor("#ffffff"));
            tView.setBackground(mAct.getResources().getDrawable(R.drawable.ic_tosta));
            // mensagem sucesso
        }


        return toast;
    }





    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }



    public URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    /*  public static int[] swappos(int a, int b){
        int tmp=0;
        int _a=a;
        int _b=b;

        return new int[] {_a, _b+};
    }*/





  /*  public String imgEstado(String estado) {

        ImageView image = DownInfoUserBoleias.findViewById(R.id.imageView_estado);
        image.setImageResource(R.drawable.canceladmin)
        if(estado==null) return null;
        String _objetivo="";
        if (estado=="1")
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        else
            _objetivo="Profissional";

        return _objetivo;

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }*/



    /**public void saveUser(String SHARED_PREF_NAME, String nfunc, String nome, String email, String tel, String local ) {


    private static final String SHARED_PREF_NAME = "asminhascenas";
          final String KEY_USER = "";
          final String KEY_NAME = "";
          final String KEY_EMAIL = "";
          final String KEY_TEL = "";
          final String KEY_LOCAL = "";


        SharedPreferences preferences = ma.getSharedPreferences("xx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER, nfunc);
        editor.putString(KEY_NAME, nome);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TEL, tel);
        editor.putString(KEY_LOCAL, local);
        editor.apply();






    }**/
}