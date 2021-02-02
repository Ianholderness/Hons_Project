/*
 * *
 * @Auther Ian Holderness 14023756
 * /
 */

package com.example.foodallergyapp.App.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.foodallergyapp.App.classes.Allergen;
import com.example.foodallergyapp.App.helper.HttpHandler;
import com.example.foodallergyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class fragNuts extends Fragment {
    private static final String TAG = "fragAdditives";
    private ListView listView;
    private View view;
    public List<Allergen> Additives = new ArrayList<>();
    private boolean isloaded = false;
    private SharedPreferences prefs;
    private SharedPreferences.Editor mEditor;
    private int id;
    private boolean state;

    public fragNuts() {
        // Required empty public constructor
    }

       @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // checks is the data has already been loaded to prevent multiple elements
        Additives.clear();
        new GetAllergen().execute();
        view = inflater.inflate(R.layout.fragment_frag_nuts, container, false);

        return view;
    }
    class lstViewAdapter extends ArrayAdapter {
        public lstViewAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final View view = (getActivity()).getLayoutInflater().inflate(R.layout.lstview, null);
            final Switch s = view.findViewById(R.id.btnSwitch);
            final int pos = position;

            //Adds the discription and state to each switch button
            s.setText(Additives.get(position).getAllergenDisc());
            s.setChecked(Additives.get(position).getState());
            // attaches the eventlistener for when the user change the sate of the switch
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Allergen aTmp = Additives.get(pos);

                    aTmp.setState(isChecked);
                    String stmp = s.getText().toString();

                    Additives.get(pos).setState(isChecked);
                    id = Additives.get(pos).getAllergenID();
                    state = isChecked;


                    new SetAllergen().execute();
                    //Test user input of switch button
                    Toast.makeText(getActivity(),
                            "Ingredient - " + stmp + " ID :" + id + "  has been set to :" + isChecked,
                            Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }

    private class GetAllergen extends AsyncTask<String,Void,Void> {
        /**
         * @param params
         * @return null
         */
        @Override
        protected Void doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            //List<String> stringArray = new ArrayList<>();

            prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

            int uid = prefs.getInt("userID", 0);
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/GetAllergen.php?Type=N&Uid=" + uid;
            String jsonStr = sh.makeServiceCall(url);
            //final String bcode = params[0];
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    JSONArray allergenArray = jsonObj.getJSONArray("Allergen");

                    for (int i = 0; i < allergenArray.length(); i++) {
                        JSONObject allergenObj = allergenArray.getJSONObject(i);
                        String stmp = allergenObj.getString("state");
                        Additives.add(new Allergen(allergenObj.getString("disc"), "N", allergenObj.getInt("id"), stmp == "true"?true:false));
                        //Log.e(TAG, "tempArr Length: " + allergenObj.getBoolean("state"));
                    }

                    // p.setIngId(id);
                    //p.setIngredients(stringArray);
                } catch (final JSONException e) {
                    Log.e(TAG, "Barcode: " + e.getMessage());

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            // Inflate the layout for this fragment
            listView = view.findViewById(R.id.list);

            //Uses the custom lstViewAdapter to create the custom listview
            lstViewAdapter adapter = new lstViewAdapter(getActivity(), R.layout.lstview, Additives.toArray());
            listView.setAdapter(adapter);

        }

    }
    private class SetAllergen extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int uid = prefs.getInt("userID", 0);
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/setAllergen.php?Uid=" + uid+"&Ing="+id+"&State="+(state == true?1:0);
            sh.makeServiceCall(url);

            //Log.e(TAG, "db Aditives " + id + " ID :" + uid + "  has been set to :" + state +" url :"+url);

            return null;
        }
    }
}

