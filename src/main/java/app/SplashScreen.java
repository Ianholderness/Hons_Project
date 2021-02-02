/*
 * *
 * @Auther Ian Holderness 14023756
 * /
 */

package com.example.foodallergyapp.App.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodallergyapp.R;


public class SplashScreen extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        i=new Intent(getBaseContext(),Login.class);
        Thread background = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                    startActivity(i);
                    finish();
                }catch (Exception e){
                }
            }
        };
        background.start();
    }
}
