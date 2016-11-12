package its.lp2.admin.control;

import android.app.Activity;
import android.util.Log;

import its.lp2.admin.preferences.TransferData;

/**
 * Created by KBSE on 08/05/2015.
 */
public class PatientControl {

    private Activity context;
    public PatientControl(Activity context)
    {
        this.context = context;
    }

    public boolean login(String email)
    {
        Log.d("qweqweqwe", email);
        boolean result = false;
        try
        {
            String url = "LoginPatient.php?email="+email;
            String content = new TransferData.JsonTask(context).execute(url).get();
            Log.d("hasil:", content.toString()+"/n");
            Log.d("url:", url+"/n");
            if(content.equals("found")) result = true;
            else result = false;
        }
        catch(Exception e) {  }
        return result;
    }
}
