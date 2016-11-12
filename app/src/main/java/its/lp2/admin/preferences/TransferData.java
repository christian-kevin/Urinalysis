package its.lp2.admin.preferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
/**
 * Created by KBSE on 08/05/2015.
 */
public class TransferData {
    public static String ipAddress = "http://localhost/urinalysisServ/";

    static public class JsonTask extends AsyncTask<String, Void, String>
    {
        private Activity context;
        private ProgressDialog dialog;

        public JsonTask(Activity applicationContext)
        {
            this.context = applicationContext;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String content = "";
            String url = ipAddress+params[0];
            Log.d("url to bash", url+"/n");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            try
            {
                Log.d("1 : ", "a");
                post.setEntity(new UrlEncodedFormEntity(param));
                Log.d("1 : ", "b");
                HttpResponse httpresponse = client.execute(post);
                Log.d("1 : ", "c");
                HttpEntity httpentity = httpresponse.getEntity();
                Log.d("1 : ", "d");
                InputStream in = httpentity.getContent();
                Log.d("1 : ", "e");
                BufferedReader read = new BufferedReader(new InputStreamReader(in));

                String line = "";
                while((line = read.readLine()) != null)
                {
                    content += line;
                    Log.d("pesan:", line);
                }
            }
            catch(ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch(Exception e) {  }
            return content;
        }

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
