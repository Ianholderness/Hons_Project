package com.example.foodallergyapp.App.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


/***
 * https://stackoverflow.com/questions/29658499/trying-to-programmatically-set-the-state-of-a-switch-inside-a-listview-after-the
 */
public class fragDairy extends Fragment {
    private static final String TAG = "fragDary";
    private ListView listView;
    private View view;
    public List<Allergen> dairy = new ArrayList<>();
    private SharedPreferences prefs;
    private int id;
    private boolean state;

    public fragDairy() {
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
            dairy.clear();
            new GetAllergen().execute();
        view = inflater.inflate(R.layout.fragment_frag_dairy, container, false);

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
                s.setText(dairy.get(position).getAllergenDisc());
                s.setChecked(dairy.get(position).getState());
                // attaches the eventlistener for when the user change the sate of the switch
                s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        dairy.get(pos).setState(isChecked);
                        id = dairy.get(pos).getAllergenID();
                        state = isChecked;
                        String stmp = s.getText().toString();

                        new SetAllergen().execute();

                        Toast.makeText(getActivity(),
                                "Ingredient - " + stmp + " ID :" + id + "  has been set to :" + dairy.get(pos).getState(),
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

            prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int uid = prefs.getInt("userID", 0);
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/GetAllergen.php?Type=D&Uid=" + uid;
            String jsonStr = sh.makeServiceCall(url);
            //final String bcode = params[0];
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    JSONArray allergenArray = jsonObj.getJSONArray("Allergen");

                    for (int i = 0; i < allergenArray.length(); i++) {
                        JSONObject allergenObj = allergenArray.getJSONObject(i);

                        dairy.add(new Allergen(allergenObj.getString("disc"), "D", allergenObj.getInt("id"), allergenObj.getBoolean("state")));
                       // Log.e(TAG, "Uid : "+uid+" Disc: "+dairy.get(i).getAllergenDisc() + " id :"+allergenObj.getInt("id")+" State : "+dairy.get(i).getState() +" "+allergenObj.getBoolean("state"));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "GetAllergen: " + e.getMessage());

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
            lstViewAdapter adapter = new lstViewAdapter(getActivity(), R.layout.lstview, dairy.toArray());
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

            //Log.e(TAG, "db Ingredient " + id + " ID :" + uid + "  has been set to :" + state +" url :"+url);

            return null;
        }
    }
}