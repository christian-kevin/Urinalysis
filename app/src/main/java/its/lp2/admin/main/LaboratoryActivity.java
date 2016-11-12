package its.lp2.admin.main;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class LaboratoryActivity extends ListActivity {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_NAMEREAGENT = "NAME_REAGENT";
    private static final String TAG_VALUEREAGENT = "VALUE_REAGENT";
    private static final String TAG_DATEREAGENT = "DATE_REAGENT";
    private static final String TAG_STATUSREAGENT = "STATUS_REAGENT";

    JSONArray history = null;
    private ArrayList<HashMap<String, String>> HistoryList;

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url = "http://10.151.64.78/urinalysisServ/SelectLabHistory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory);
        HistoryList = new ArrayList<HashMap<String, String>>();
        new HistoryLaboratory().execute();


    }

    class HistoryLaboratory extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LaboratoryActivity.this);
            pDialog.setMessage("Loading reagents. Please wait...");
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
            JSONObject json = jParser.makeHttpRequest(url,"GET",params);
            Log.d("History Response", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    history = json.getJSONArray(TAG_HISTORY);

                    // looping through All Products
                    for (int i = 0; i < history.length(); i++) {
                        JSONObject c = history.getJSONObject(i);

                        // Storing each json item in variable
                        String name = c.getString("NAMA_REAGENT");
                        String value = c.getString("VALUE_KADAR");
                        String date = c.getString("WAKTU_SUBMIT");
                        String status = c.getString("STATUS");

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAMEREAGENT, name);
                        map.put(TAG_VALUEREAGENT, value);
                        map.put(TAG_DATEREAGENT, date);
                        map.put(TAG_STATUSREAGENT, status);

                        // adding HashList to ArrayList
                        HistoryList.add(map);
                        Log.d("reagent",HistoryList.toString());
                    }
                } else {
                    // no reagent found
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    Log.d("","3");
                    //SET NAME REAGENT
                    ListAdapter adapterName = new SimpleAdapter(
                            LaboratoryActivity.this, HistoryList,
                            R.layout.list_data_chart, new String[] { TAG_NAMEREAGENT,
                            TAG_VALUEREAGENT, TAG_DATEREAGENT, TAG_STATUSREAGENT},
                            new int[] { R.id.valuenameChart, R.id.valuevalueChart,
                                    R.id.valuedateChart, R.id.valuestatusChart});
                    // updating listview
                    setListAdapter(adapterName);

                    /*
                    //SET DATE REAGENT
                    ListAdapter adapterDate = new SimpleAdapter(
                            LaboratoryActivity.this, HistoryList,
                            R.layout.list_data_chart, new String[] { TAG_VALUEREAGENT,
                    },
                            new int[] { R.id.valuevalueChart });
                    // updating listview
                    setListAdapter(adapterDate);

                    //SET VALUE REAGENT
                    ListAdapter adapterValue = new SimpleAdapter(
                            LaboratoryActivity.this, HistoryList,
                            R.layout.list_data_chart, new String[] { TAG_DATEREAGENT,
                    },
                            new int[] { R.id.valuedateChart });
                    // updating listview
                    setListAdapter(adapterValue);

                    //SET STATUS REAGENT
                    ListAdapter adapterStatus = new SimpleAdapter(
                            LaboratoryActivity.this, HistoryList,
                            R.layout.list_data_chart, new String[] { TAG_STATUSREAGENT,
                    },
                            new int[] { R.id.valuestatusChart });
                    // updating listview
                    setListAdapter(adapterStatus);
                    */
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_laboratory, menu);
        return true;
    }

    public void buttonAddLab(View v){
        Intent intent = new Intent(this, SelectReagentChart.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        startActivity(intent);
    }

    public void buttonViewChart(View v){
        Intent intent = new Intent(this, SelectReagentChart.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
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
}
