package com.example.softpool.softpool;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.NavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        BoleiasActivasUserFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        CriarBoleiaFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener,
        BoleiasPesquisaListaFragment.OnFragmentInteractionListener,
        CriarViaturaFragment.OnFragmentInteractionListener,
        UserDetailsFragment.OnFragmentInteractionListener,
        BoleiasDetalhesFragment.OnFragmentInteractionListener,
        BoleiasFiltrarFragment.OnFragmentInteractionListener,
        BoleiasAdicionaDialogFragment.OnFragmentInteractionListener,
        BoleiasActivasDetalhesFragment.OnFragmentInteractionListener,
        BoleiasPesquisaFragment.OnFragmentInteractionListener{



    private ImageView buttonUser;
    boolean doubleBackToExitPressedOnce = false;
    private BoleiasPesquisaFragment mNavigationDrawerFragment;
    public ArrayList<String> listaComb=null;
    private Boolean flagcomb=false;

    //private UserFragment mNavigationDrawerFragment;

    public void onListFragmentInteraction(){
        //you can leave it empty
    }


    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);



        DownInfoLocais dpLocais = new DownInfoLocais(this); //Classe para fazer o parse do JSON com os Locais
        // dpLocais.ma = this;
        dpLocais.listaprs = new ArrayList<>();
        dpLocais.execute();

        VarGlobals g2=(VarGlobals) this.getApplication(); // Criar Variavel global com os locais existentes na classe VarGolbals
        g2.listaLocais1=dpLocais.listaprs;



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nView =  navigationView.getHeaderView(0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new UserFragment())
                .commit();


        buttonUser = nView.findViewById(R.id.imageView_User);

        buttonUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new UserDetailsFragment() )
                        .addToBackStack("UserDetailsFragment") //coloca fragmento na stack para poder voltar atras
                        .commit();
                Log.d("FRAGMENTO: ","User Details activity");
                     // fecha a navigation drawer
                DrawerLayout drawer =  findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }



    }






    @Override
    public void onBackPressed() {
        FragmentManager mgr = getSupportFragmentManager();
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))  {
            drawer.closeDrawer(GravityCompat.START);
        }
        if  (doubleBackToExitPressedOnce){/** caso clique 2 vezes seguidas no botão voltar atras, termina aplicação**/
            super.onBackPressed();
            return;
        }
        if  (mgr.getBackStackEntryCount()!= 0){/** se a existirem fragmentos na stack**/
            mgr.popBackStack();
            Log.d("FRAGMENTO: ","foi terminado");

        }
        else {
            mgr.popBackStack();
        }



        this.doubleBackToExitPressedOnce = true;


        Utils.minhaTosta(this,  R.drawable.cancelado, "Outra vez para sair", "short", "sucesso").show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_area_user ) {

                    fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new UserFragment())
                    .commit();

        } else if (id == R.id.nav_criar_boleias) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new CriarBoleiaFragment())
                    .addToBackStack("CriarBoleiasfragment")
                    .commit();
        } else if (id == R.id.nav_pesquisa_boleias) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BoleiasPesquisaFragment())
                    .addToBackStack("PesquisaBoleiasfragment")
                    .commit();
        } else if (id == R.id.nav_criar_viatura) {

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new CriarViaturaFragment()  )
                        .addToBackStack("CriarViaturaFfragment")
                        .commit();


        }
        else if (id == R.id.nav_logout) {

            SharedPref.writeBoolean(String.valueOf(SharedPref.KEY_ISLOGGED), false);
            /**https://stackoverflow.com/questions/3007998/on-logout-clear-activity-history-stack-preventing-back-button-from-opening-l**/
            /**faz logout fazendo a limpeza da sessão e volta para a login activity*/
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             this.startActivity(intent);
            //this.finish(); //terina activity
           // System.exit(0);


        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
