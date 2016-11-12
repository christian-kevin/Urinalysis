package its.lp2.admin.doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import its.lp2.admin.main.EditProfileActivity;
import its.lp2.admin.main.R;
import its.lp2.admin.support.JSONParser;

/**
 * Created by KBSE on 02/06/2015.
 */
public class AddPatientActivity extends ActionBarActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_select_profile = "http://10.151.64.78/urinalysisServ/SelectPatient.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PATIENT = "patient";
    private static final String TAG_NAMAUSER = "NAMA_USER";
    private static final String TAG_IDUSER = "ID_USER";
    private static final String TAG_IDPRIVILEGE = "ID_PRIVILEGE";

    HashMap<String, String> PatientList = new HashMap<String, String>();
    JSONArray patient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patient);
        new AddPatient().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
    class AddPatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPatientActivity.this);
            pDialog.setMessage("Loading your patient. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", ""+getIntent().getExtras().getInt("id_user")));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_select_profile,"GET",params);
            Log.d("profile Response", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    patient = json.getJSONArray(TAG_PATIENT);

                    // looping through All Products
                    for (int i = 0; i < patient.length(); i++) {
                        JSONObject c = patient.getJSONObject(i);

                        // Storing each json item in variable
                        String NAMA_USER = c.getString("NAMA_USER");
                        String ID_USER = c.getString("ID_USER");
                        String ID_PRIVILEGE = c.getString("ID_PRIVILEGE");

                        // adding each child node to HashMap key => value
                        PatientList.put(TAG_IDUSER, ID_USER);
                        PatientList.put(TAG_IDPRIVILEGE, ID_PRIVILEGE);
                        PatientList.put(TAG_NAMAUSER, NAMA_USER);

                        // adding HashList to ArrayList
                        //ProfileList.add(map);
                        Log.d("profil user",PatientList.toString());
                    }
                } else {
                    // no profil user found
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
            pDialog.dismiss();
        }
    }

}
