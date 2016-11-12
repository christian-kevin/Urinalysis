package its.lp2.admin.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import its.lp2.admin.main.R;

public class SelectPhotoOrCrop extends Activity {

    private static final String TAG_ID_DIPSTICK = "ID_DIPSTICK";
    String id_merk_dipstick;

    private Button btnPhoto;
    private Button btnCrop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo_or_crop);

        Intent i = getIntent();

        id_merk_dipstick = i.getStringExtra(TAG_ID_DIPSTICK);
        btnPhoto = (Button)findViewById(R.id.btnPhoto);
        btnCrop = (Button)findViewById(R.id.btnCrop);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_photo_or_crop, menu);
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
