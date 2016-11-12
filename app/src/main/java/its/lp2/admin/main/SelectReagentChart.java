package its.lp2.admin.main;

import android.content.Intent;
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
import android.widget.TextView;

import its.lp2.admin.main.R;

public class SelectReagentChart extends ActionBarActivity {

    private Spinner spReagent;
    private Spinner spMonth;
    private EditText txtYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reagent_chart);

        //Reagent's spinner
        spReagent = (Spinner) findViewById(R.id.spReagentChart);
        String[] reagentArray = new String[] { "Glucose", "Ketone", "Leukocytes", "Nitrite", "Bilirubin", "Urobilinogen", "Protein", "Haemoglobin" };
        ArrayAdapter<String> adapterReagent;
        Log.d("", reagentArray.toString());

        adapterReagent = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reagentArray);
        adapterReagent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReagent.setAdapter(adapterReagent);

        //Month's spinner
        spMonth = (Spinner) findViewById(R.id.spMonthChart);
        String[] monthArray = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        ArrayAdapter<String> adapterMonth;
        Log.d("",monthArray.toString());

        adapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthArray);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(adapterMonth);

        txtYear = (EditText) findViewById(R.id.txtYear);
        Button btnSubmitSelectParamReagent = (Button) findViewById(R.id.btnSubmitSelectParamReagent);
        btnSubmitSelectParamReagent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitSelectParam();
            }
        });

    }

    public void SubmitSelectParam(){
        Intent intent = new Intent(this,ChartActivity.class);
        intent.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
        intent.putExtra("reagent", spReagent.getSelectedItem().toString());
        intent.putExtra("month", spMonth.getSelectedItem().toString());
        intent.putExtra("year", txtYear.getText().toString());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_reagent_chart, menu);
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
