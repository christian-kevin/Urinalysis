package its.lp2.admin.main;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import its.lp2.admin.doctor.AddPatientActivity;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int privilege = getIntent().getExtras().getInt("id_privilege");
        if(privilege == 2)
        {
            setContentView(R.layout.activity_home_dokter);
        }
        else if(privilege == 3) {
            setContentView(R.layout.activity_home);
        }
    }

    //Event Button Test
    public void buttonTestOnClick(View v){
        Intent intent = new Intent(this,SelectDipstickActivity.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        startActivity(intent);
    }

    //Event Button Find Doctor
    public void buttonFindDoctorOnClick(View v){
        Intent intent = new Intent(this,MapActivity.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        startActivity(intent);

    }

    //Event Button History
    public void buttonHistoryOnClick(View v){
        Intent intent = new Intent(this, HistoryUrianLab.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        startActivity(intent);
    }

    //Event Button Profile
    public void buttonProfileOnClick(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        startActivity(intent);
    }

    //Event Button Help
    public void buttonHelpOnClick(View v){
        Intent intent = new Intent(this, HelpLoginActivity.class);
        startActivity(intent);
    }

    //Event Button Logout
    public void buttonLogoutOnClick(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        startActivity(intent);
    }

    //Event Button AddPatient
    public void buttonAddPatientOnClick(View v){
        Intent intent = new Intent(this, AddPatientActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    @Override
    public void onBackPressed() {

    }
}
