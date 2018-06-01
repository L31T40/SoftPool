package com.example.softpool.softpool;






import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import android.widget.ListView;

//import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements
        BoleiasActivasUserFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        CriarBoleiaFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener,
        PesquisaBoleiasFragment.OnFragmentInteractionListener{

    FragmentManager fragmentManager = getSupportFragmentManager();


    //private PesquisaBoleiasFragment mNavigationDrawerFragment;
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



        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /**   FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace XXXXXXXXXXXX with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //fragmentManager = getSupportFragmentManager();
       fragmentManager.beginTransaction()
                .add(R.id.content_frame, new UserFragment(),"cenastag")
                .commit();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }




    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // update the main content by replacing fragments



        if (id == R.id.nav_area_user ) {

            //android.app.FragmentManager fm = getFragmentManager();


            /*//addShowHideListener(R.id.nav_area_user, fragmentManager.findFragmentById(R.id.content_frame));
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            android.app.FragmentManager fm = getFragmentManager();
            android.app.Fragment fragment = fm.findFragmentByTag("cenastag");

            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment xxx=new UserFragment();
            //Fragment XX =  getSupportFragmentManager().findFragmentById(R.id.UserFragmentLayout);

            ft.show(fragment);*/


            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new UserFragment())
                    .commit();

           /* fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(UserFragment())
                    .commit();*/


        } else if (id == R.id.nav_criar_boleias) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new CriarBoleiaFragment())
                    .commit();

        } else if (id == R.id.nav_pesquisa_boleias) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new PesquisaBoleiasFragment())
                    .commit();

           /* fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new PesquisaBoleiasFragment())
                    .commit();*/
        } else if (id == R.id.nav_alterapass) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BoleiasActivasUserFragment())
                    .commit();

        }
        else if (id == R.id.nav_sair) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void addShowHideListener(int buttonId, final Fragment fragment) {
        /*final Button button = findViewById(buttonId);
        final int button1= buttonId;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {*/
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out);
                if (fragment.isHidden()) {
                    ft.show(fragment);
                    //button.setText("Hide");
                } else {
                    ft.hide(fragment);
                   // button.setText("Show");
                }
                ft.commit();
            }
        //});
    //}

}
