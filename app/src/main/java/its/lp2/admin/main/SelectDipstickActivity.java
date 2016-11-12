package its.lp2.admin.main;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import its.lp2.admin.support.JSONParser;

/**
 * Created by illiyin on 5/28/2015.
 */
public class SelectDipstickActivity extends ListActivity {
    static final String[] merkDipstick1 = new String[]{"Urit 11G", "Mindray U11", "See More"};

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_select_dipstick = "http://10.151.64.78/urinalysisServ/SelectDipstick.php";

    String TAG_ID_DIPSTICK = "ID_DIPSTICK";
    String TAG_MERK_DIPSTICK = "MERK_DIPSTICK";

    JSONArray dipstick = null;
    private ArrayList<HashMap<String, String>> DipstickList;

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dipstick);

        Log.d("masuk", "masuk");
        int more = getIntent().getExtras().getInt("id_see_more");
        if (more == 0) {

            Log.d("masuk", merkDipstick1[0].toString());
            Log.d("masuk", merkDipstick1[1].toString());
            Log.d("masuk", merkDipstick1[2].toString());
            setListAdapter(new ArrayAdapter<String>(this, R.layout.list_dipstick, merkDipstick1));

            ListView listDipstick = getListView();
            listDipstick.setTextFilterEnabled(true);

            listDipstick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                    // Starting new intent

                    String name = ((TextView) view).getText().toString();
                    Log.d("nama", name);
                    if (name == "Urit 11G") {
                        Intent in = new Intent(getApplicationContext(),
                                SelectReagenActivity.class);
                        String pid = "18";
                        // sending pid to next activity
                        in.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                        in.putExtra("id_dipstick", Integer.parseInt(pid));
                        Log.d("your dipstick", pid);
                        // starting new activity and expecting some response back
                        startActivityForResult(in, 100);
                    } else if (name == "Mindray U11") {
                        Intent in = new Intent(getApplicationContext(),
                                SelectReagenActivity.class);
                        String pid = "19";
                        // sending pid to next activity
                        in.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                        in.putExtra("id_dipstick", Integer.parseInt(pid));
                        Log.d("your dipstick :", pid);
                        // starting new activity and expecting some response back
                        startActivityForResult(in, 100);
                    } else if (name == "See More") {
                        Intent in = new Intent(getApplicationContext(),
                                SelectDipstickActivity.class);
                        // sending pid to next activity
                        in.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                        in.putExtra("id_see_more", 1);

                        // starting new activity and expecting some response back
                        startActivityForResult(in, 100);
                    }
                }
            });
        } else {

            ListView listView = (ListView) findViewById(R.id.list_item);

            DipstickList = new ArrayList<HashMap<String, String>>();
            new SelectDipstick().execute();
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // getting values from selected ListItem
                    String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                            .toString();

                    // Starting new intent
                    Intent in = new Intent(getApplicationContext(),
                            SelectReagenActivity.class);
                    // sending pid to next activity
                    in.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
                    in.putExtra("id_dipstick", Integer.parseInt(pid));
                    Log.d("your dipstick : ", "" + pid);

                    // starting new activity and expecting some response back
                    startActivityForResult(in, 100);
                }
            });
        }
    }
    class SelectDipstick extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectDipstickActivity.this);
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
            JSONObject json = jParser.makeHttpRequest(url_select_dipstick,"GET",params);
            Log.d("Dipstick Response", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    dipstick = json.getJSONArray("dipstick");

                    // looping through All Products
                    for (int i = 0; i < dipstick.length(); i++) {
                        JSONObject c = dipstick.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString("ID_DIPSTICK");
                        String name = c.getString("MERK_DIPSTICK");

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_ID_DIPSTICK, id);
                        map.put(TAG_MERK_DIPSTICK, name);

                        DipstickList.add(map);
                        Log.d("reagent",DipstickList.toString());
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
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            SelectDipstickActivity.this, DipstickList,
                            R.layout.list_item, new String[] { TAG_ID_DIPSTICK,
                            TAG_MERK_DIPSTICK},
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
        getMenuInflater().inflate(R.menu.menu_select_dipstick, menu);
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