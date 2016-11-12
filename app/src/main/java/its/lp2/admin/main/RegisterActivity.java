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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import its.lp2.admin.main.R;
import its.lp2.admin.support.JSONParser;

public class RegisterActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    JSONObject json = new JSONObject();
    private static String register_patient = "http://10.151.64.78/urinalysisServ/RegisterPatient.php";

    String gender[]={"Female","Male"};

    private EditText username;
    private EditText password;
    private EditText email;
    private EditText name;
    private Spinner spinner1;
    //private EditText genders;
    private EditText bio;
    private EditText website;
    private EditText address;
    private EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner1 = (Spinner) findViewById(R.id.spgender);
        String[] menuArray = new String[] { "Female", "Male" };
        ArrayAdapter<String> adapter;
        Log.d("",menuArray.toString());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        username = (EditText)findViewById(R.id.tbUsername);
        password = (EditText)findViewById(R.id.tbPassword);
        email = (EditText)findViewById(R.id.tbEmail);
        name = (EditText)findViewById(R.id.tbname);
        //genders = (EditText)findViewById(R.id.spgender);
        bio = (EditText)findViewById(R.id.tbAddress);
        website = (EditText)findViewById(R.id.tbWeb);
        address = (EditText)findViewById(R.id.tbAddress);
        phone = (EditText)findViewById(R.id.tbPhone);

        Button btnCreateUser = (Button)findViewById(R.id.button);

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateUser().execute();
            }
        });
    }


    class CreateUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            // Building Parameters

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username.getText().toString()));
            params.add(new BasicNameValuePair("password", password.getText().toString()));
            params.add(new BasicNameValuePair("email", email.getText().toString()));
            params.add(new BasicNameValuePair("name", name.getText().toString()));
            params.add(new BasicNameValuePair("bio", bio.getText().toString()));
            params.add(new BasicNameValuePair("website", website.getText().toString()));
            params.add(new BasicNameValuePair("address", address.getText().toString()));
            params.add(new BasicNameValuePair("phone", phone.getText().toString()));
            params.add(new BasicNameValuePair("genders", spinner1.getSelectedItem().toString()));
            Log.d("genders", spinner1.getSelectedItem().toString());
            // getting JSON Object
            // Note that create product url accepts POST method
            json = jParser.makeHttpRequest(register_patient, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your email verification in your email.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Register failed.", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
}
