
package com.example.foodallergyapp.App.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.foodallergyapp.App.helper.HttpHandler;
import com.example.foodallergyapp.R;
import com.google.android.material.textfield.TextInputLayout;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private TextInputLayout txtInputEmail;
    private TextInputLayout txtInputPassword;
    private Button btnLogin;
    private Button btnReg;
    private CheckBox chkRem;
    private SharedPreferences prefs;
    private SharedPreferences.Editor mEditor;
    private int id =0;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        chkRem = findViewById(R.id.chkRem);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = prefs.edit();


        if(prefs.getBoolean("chkRem", false) == true) {
            txtInputEmail.getEditText().setText(prefs.getString("Login", null));
            txtInputPassword.getEditText().setText(prefs.getString("Pword", null));
            chkRem.setChecked(true);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserLogin().execute(txtInputEmail.getEditText().getText().toString().trim(), txtInputPassword.getEditText().getText().toString().trim());
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getBaseContext(),Register.class);
                startActivity(i);
            }
        });
    }
    // Xmlhttp to get the uid from the db based on the users password & email
    private class UserLogin extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params)  {
            HttpHandler sh = new HttpHandler();
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/ProcessLogin.php?Email="+params[0]+"&userpass="+params[1];
            //Log.e("Reg", "Email : " + params[0] + " Pwd : " + params[1]);
            String jsonStr = sh.makeServiceCall(url);
            // checks the returend json is not mull
            if (jsonStr != null) {
                try {
                    //created the json array
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    // Log.e("Reg", "userID : " + prefs.getInt("userID",0));
                    // gets the int value of UserID
                    id = jsonObj.getInt("UserID");

                }catch (final JSONException e) {
                    Log.e("Error", "Uid: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "URL :https://comp-hons.uhi.ac.uk/~14023756/Hons/ProcessLogin.php  could not be found",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("Error Json", "Couldn't get json from server.");
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
            super.onPostExecute(aVoid);
            // checks if the userid has been returned, if uid =0 then login has failed and warning
            if (id !=0){
                //Stores the userID in mEditor to be called by the applcaion at a latter time
                mEditor.putInt("userID", id);
                //Check if the uer has selected for their dettails to be remembered
                if (chkRem.isChecked() == true){
                    //Add the user details to mEditor for when the user next logs in
                    mEditor.putString("Login", txtInputEmail.getEditText().getText().toString().trim());
                    mEditor.putString("Pwrd", txtInputPassword.getEditText().getText().toString().trim());
                    mEditor.putBoolean("chkRem", true);
                }else{
                    mEditor.putString("Login", null);
                    mEditor.putString("Pwrd", null);
                    mEditor.putBoolean("chkRem", false);
                }
                mEditor.commit();


                //Log.e("Reg", "Email : " + prefs.getString("Login","TestFailed") + " Pwd : " + prefs.getString("Pwrd","TestFailed") + " chk : "+ prefs.getBoolean("chkRem",false));

                i=new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
            }else{
                txtInputPassword.setError("Password and/or Email do not match");
            }

        }
    }
}
