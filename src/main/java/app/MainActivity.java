package com.example.foodallergyapp.App.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;


import com.example.foodallergyapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
**************************************
Created :
@author : Ian Holderness 14023756
@version : 1.0.0
Last Update:

Version Updates:

Refrences:

**************************************
 */

public class MainActivity extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState
     */
    //Creates the nav bar and attaches theevent listener to the oobjects
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.scan:
                       startActivity(new Intent(MainActivity.this, BarcodeScanner.class));
                       break;
                    case R.id.allergens:
                       startActivity(new Intent(MainActivity.this, Allergens.class));
                        break;
                    case R.id.settings:
                       startActivity(new Intent(MainActivity.this, Settings.class));
                        break;
                }

                return false;
            }
        });
    }
    private class GetData extends AsyncTask<Void,Void,Void> {
        protected Void doInBackground(Void...Void) {
            return null;
        }
    }
}

