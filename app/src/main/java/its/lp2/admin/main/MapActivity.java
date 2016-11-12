package its.lp2.admin.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import its.lp2.admin.support.JSONParser;


public class MapActivity extends ActionBarActivity {


    private static final int GPS_ERROR_DIALOG_REQUEST=9001;

    protected LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // dalam Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // dalam Milliseconds

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String latlong = "http://10.151.64.78/urinalysisServ/LatLong.php";

    ArrayList<HashMap<String, String>> labList;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LAB = "lab";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    JSONArray lab=null;




    ArrayList<LatLng> myList;




    Marker marker;
    GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(LayananSiap())
        {
            setContentView(R.layout.activity_map);
            initMap();
            // Loading products in Background Thread
            myList = new ArrayList();
            new loadLatLong().execute();

            //showDoctor();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATES,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                    new MyLocationListener());

            showCurrentLocation();

        }
    }

    private boolean LayananSiap(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){return true;}
        else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable))
        {Dialog d = GooglePlayServicesUtil.getErrorDialog(isAvailable,this,GPS_ERROR_DIALOG_REQUEST);
            d.show();}
        else{
            Toast.makeText(this, "Tidak bisa terhubung dengan Google Service", Toast.LENGTH_SHORT).show();}
        return false;
    }



    private void gotoLocation(LatLng kordinatLokasi, int zoom)
    {
        //LatLng coordinate = new LatLng(coordinate);
        myMap.addMarker(new MarkerOptions()
                .position(kordinatLokasi)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(kordinatLokasi, zoom);
        myMap.moveCamera(update);
    }

    private void gotoLocation2(double lintang,double bujur, int zoom)
    {
        LatLng kordinatLokasi = new LatLng(lintang,bujur);
        marker =myMap.addMarker(new MarkerOptions()
                .position(kordinatLokasi)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(kordinatLokasi,zoom);
        myMap.moveCamera(update);
    }

    private boolean initMap()
    {
        if(myMap==null)
        {
            SupportMapFragment mapFrag=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            myMap=mapFrag.getMap();
        }
        return(myMap!=null);
    }

    public void showDoctor()
    {
        for(int i=0;i<myList.size();i++) {
            gotoLocation(myList.get(i), 10);
        }
    }

    private void hideSoftKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    protected void showCurrentLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            String message = String.format(
                    "Lokasi saat ini \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            gotoLocation2(location.getLatitude(), location.getLongitude(), 17);
            Toast.makeText(this, message,
                    Toast.LENGTH_LONG).show();
        }
    }

    class loadLatLong extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */

         protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList();

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(latlong, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Lab: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    lab = json.getJSONArray(TAG_LAB);


                    // looping through All Products
                    for (int i = 0; i < lab.length(); i++) {
                        JSONObject c = lab.getJSONObject(i);

                        // Storing each json item in variable
                        String lat = c.getString("latitude");
                        String longi = c.getString("longitude");
                        String id = c.getString("id");
                        String name = c.getString("nama");


                        LatLng labi = new LatLng(Double.parseDouble(lat),Double.parseDouble(longi));

                        myList.add(labi);

                    }
                    // } else {
                    // no products found

                    //}
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    //Log.d("All Lab: ", myList.get(0).toString());

                    showDoctor();
                }
            });

        }


    }


    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "Deteksi Lokasi Baru \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            if(marker!=null)marker.remove();
            gotoLocation2(location.getLatitude(), location.getLongitude(), 15);
            Toast.makeText(MapActivity.this, message, Toast.LENGTH_LONG).show();
            //switchToMap();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(MapActivity.this, "Status provider berubah",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(MapActivity.this,
                    "Provider dinonaktifkan oleh user, GPS off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(MapActivity.this,
                    "Provider diaktifkan oleh user, GPS on",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
