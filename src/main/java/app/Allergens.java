package com.example.foodallergyapp.App.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.example.foodallergyapp.App.fragments.fragAdditives;
import com.example.foodallergyapp.App.fragments.fragAllergenMain;
import com.example.foodallergyapp.App.fragments.fragDairy;
import com.example.foodallergyapp.App.fragments.fragNuts;
import com.example.foodallergyapp.App.fragments.fragWheat;
import com.example.foodallergyapp.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


/**
**************************************
Created :
@author : Ian Holderness 14023756
@version  : 1.0.0
Last Update:

Version Updates:
Refrences:

**************************************
 */
public class Allergens extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabItem tabAllergenMain,tabDairy, tabWheat, tabNut, tabAdditives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergens);

        tabLayout = findViewById(R.id.tabLayout);
        tabAllergenMain = findViewById(R.id.tabAllergenMain);
        tabDairy = findViewById(R.id.tabDairy);
        tabWheat = findViewById(R.id.tabWheat);
        tabNut = findViewById(R.id.tabNuts);
        tabAdditives = findViewById(R.id.tabAdditives);
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new com.example.foodallergyapp.App.adapter.PagerAdapter(getSupportFragmentManager());

        //Creates the fragments for each of the ingrediets types
            ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragAllergenMain(), "Allergen");
            ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragDairy(), "Dairy");
            ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragWheat(), "Wheat");
            ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragNuts(), "Nuts");
            ((com.example.foodallergyapp.App.adapter.PagerAdapter) pagerAdapter).addFragment(new fragAdditives(), "Additive");

        // Test Data
       /* allergen.add(new Allergen("E150d - Sulphite ammonia caramel dioxide", "A", 19, false));
        allergen.add(new Allergen("E150d - Sulphite ammonia caramel dioxide", "A", 21, false));
        allergen.add(new Allergen("E338 - Phosphoric acid", "A", 20, true));
        allergen.add(new Allergen("Malic Acid", "A", 23, false));
        allergen.add(new Allergen("Citric Acid", "A", 24, false));
        allergen.add(new Allergen("Milk", "D", 1, false));
        allergen.add(new Allergen("Tuna", "F", 3, false));
        allergen.add(new Allergen("Wheat Flour", "G", 2, false));
        allergen.add(new Allergen("Wheat Bran", "G", 12, false));*/

        // Sets the adaptor with the number of pages
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(5);
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

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


}
