package its.lp2.admin.main;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import its.lp2.admin.support.JSONParser;


public class ChartActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url = "http://10.151.64.78/urinalysisServ/SelectLabResult.php";

    JSONArray reagent = null;
    ArrayList<HashMap<String, String>> ReagentList;
    ArrayList<Float> arrayScore = new ArrayList<Float>();
    String reagentName;
    String tempWaktuSubmit;
    ArrayList<Integer> tanggalReagent  = new ArrayList<Integer>();

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        new ChartView().execute();
    }

    class ChartView extends AsyncTask<String, String, String> {


         //Before starting background thread Show Progress Dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChartActivity.this);
            pDialog.setMessage("Loading Chart. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


         //getting All products from url

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL

            params.add(new BasicNameValuePair("id_user", Integer.toString(getIntent().getExtras().getInt("id_user"))));
            params.add(new BasicNameValuePair("reagent", getIntent().getExtras().getString("reagent")));
            params.add(new BasicNameValuePair("month", getIntent().getExtras().getString("month")));
            params.add(new BasicNameValuePair("year", getIntent().getExtras().getString("year")));
            JSONObject json = jParser.makeHttpRequest(url,"GET",params);
            Log.d("Score Response", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    reagent = json.getJSONArray("history");
                    Log.d("history count = ", ""+reagent.length());
                    //Log.d("arrayScore count = ", ""+arrayScore.length);
                    // looping through All Products
                    for (int i = 0; i < reagent.length(); i++) {
                        JSONObject c = reagent.getJSONObject(i);

                        // Storing each json item in variable
                        Log.d("teset count = ", "aaa");
                        arrayScore.add(Float.parseFloat(c.getString("VALUE_KADAR")));
                        reagentName = c.getString("NAMA_REAGENT");
                        tempWaktuSubmit = c.getString("WAKTU_SUBMIT");
                        tanggalReagent.add(Integer.parseInt(tempWaktuSubmit.substring(8, 10)));
                    }
                } else {
                    // no reagent found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


         //After completing background task Dismiss the progress dialog

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                     //Updating parsed JSON data into ListView

                    Log.d("","3");
                    int counts = 0;
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    int wew=0;
                    for(int a = 1; a<=31; a++)
                    {
                        wew=0;
                        for(int b = 0; b<arrayScore.size(); b++)
                        {
                            if(tanggalReagent.get(b)==a)
                            {
                                entries.add(new BarEntry(arrayScore.get(b), a));
                                Log.d("barentry : ", ""+tanggalReagent.get(b));
                                counts++;
                                wew++;
                            }
                        }
                        if(wew>0)
                        {
                            entries.add(new BarEntry(0, a));
                            counts++;
                        }
                    }

                    BarDataSet dataset = new BarDataSet(entries,"");

                    ArrayList<String> labels = new ArrayList<String>();
                    for(int a = 1; a<=31; a++)
                    {
                        labels.add(""+a);
                    }

                    Log.d("count bar : ", ""+counts);

                    BarChart chart = new BarChart(getBaseContext());
                    setContentView(chart);

                    BarData data = new BarData(labels, dataset);
                    chart.setData(data);
                    //chart.setDescription("# Glucose Score");
                    //chart.get
                    YAxis y = chart.getAxisRight();
                    //YAxis y2 = chart.getAxisRight();
                    //y2.removeAllLimitLines();
                    //y2.resetAxisMaxValue();
                    //y2.resetAxisMinValue();

                    y.setAxisMaxValue(28f);
                    y.setAxisMinValue(0f);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    chart.animateY(2500);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
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
