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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import its.lp2.admin.main.R;
import its.lp2.admin.support.JSONParser;

public class InputLaboratoryActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    JSONObject json = new JSONObject();
    private static String add_laboratory_patient = "http://10.151.64.78/urinalysisServ/AddLaboratoryPatient.php";
    private EditText tanggal;
    private EditText value;
    private Spinner parameter;
    private Spinner laboratorium;
    private EditText status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_laboratory);

        String[] menuArray = new String[] { "Glucose", "Ketone", "Leukocytes", "Nitrite", "Bilirubin", "Urobilinogen", "Protein", "Haemoglobin" };
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] menuLab = new String[] { "Pramita", "Populer" };

        tanggal = (EditText)findViewById(R.id.date_field);
        value = (EditText)findViewById(R.id.value_field);
        status = (EditText)findViewById(R.id.status_field);
        parameter = (Spinner)findViewById(R.id.param_field);
        laboratorium = (Spinner)findViewById(R.id.laboratorium_field);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parameter.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuLab);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        laboratorium.setAdapter(adapter);

        status.setText(""+getIntent().getExtras().getInt("id_user"));
        tanggal.setText(date);

        Button btnSubmitLaboratory = (Button)findViewById(R.id.buttonSubmitLaboratory);
        btnSubmitLaboratory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputLaboratory().execute();
            }
        });
    }

    class InputLaboratory extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InputLaboratoryActivity.this);
            pDialog.setMessage("Input Laboratory Result..");
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

            params.add(new BasicNameValuePair("id_user", ""+getIntent().getExtras().getInt("id_user")));
            params.add(new BasicNameValuePair("tanggal", tanggal.getText().toString()));
            params.add(new BasicNameValuePair("value", value.getText().toString()));
            params.add(new BasicNameValuePair("status", status.getText().toString()));
            params.add(new BasicNameValuePair("parameter", parameter.getSelectedItem().toString()));
            params.add(new BasicNameValuePair("laboratorium", laboratorium.getSelectedItem().toString()));

            // getting JSON Object
            // Note that create product url accepts POST method
            json = jParser.makeHttpRequest(add_laboratory_patient, "GET", params);

            // check log cat fro response
            Log.d("Insert Lab Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), LaboratoryActivity.class);
                    i.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                    startActivity(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Laboratory result successfully created.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Oops! An error occurred.", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_input_laboratory, menu);
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
