package com.example.foodallergyapp.App.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodallergyapp.App.classes.product;
import com.example.foodallergyapp.App.fragments.fragAllergens;
import com.example.foodallergyapp.App.fragments.fragIngredient;
import com.example.foodallergyapp.App.helper.HttpHandler;
import com.example.foodallergyapp.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**

CODE REFRENCES
####################################
Created :
@author : Ian Holderness 14023756
@version  : 1.0.1
Last Update:
Version Updates:

1.0.0 - 1.0.1 : Addition of

Refrences:

Tabview : https://www.youtube.com/watch?v=ltQMyvgIkMs&t=751s

###################################


*/
public class ScannedData extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabItem tabAllergens,tabIngredient;
    ImageView imageView;
    private SharedPreferences prefs;
    public int foundA;
    private int uid;
    private String TAG = fragIngredient.class.getSimpleName();
    ArrayList<HashMap<String, String>> ingredList;
    product p = new product();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanned_data2);
        //Get the barcode that has been passed as a pramater from BarcodeScanner
        Intent intent = getIntent();
        String passedBcode = intent.getStringExtra("Barcode");
        // Tesing for barcode being pass
        /*Toast.makeText(getApplicationContext(),
                "Test : " + passedBcode,
                Toast.LENGTH_LONG).show();*/

        //Getting the userid from which is storted in PreferenceManager
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        uid = prefs.getInt("userID", 0);
            // calls the GetData process to collect the ingredents from the database
            new GetData().execute(passedBcode);
            // declares each of the fragments used within the tabview
            tabLayout = findViewById(R.id.tabLayout);
            tabAllergens = findViewById(R.id.tabAllergens);
            tabIngredient = findViewById(R.id.tabIngredient);
            viewPager = findViewById(R.id.viewPager);
            ingredList = new ArrayList<>();

        // create the page adapter from the fragments
        pagerAdapter = new com.example.foodallergyapp.App.adapter.PagerAdapter(getSupportFragmentManager());
        //add the fragments to the adapter
        ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragAllergens(), "Allergens");
        ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragIngredient(), "Ingredient");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /*

             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            /**
             *
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             *
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
         this.finish();
    }

    private class GetData extends AsyncTask<String,Void,Void>{
        /**
         *
         * @param params
         * @return null
         */
        @Override
        protected Void doInBackground(String... params)  {
            HttpHandler sh = new HttpHandler();
            List<String> stringArray = new ArrayList<>();
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/GetIngredients.php?Barcode="+params[0]+"&Uid="+uid;
            String jsonStr = sh.makeServiceCall(url);
            final String bcode = params[0];
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    p.setDisc(jsonObj.getString("Product_Disc"));

                    foundA = Integer.valueOf(jsonObj.getString("AllergenFound"));
                    //Log.e(TAG, jsonObj.getString("Product_Disc")+ " "+params[0]+" foundA: "+p.getAllergen() +" "+jsonObj.has("AllergenFound")+" multipleContacts :"+jsonArr.toString());
                    JSONArray ingredientsArr = jsonObj.getJSONArray("Ingrent");

                    for (int i = 0; i < ingredientsArr.length(); i++) {
                        JSONObject ingredentObj = ingredientsArr.getJSONObject(i);
                        stringArray.add( ingredentObj.getString("Ingredent"));
                        //Log.e(TAG, ingredentObj.has("Allergen")+" Ingredient allergen :"+ingredentObj.getBoolean("Allergen"));
                        if(ingredentObj.getBoolean("Allergen")){
                            p.setAllergen(true);
                        }

                    }

                    p.setIngredients(stringArray);

                } catch (final JSONException e) {
                    Log.e(TAG, "Barcode: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Barcode : " + bcode + " could not be found",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ScannedData.this, BarcodeScanner.class));
                            finish();
                        }

                    });


                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            imageView = findViewById(R.id.imgTick);
            TextView textView = findViewById(R.id.lblTick);
            TextView prod = findViewById(R.id.lblproduct);

            prod.setText(product.getDisc());

            if(foundA == 1){
                imageView.setBackgroundResource(R.drawable.warning_icon);
                textView.setText("Allergen found");
                Vibrator vibrate = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                long[] mVibratePattern = new long[]{0,400,50,400,50,400};
                vibrate.vibrate(mVibratePattern, -1);
            }else{
                imageView.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                textView.setText("No allergen found");
            }
        }
    }
}