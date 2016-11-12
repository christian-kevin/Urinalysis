package its.lp2.admin.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import its.lp2.admin.control.PatientControl;
import its.lp2.admin.doctor.RegisterDoctor;
import its.lp2.admin.support.JSONParser;


public class LoginActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    JSONObject json = new JSONObject();
    private static String login_patient = "http://10.151.64.78/urinalysisServ/LoginPatient.php";


    private Button buttonLgn;
    private EditText username;
    private EditText password;
    private PatientControl patientControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        //patientControl = new PatientControl(this);

        buttonLgn = (Button)findViewById(R.id.buttonLogin);
        buttonLgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginPatient().execute();
            }
        });
        /*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_app_title);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        */

    }
/*
    //Event Button Login
    public void buttonLoginOnClick(View v){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
*/
    //Event Button Register
    public void buttonRegisterPatientOnClick(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void buttonRegisterDoctorOnClick(View v){
        Intent intent = new Intent(this, RegisterDoctor.class);
        startActivity(intent);
    }

    class LoginPatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            params.add(new BasicNameValuePair("email", username.getText().toString()));
            json = jParser.makeHttpRequest(login_patient, "GET", params);
            Log.d("Login Response", json.toString());
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id_user", json.getInt("id_user"));
                    intent.putExtra("id_privilege", json.getInt("id_privilege"));
                    startActivity(intent);
                    /*if(json.getInt("id_user")==3)
                    {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("id_user", json.getInt("id_user"));
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("id_user", json.getInt("id_user"));
                        startActivity(intent);
                    }*/

                } else {
                    // failed to create product
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Login failed. Wrong username or password", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    @Override
    public void onBackPressed() {

    }
}
