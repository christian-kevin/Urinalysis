package its.lp2.admin.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import its.lp2.admin.main.R;
import its.lp2.admin.support.JSONParser;

public class EditProfileActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_select_profile = "http://10.151.64.78/urinalysisServ/UpdateProfileInfo.php";

    HashMap<String, String> ProfileList = new HashMap<String, String>();
    JSONArray profil = null;

    EditText nama_user;
    Spinner gender_user;
    EditText bio_user;
    EditText website_user;
    EditText address_user;
    EditText phone_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nama_user = (EditText) findViewById(R.id.tbEditName);
        gender_user = (Spinner) findViewById(R.id.editSpgender);
        website_user = (EditText) findViewById(R.id.tbEditWeb);
        bio_user = (EditText) findViewById(R.id.tbEditBio);
        address_user = (EditText) findViewById(R.id.tbEditAddress);
        phone_user = (EditText) findViewById(R.id.tbEditPhone);

        String[] menuArray = new String[]{"Female", "Male"};

        String compareValue = getIntent().getExtras().getString("gender_user");
        ArrayAdapter<String> adapter;
        Log.d("", menuArray.toString());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1;
        spinner1 = (Spinner) findViewById(R.id.editSpgender);

        spinner1.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner1.setSelection(spinnerPosition);
            spinnerPosition = 0;
        }

        nama_user.setText(getIntent().getExtras().getString("nama_user"));
        //gender_user.setSelected();
        website_user.setText(getIntent().getExtras().getString("website_user"));
        bio_user.setText(getIntent().getExtras().getString("bio_user"));
        address_user.setText(getIntent().getExtras().getString("address_user"));
        phone_user.setText(getIntent().getExtras().getString("phone_user"));

        Button btnCreateUser = (Button)findViewById(R.id.buttonSaveProfile);

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditProfileUser().execute();
            }
        });
    }

    class EditProfileUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setMessage("Loading your profile info. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", "" + getIntent().getExtras().getInt("id_user")));
            params.add(new BasicNameValuePair("name", nama_user.getText().toString()));
            params.add(new BasicNameValuePair("bio", bio_user.getText().toString()));
            params.add(new BasicNameValuePair("website", website_user.getText().toString()));
            params.add(new BasicNameValuePair("address", address_user.getText().toString()));
            params.add(new BasicNameValuePair("phone", phone_user.getText().toString()));
            params.add(new BasicNameValuePair("genders", gender_user.getSelectedItem().toString()));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_select_profile, "POST", params);
            Log.d("profile Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                    startActivity(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "User successfully updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Update failed.", Toast.LENGTH_SHORT).show();
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
            getMenuInflater().inflate(R.menu.menu_edit_profile, menu);

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
