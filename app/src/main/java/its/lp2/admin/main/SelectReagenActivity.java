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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import android.widget.AdapterView.OnItemSelectedListener;
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

import its.lp2.admin.support.JSONParser;

public class SelectReagenActivity extends ListActivity{

    private Spinner ReagentSpinner, LevelSpinner;

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_select_reagent = "http://10.151.64.78/urinalysisServ/SelectReagent.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REAGENT = "reagent";
    private static final String TAG_IDREAGENT = "ID_REAGENT";
    private static final String TAG_NAMAREAGENT = "NAMA_REAGENT";

    JSONArray reagent = null;
    ArrayList<HashMap<String, String>> ReagentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reagen);

        ReagentList = new ArrayList<HashMap<String, String>>();
        Log.d("1","1");
        new SelectReagent().execute();
        Log.d("2","2");

        ListView lvReagent = getListView();
        Log.d("2","2");

        lvReagent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        TakeImageActivity.class);
                // sending pid to next activity
                in.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                in.putExtra("id_dipstick", getIntent().getExtras().getInt("id_dipstick"));
                in.putExtra("id_reagent", Integer.parseInt(pid));
                Log.d("your dipstick : ", ""+getIntent().getExtras().getInt("id_dipstick"));
                Log.d("your reagent : ", ""+pid);
                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class SelectReagent extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectReagenActivity.this);
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
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_select_reagent,"GET",params);
            Log.d("Reagent Response", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    reagent = json.getJSONArray(TAG_REAGENT);

                    // looping through All Products
                    for (int i = 0; i < reagent.length(); i++) {
                        JSONObject c = reagent.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_IDREAGENT);
                        String name = c.getString(TAG_NAMAREAGENT);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_IDREAGENT, id);
                        map.put(TAG_NAMAREAGENT, name);

                        // adding HashList to ArrayList
                        ReagentList.add(map);
                        Log.d("reagent",ReagentList.toString());
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
                    ListAdapter adapter = new SimpleAdapter(
                            SelectReagenActivity.this, ReagentList,
                            R.layout.list_item_reagent, new String[] { TAG_IDREAGENT,
                            TAG_NAMAREAGENT},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_reagen, menu);
        return true;
    }
}
