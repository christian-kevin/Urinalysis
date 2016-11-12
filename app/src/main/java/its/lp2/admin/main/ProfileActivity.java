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
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import its.lp2.admin.support.JSONParser;

public class ProfileActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_select_profile = "http://10.151.64.78/urinalysisServ/SelectProfileInfo.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_NAMAUSER = "NAMA_USER";
    private static final String TAG_GENDERUSER = "GENDER_USER";
    private static final String TAG_BIOUSER = "BIO_USER";
    private static final String TAG_WEBSITEUSER = "WEBSITE_USER";
    private static final String TAG_ADDRESSUSER = "ADDRESS_USER";
    private static final String TAG_PHONEUSER = "PHONE_USER";

    HashMap<String, String> ProfileList = new HashMap<String, String>();
    JSONArray profil = null;

    TextView nama_user;
    TextView gender_user;
    TextView bio_user;
    TextView website_user;
    TextView address_user;
    TextView phone_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ProfileList = new ArrayList<HashMap<String, String>>();
        new ProfileUser().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    //Event Button Edit Profile
    public void buttonEditProfile(View v){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        intent.putExtra("nama_user", ProfileList.get(TAG_NAMAUSER));
        intent.putExtra("gender_user", ProfileList.get(TAG_GENDERUSER));
        intent.putExtra("bio_user", ProfileList.get(TAG_BIOUSER));
        intent.putExtra("website_user", ProfileList.get(TAG_WEBSITEUSER));
        intent.putExtra("phone_user", ProfileList.get(TAG_PHONEUSER));
        intent.putExtra("address_user", ProfileList.get(TAG_ADDRESSUSER));
        startActivity(intent);

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
    class ProfileUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading your profile. Please wait...");
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
                    profil = json.getJSONArray(TAG_PROFILE);

                    // looping through All Products
                    for (int i = 0; i < profil.length(); i++) {
                        JSONObject c = profil.getJSONObject(i);

                        // Storing each json item in variable
                        String NAMA_USER = c.getString("NAMA_USER");
                        String GENDER_USER = c.getString("GENDER_USER");
                        String BIO_USER = c.getString("BIO_USER");
                        String WEBSITE_USER = c.getString("WEBSITE_USER");
                        String ADDRESS_USER = c.getString("ADDRESS_USER");
                        String PHONE_USER = c.getString("PHONE_USER");

                        // creating new HashMap


                        // adding each child node to HashMap key => value
                        ProfileList.put(TAG_NAMAUSER, NAMA_USER);
                        ProfileList.put(TAG_GENDERUSER, GENDER_USER);
                        ProfileList.put(TAG_BIOUSER, BIO_USER);
                        ProfileList.put(TAG_WEBSITEUSER, WEBSITE_USER);
                        ProfileList.put(TAG_ADDRESSUSER, ADDRESS_USER);
                        ProfileList.put(TAG_PHONEUSER, PHONE_USER);

                        // adding HashList to ArrayList
                       //ProfileList.add(map);
                        Log.d("profil user",ProfileList.toString());
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
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    setContentView(R.layout.activity_profile);
                    nama_user = (TextView) findViewById(R.id.nameInput);
                    gender_user = (TextView) findViewById(R.id.genderInput);
                    website_user = (TextView) findViewById(R.id.websiteInput);
                    bio_user = (TextView) findViewById(R.id.bioInput);
                    address_user = (TextView) findViewById(R.id.addressInput);
                    phone_user = (TextView) findViewById(R.id.phoneInput);

                    nama_user.setText(ProfileList.get(TAG_NAMAUSER));
                    gender_user.setText(ProfileList.get(TAG_GENDERUSER));
                    website_user.setText(ProfileList.get(TAG_WEBSITEUSER));
                    bio_user.setText(ProfileList.get(TAG_BIOUSER));
                    address_user.setText(ProfileList.get(TAG_ADDRESSUSER));
                    phone_user.setText(ProfileList.get(TAG_PHONEUSER));
                }
            });
        }
    }
}
