package com.example.foodallergyapp.App.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.foodallergyapp.App.helper.HttpHandler;
import com.example.foodallergyapp.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.regex.Pattern;
import static java.lang.Thread.sleep;

/*
 * *
 * @Auther Ian Holderness 14023756
 * Reference
 * ************************************************************

   ************************************************************
 * /
 */

public class Register extends AppCompatActivity {

    //Sets tbe regex patter to check that the email address is in the correct format
    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}");
    //Sets the regex patter for the password a mix of upper and lower case with special character
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("(?=^.{6,255}$)((?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))^.*");
    // declares the objects that ares uses within the registration form.
    private TextInputLayout txtInputFirstName;
    private TextInputLayout txtInputSurName;
    private TextInputLayout txtInputEmail;
    private TextInputLayout txtInputPassword;
    private TextInputLayout txtInputPassword2;
    private Button btnSubmit;
    private boolean chkEmail = false;
    private int id =0;
    private SharedPreferences prefs;
    private SharedPreferences.Editor mEditor;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // get he object from the login form
        txtInputFirstName = findViewById(R.id.txtInputFirstName);
        txtInputSurName = findViewById(R.id.txtInputSurName);
        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        txtInputPassword2 = findViewById(R.id.txtInputPassword2);
        btnSubmit = findViewById(R.id.btnSubmit);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = prefs.edit();

        //Set the event listener for the submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkEmail = false;
                new chkEmail().execute(txtInputEmail.getEditText().getText().toString().trim());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               // Log.e("JSON", "emailchk : " + chkEmail);
                if (chkFirstName() & chkSurname() & vaildEmail() & MatchPasswords() & PasswordFormat() & chkEmail) {

                    //Test output reg user
                    //Toast.makeText(Register.this, "Registering User", Toast.LENGTH_SHORT).show();
                    new RegisterUser().execute(
                            txtInputFirstName.getEditText().getText().toString().trim(),
                            txtInputSurName.getEditText().getText().toString().trim(),
                            txtInputEmail.getEditText().getText().toString().trim(),
                            txtInputPassword.getEditText().getText().toString());

                    //Log.e("User", "getId: "+user.getId());
                    try {
                        sleep(1000);
                        if(id !=0){

                            mEditor.putInt("userID", id);
                            mEditor.commit();
                            //output for user reg complete
                            Toast.makeText(Register.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                            //Sets the activity
                            i=new Intent(getBaseContext(),MainActivity.class);
                            startActivity(i);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    //output for user reg failed
                    Toast.makeText(getApplicationContext(),
                            "Registration failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }



        private boolean chkFirstName() {
        String firstName = txtInputFirstName.getEditText().getText().toString().trim();
        //Check if details have been entered if not show error
        if (firstName.isEmpty() || firstName.length() == 0 || firstName.equals("") || firstName == null) {
            Log.d("Fname", "chkFirstName: "+firstName.length());
            txtInputFirstName.setError("Field must be completed");
            return false;

        } else {
            Log.d("Fname", "chkFirstName: Not null "+firstName.length() + " "+firstName.isEmpty() + " "+firstName.equals("")+" "+firstName);
            txtInputFirstName.setError("");
            return true;
        }
    }

    private boolean chkSurname() {
        String Surname = txtInputSurName.getEditText().getText().toString().trim();
        //Check if surname has been ented if not show the error
        if (Surname.isEmpty() || Surname.length() == 0 || Surname.equals("") || Surname == null) {
            txtInputSurName.setError("Field must be completed");
            return false;
        } else {
            txtInputSurName.setError("");
            return true;
        }
    }
    //TEST 12-13
    private boolean vaildEmail() {
        String emailInput = txtInputEmail.getEditText().getText().toString().trim();
        // chceif if emaiul has been entered if not show error
        if (emailInput.isEmpty()) {
            txtInputEmail.setError("Field can't be empty");
            return false;
        // Check if the email format is corret using the regex if not show the error
        } else if (!EMAIL_ADDRESS.matcher(emailInput).matches()) {
            txtInputEmail.setError("Email address not valid");
            return false;
        } else {
            // check if the email ahs already been registerd
            if(chkEmail == false){
                txtInputEmail.setError("Email already registered");
                return false;
            }else{
                txtInputEmail.setError("");
                Log.d("Email", "state : true");
                return true;
            }


        }
    }

    private boolean MatchPasswords() {
        //TESTING if the passwrords match
        /*String password1 = "123456789";
        String password2 = "123456789";*/
        String password1 = txtInputPassword.getEditText().getText().toString();
        String password2 = txtInputPassword2.getEditText().getText().toString();
        //Test output
        //Log.d("pwd", "p1: "+password1 +" p2: "+password2);
        if (password1.equals(password2)) {
            txtInputPassword2.setError("");
            return true;

        } else {
            txtInputPassword2.setError("Passwords do not match");
            return false;
        }
    }
    // checksin if the password meets the criteria from regex
    private boolean PasswordFormat() {
        String password1 = txtInputPassword.getEditText().getText().toString().trim();
        if (!PASSWORD_PATTERN.matcher(password1).matches()) {
            txtInputPassword.setError("Password does not meet the required complexity");
            return false;
        }else{
            txtInputPassword.setError("");
            return true;
        }

    }
    //XMLHTTPRequest to check if the email has already been registered
    private class chkEmail extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params)  {
            HttpHandler sh = new HttpHandler();
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/chkEmail.php?Email="+params[0];
            Log.e("JSON", "pram: " + params[0]+" "+url);
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    int rtnJson = jsonObj.getInt("chkEmail");
                    Log.e("JSON", "rtnJson: " + rtnJson);

                    if(rtnJson == 0){
                        chkEmail = true;
                        Log.e("JSON", "chkEmail: " + chkEmail);
                    }
                }catch (final JSONException e) {
                    Log.e("Error", "Barcode: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "URL : ttps://comp-hons.uhi.ac.uk/~14023756/Hons/chkEmail.php  could not be found",
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

    }
    //XMLHTTPRequest to passw the user details to the database
    private class RegisterUser extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params)  {
            HttpHandler sh = new HttpHandler();
            String url = "https://comp-hons.uhi.ac.uk/~14023756/Hons/RegisterUser.php?Fname="+params[0]+"&Sname="+params[1]+"&Email="+params[2]+"&Pword="+params[3];
            Log.e("RegURL", "pram: " + url);
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    int rtnJson = jsonObj.getInt("UserID");
                    Log.e("JSON", "Uid: " + rtnJson);

                    if(rtnJson != 0){
                        id = rtnJson;
                    }
                }catch (final JSONException e) {
                    Log.e("Error", "Uid: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "URL : ttps://comp-hons.uhi.ac.uk/~14023756/Hons/chkEmail.php  could not be found",
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

    }
}