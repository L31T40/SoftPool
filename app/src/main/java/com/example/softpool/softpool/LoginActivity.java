package com.example.softpool.softpool;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Iterator;


import javax.net.ssl.HttpsURLConnection;



/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener/*,LoaderCallbacks<Cursor>*/ {
    String nfunc,idfunc, password;
    int nFuncGlobal;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mnFuncView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mSplashScreen;
    private Button btnlogin;
    private String _nFunc;
    private String _idFunc;
    public Activity ma;
    DownInfoUser dpUser  = new DownInfoUser(ma);
    LinearLayout l1,l2;
    Animation uptodown,downtoup;
    Animation fadein,fadeout;
    //String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        btnlogin=findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(this);
        mSplashScreen=findViewById(R.id.SplashScreen);

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
            /**este metodo é chamado apos 2 segundos, duração da animação splash screen**/

                mSplashScreen.animate().setDuration(300).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSplashScreen.setVisibility(View.GONE);
                        mLoginFormView.setVisibility(View.VISIBLE);
                    }
                });

            }
        };
        final Handler h = new Handler();
        h.removeCallbacks(runnable); //
        h.postDelayed(runnable, 3000);


        /** Cria animação no Login**/
        l1 =  findViewById(R.id.l1);
        l2 =  findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);


        //Incializa classe  shared preferences
        SharedPref.init(getApplicationContext());



        String img = SharedPref.readStr(SharedPref.KEY_FOTO, null);
        String path = Environment.getExternalStorageDirectory()+ "/Images/"+img;


        boolean logged = SharedPref.readBoolean(String.valueOf(SharedPref.KEY_ISLOGGED), false );//vai verificar se a opção lembrar esta activa.

        if (logged){ /**se sessão estiver ativa, atualiza a variavel global com numero do funcionario*/
            _idFunc = SharedPref.readStr(SharedPref.KEY_USER, null);
            VarGlobals g=(VarGlobals) getApplication(); // Criar Variavel global com ID utilizador
            g.idFuncGlobal=_idFunc;

            _nFunc = SharedPref.readStr(SharedPref.KEY_NUMFUNC, null);
            VarGlobals g1=(VarGlobals) getApplication(); // Criar Variavel global com ID utilizador
            g1.NFuncGlobal=_nFunc;


            Intent intent = new Intent(this,  MainActivity.class);
            startActivity(intent);
            finish(); /** termina a ativity login**/

        }



        File imgFile = new File(path);/**se a imagem do funcionario existir coloca no login*/
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = findViewById(R.id.imgViewUser);
            imageView.setImageBitmap(myBitmap);
        }

        TextView btnEmail =  this.findViewById(R.id.textView);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de Acesso à aplicação SoftPool");
                intent.putExtra(Intent.EXTRA_TEXT, "Pedido de Acesso à aplicação SoftPool");
                intent.setData(Uri.parse("mailto:jorgeleitao76@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);

            }
        });



        /** Cria o login e vai actualizar pelas shared prefrences*/
        mnFuncView =  findViewById(R.id.editTextNFunc);
        mnFuncView.setText(SharedPref.readStr(SharedPref.KEY_NUMFUNC, null));



        mPasswordView = findViewById(R.id.editTextPassword);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btnLogin || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });



    }


    public void onClick(View view) {//for btnlogin
        nfunc=mnFuncView.getText().toString();
        password=mPasswordView.getText().toString();
        new UserLoginTask(nfunc, password).execute();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset aos erros.
        mnFuncView.setError(null);
        mPasswordView.setError(null);

        // guarda os valores da tentativa de login
        String nfunc = mnFuncView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // verifica a password caso tenha sido inserida uma
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // verifica se inseriu o numero de funcionario
            if (TextUtils.isEmpty(nfunc)) {
                mnFuncView.setError(getString(R.string.error_field_required));
                focusView = mnFuncView;
                cancel = true;

        }

        if (cancel) {
            // erro e tenta direcionar o focus para o form field com erro
            focusView.requestFocus();
        } else {
            // lança o spinner de progresso e ao mesmo tempo a asynctask de login

            showProgress(true);

            new UserLoginTask(nfunc, password);

        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, String> {

        private final String mFunc;
        private final String mPassword;

        UserLoginTask(String nfunc, String password) {
            mFunc = nfunc;
            mPassword = password;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL("http://193.137.7.33/~estgv16287/index.php/ctrlogin/validardadosMobile"); // here is your URL path

                JSONObject postDataParams = new JSONObject();



                postDataParams.put("_user", nfunc);
                postDataParams.put("_password", password);

                Log.e("params: ",postDataParams.toString());
                Log.e("URL: ",url.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                Log.e("Codigo de Resposta", "Codigo de Resposta "+responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {//code 200 connection OK
                    //receber a resposta do servidor
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));
                    Log.e("resposta: ",conn.getInputStream().toString());

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    do{
                        sb.append(line);
                        Log.e("MSG sb",sb.toString());
                    }while ((line = in.readLine()) != null) ;

                    in.close();
                    Log.e("resposta",conn.getInputStream().toString());
                    Log.e("mensagem",sb.toString());
                    return sb.toString();//server response message

                }
                else {

                    return new String("Falso : "+responseCode);
                }
            }
            catch(Exception e){
                //erro na ligação
                return new String("Excepção: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            mAuthTask = null;
            showProgress(false);

            loginVerification(result);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private void loginVerification(String svrmsg){

        if (svrmsg.equals("sucesso")){
            Utils.minhaTosta(this,  R.drawable.completo, "Login com Sucesso", "short", "sucesso").show();


            dpUser.ma = this;
            dpUser.execute(nfunc);



            VarGlobals g=(VarGlobals) getApplication(); // Criar Variavel global com ID utilizador
            g.NFuncGlobal=nfunc;



            CheckBox checkBox =findViewById(R.id.chkLembrar); /** Se Checkbox estiver seleccionada guarda o login**/
            if (checkBox.isChecked()) {
                     SharedPref.writeBoolean(String.valueOf(SharedPref.KEY_ISLOGGED), true);
            }



            Intent intent = new Intent(this,  MainActivity.class);
            startActivity(intent);
            finish(); /** termina a ativity login**/


        }
        else {//

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Email/password inválidos!");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }/**fim da Verificação de login*/


}
