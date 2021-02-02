package com.example.foodallergyapp.App.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.foodallergyapp.App.classes.product;
import com.example.foodallergyapp.R;

import java.util.List;

import static java.lang.Thread.sleep;

/**
 *
//https://developer.android.com/reference/android/support/v4/app/FragmentPagerAdapter
 https://www.youtube.com/watch?v=E6vE8fqQPTE
 */
public  class fragIngredient extends Fragment {
    private ListView listView;
    private View view;
    private List<String> items = product.getIngredients();

    public fragIngredient() {
        // Required empty public constructor
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @throws NullPointerException
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException{
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        view = inflater.inflate(R.layout.fragment_frag_ingredient, container, false);

        listView =  view.findViewById(R.id.list);
        listView.setAdapter(null);
        items = product.getIngredients();

        if(items != null){
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    items
            );
            listView.setAdapter(listViewAdapter);
        }
        // Log.e(TAG, "Json bcode: " + p);
        //Log.e(TAG, "Json ingredient: " + items);

        // Inflate the layout for this fragment
        return view;
    }
    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
