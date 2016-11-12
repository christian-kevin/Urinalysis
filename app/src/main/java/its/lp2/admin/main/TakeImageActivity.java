package its.lp2.admin.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import eu.janmuller.android.simplecropimage.CropImage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import its.lp2.admin.support.JSONParser;

public class TakeImageActivity extends ActionBarActivity {

    public final int SELECT_FILE = 1;final int PIC_CROP = 2;
    ImageView ivImage;
    public int redMode,blueMode,greenMode;
    final int MAX = 9999;
    int levelReagen=0,urian_id_user,id_reagent,id_dipstick;
    int[] color;
    double[] dist;
    int limitIndex;
    double value1,value2;
    JSONParser jsonParser = new JSONParser();
    double[] value;
    double degree;
    private ProgressDialog pDialog;
    JSONObject json = new JSONObject();
    private static String save_result = "http://10.151.64.78/urinalysisServ/SaveResult.php";
    CieLAB colorreagen;
    HSV colorreagenHSV;
    String status;
    TextView txtValue4,txtStatus,txtWelcome;
    CheckBox[] check;
    ImageView[] picView;
    int counter =0;
    Button btnSelect1,btnSave;
    Button[] btnSelecto;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_image);

        urian_id_user = getIntent().getExtras().getInt("id_user");
        id_dipstick = getIntent().getExtras().getInt("id_dipstick");
        id_reagent = getIntent().getExtras().getInt("id_reagent");

        btnSelecto = new Button[6];
        check = new CheckBox[6];
        picView = new ImageView[6];
        picView[0] = (ImageView) findViewById(R.id.ivImage1);
        picView[1] = (ImageView) findViewById(R.id.ivImage2);
        picView[2] = (ImageView) findViewById(R.id.ivImage3);
        picView[3] = (ImageView) findViewById(R.id.ivImage4);
        picView[4] = (ImageView) findViewById(R.id.ivImage5);
        picView[5] = (ImageView) findViewById(R.id.ivImage6);
        btnSelecto[0] = (Button) findViewById(R.id.btnSelectPhoto1);
        btnSelecto[1] = (Button) findViewById(R.id.btnSelectPhoto2);
        btnSelecto[2] = (Button) findViewById(R.id.btnSelectPhoto3);
        btnSelecto[3] = (Button) findViewById(R.id.btnSelectPhoto4);
        btnSelecto[4] = (Button) findViewById(R.id.btnSelectPhoto5);
        btnSelecto[5] = (Button) findViewById(R.id.btnSelectPhoto6);
        check[0] = (CheckBox) findViewById(R.id.checkBox1);
        check[1] = (CheckBox) findViewById(R.id.checkBox2);
        check[2] = (CheckBox) findViewById(R.id.checkBox3);
        check[3] = (CheckBox) findViewById(R.id.checkBox4);
        check[4] = (CheckBox) findViewById(R.id.checkBox5);
        check[5] = (CheckBox) findViewById(R.id.checkBox6);
        if(id_reagent==3)
        {
            btnSelecto[5].setVisibility(View.INVISIBLE);
            check[5].setVisibility(View.INVISIBLE);
            picView[5].setVisibility(View.INVISIBLE);
            value = new double[5];
            levelReagen=6;
            value[0]=0;
            value[1]=15;
            value[2]=70;
            value[3]=125;
            value[4]=500;
        }
        if(id_reagent==4) {
            for (int i = 2; i <= 5; i++) {
                btnSelecto[i].setVisibility(View.INVISIBLE);
                check[i].setVisibility(View.INVISIBLE);
                picView[i].setVisibility(View.INVISIBLE);
            }
            value = new double[2];
            levelReagen=3;
            value[0]=0;
            value[1]=1;
        }
        //bil
        if(id_reagent==5)
        {
            for (int i = 4; i <= 5; i++) {
                btnSelecto[i].setVisibility(View.INVISIBLE);
                check[i].setVisibility(View.INVISIBLE);
                picView[i].setVisibility(View.INVISIBLE);
            }
            value = new double[4];
            levelReagen=5;
            value[0]=0;
            value[1]=8.6;
            value[2]=33;
            value[3]=100;
        }
        //uro
        if(id_reagent==6)
        {
            for (int i = 4; i <= 5; i++) {
                btnSelecto[i].setVisibility(View.INVISIBLE);
                check[i].setVisibility(View.INVISIBLE);
                picView[i].setVisibility(View.INVISIBLE);
            }
            btnSelecto[4].setVisibility(View.INVISIBLE);
            check[4].setVisibility(View.INVISIBLE);
            picView[4].setVisibility(View.INVISIBLE);
            value = new double[4];
            levelReagen=5;
            value[0]=0;
            value[1]=33;
            value[2]=66;
            value[3]=131;
        }
        //blo
        if(id_reagent==8)
        {
            btnSelecto[5].setVisibility(View.INVISIBLE);
            check[5].setVisibility(View.INVISIBLE);
            picView[5].setVisibility(View.INVISIBLE);
            value = new double[5];
            levelReagen=6;
            value[0]=0;
            value[1]=10;
            value[2]=25;
            value[3]=80;
            value[4]=200;
        }
        //pro
        if(id_reagent==7)
        {
            btnSelecto[5].setVisibility(View.INVISIBLE);
            check[5].setVisibility(View.INVISIBLE);
            picView[5].setVisibility(View.INVISIBLE);
            value = new double[5];
            levelReagen=6;
            value[0]=0;
            value[1]=0.15;
            value[2]=0.3;
            value[3]=1;
            value[4]=3;
        }
        //ket
        if(id_reagent==2)
        {
            btnSelecto[5].setVisibility(View.INVISIBLE);
            check[5].setVisibility(View.INVISIBLE);
            picView[5].setVisibility(View.INVISIBLE);
            value = new double[5];
            levelReagen=6;
            value[0]=0;
            value[1]=0.5;
            value[2]=1.5;
            value[3]=4;
            value[4]=8;
        }
        //glu
        if(id_reagent==1)
        {
            value = new double[6];
            levelReagen=7;
            value[0]=0;
            value[1]=2.8;
            value[2]=5.5;
            value[3]=14;
            value[4]=28;
            value[5]=55;
        }

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        color = new int[levelReagen];
        dist = new double[levelReagen];
        for(int i=0;i<levelReagen;i++)
            color[i]=MAX;
        for(int j=0;j<levelReagen-1;j++)
            dist[j]=MAX;
        btnSelect1 = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect1.setOnClickListener(new OnClickListener()  {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_image, menu);
        return true;
    }

    class submitResult extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TakeImageActivity.this);
            pDialog.setMessage("Saving Result..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            // Building Parameters

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd"+" "+"HH:mm:ss").format(new Date());

            params.add(new BasicNameValuePair("id_user", Integer.toString(urian_id_user)));
            params.add(new BasicNameValuePair("id_reagent", Integer.toString(id_reagent)));
            params.add(new BasicNameValuePair("id_dipstick", Integer.toString(id_dipstick)));
            params.add(new BasicNameValuePair("value", Double.toString(degree)));
            Log.d("Nilai H : ", ""+colorreagenHSV.H);
            Log.d("Nilai S : ", ""+colorreagenHSV.S);
            Log.d("Nilai V : ", ""+colorreagenHSV.V);

            Log.d("Nilai L : ", ""+colorreagen.LValue);
            Log.d("Nilai a : ", ""+colorreagen.aValue);
            Log.d("Nilai B : ", ""+colorreagen.bValue);
            if(id_reagent == 5 || id_reagent == 7 || id_reagent == 8)
            {
                params.add(new BasicNameValuePair("CieL", Float.toString(colorreagenHSV.H)));
                params.add(new BasicNameValuePair("Ciea", Float.toString(colorreagenHSV.S)));
                params.add(new BasicNameValuePair("Cieb", Float.toString(colorreagenHSV.V)));
            }
            else
            {
                params.add(new BasicNameValuePair("CieL", Float.toString(colorreagen.LValue)));
                params.add(new BasicNameValuePair("Ciea", Float.toString(colorreagen.aValue)));
                params.add(new BasicNameValuePair("Cieb", Float.toString(colorreagen.bValue)));
            }

            params.add(new BasicNameValuePair("colorR", Integer.toString(Color.red(color[0]))));
            params.add(new BasicNameValuePair("colorG", Integer.toString(Color.green(color[0]))));
            params.add(new BasicNameValuePair("colorB", Integer.toString(Color.blue(color[0]))));
            params.add(new BasicNameValuePair("status", status));
            params.add(new BasicNameValuePair("datenow", timeStamp));


            // getting JSON Object
            // Note that create product url accepts POST method
            json = jsonParser.makeHttpRequest(save_result, "GET", params);

            // check log cat fro response
            Log.d("Insert Lab Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), TakeImageActivity.class);
                    i.putExtra("id_user", Integer.toString(urian_id_user));
                    startActivity(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Result saved.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Oops! An error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    });
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
            // dismiss the dialog once done
            pDialog.dismiss();
            Intent in = new Intent(getApplicationContext(),
                    HomeActivity.class);
            in.putExtra("id_user", getIntent().getExtras().getInt("id_user"));
            startActivity(in);

        }

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TakeImageActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = timeStamp + ".jpg";

                    File urinDirectory = new File( Environment.getExternalStorageDirectory()+"/urin/");
                    urinDirectory.mkdirs();
                    File f = new File(urinDirectory,fileName);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));
                    startActivity(intent);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public int calculateMode(int v[], int n){
        int maxValue = -1;
        int maxCount = 0;
        for(int i = 0; i < n; i++) {
            int count = 0;
            for(int j = 0; j < n; j++) {
                if(v[j] == v[i]) {
                    count++;
                }
            }

            if(count > maxCount) {
                maxValue = v[i];
                maxCount = count;
            }
        }
        return maxValue;
    }


    public void getMode(Bitmap selectedBitmap) {
        int[] redBucket;
        redBucket = new int[2000000];
        int[] greenBucket;
        greenBucket = new int[2000000];
        int[] blueBucket;
        blueBucket = new int[2000000];
        int pixelCount = 0;

        for (int y = 0; y < selectedBitmap.getHeight(); y++) {
            for (int x = 0; x < selectedBitmap.getWidth(); x++) {
                int c = selectedBitmap.getPixel(x, y);
                redBucket[pixelCount] = Color.red(c);
                greenBucket[pixelCount] = Color.green(c);
                blueBucket[pixelCount] = Color.blue(c);
                pixelCount++;
            }
        }

        redMode = calculateMode(redBucket,pixelCount);
        greenMode = calculateMode(greenBucket, pixelCount);
        blueMode = calculateMode(blueBucket, pixelCount);
    }

    public double getColorDistLAB(int color1, int color2)
    {
        CieLAB labColor1 = new CieLAB(color2);
        CieLAB labColor2 = new CieLAB(color1);

        HSV hsvColor1 = new HSV(color2);
        HSV hsvColor2 = new HSV(color1);
        double distance = 0;


        if(id_reagent == 5)
        {
            distance = hsvColor1.V - hsvColor2.V;
        }
        if(id_reagent == 7)
        {
            distance = hsvColor1.H - hsvColor2.H;
        }
        if(id_reagent == 8)
        {
            distance = Math.sqrt(Math.pow(hsvColor1.H - hsvColor2.H, 2) +
                    Math.pow(hsvColor1.S - hsvColor2.S, 2) +
                    Math.pow(hsvColor1.V - hsvColor2.V, 2));
        }
        else
        {
            distance = Math.sqrt(Math.pow(labColor1.LValue - labColor2.LValue, 2) +
                    Math.pow(labColor1.aValue - labColor2.aValue, 2) +
                    Math.pow(labColor1.bValue - labColor2.bValue, 2));
        }


        return distance;
    }

    public int getNearestIndex()
    {
        int index = MAX;
        double nearestDistance=MAX;
        for(int i=0;i<levelReagen-1;i++)
        {
            if(dist[i]<nearestDistance) {
                nearestDistance = dist[i];
                index=i;
            }
        }
        return index;
    }
    public int getSecondNearestIndex()
    {
        int index = MAX;
        double nearestDistance=MAX;
        for(int i=0;i<levelReagen-1;i++)
        {
            if(i==limitIndex)continue;
            if(dist[i]<nearestDistance) {
                nearestDistance = dist[i];
                index=i;
            }
        }
        return index;
    }

    public double getDegree(int nearestIndex,int secondNearestIndex)
    {
        int top = nearestIndex > secondNearestIndex ? nearestIndex : secondNearestIndex;
        int bottom = nearestIndex < secondNearestIndex ? nearestIndex : secondNearestIndex;
        value1=value[bottom-1];
        value2=value[top-1];

        double distanceBotReg = getColorDistLAB(color[bottom], color[0]);
        double distanceTopReg = getColorDistLAB(color[top], color[0]);
        double distanceBotTop = getColorDistLAB(color[bottom], color[top]);
        double distanceTopX = (Math.pow(distanceBotReg,2)-Math.pow(distanceBotTop,2)-Math.pow(distanceTopReg,2))/(-2*distanceBotTop);
        double distanceBotX = distanceBotTop - distanceTopX;
        double reagentValue = ((distanceBotX/distanceBotTop)*(value2-value1))+0;
        //Toast.makeText(this,Double.toString(distanceBotReg)+" , " + Double.toString(distanceTopReg)+" , " +Double.toString(distanceBotTop)+" , " +Double.toString(distanceTopX),Toast.LENGTH_LONG).show();
        if(distanceBotReg>distanceBotTop&&distanceBotReg<distanceTopReg)
            return value1;
        else {
            if (distanceBotReg > distanceBotTop && distanceBotReg > distanceTopReg)
                return value2;
            else{
                if(distanceTopReg>distanceBotTop&&distanceBotReg<distanceBotTop)
                    return value1;
                else
                    return reagentValue;}}
    }

    public void runCropImage(String path) {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, path);
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);//change ration here via intent
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.RETURN_DATA,true);
        startActivityForResult(intent, PIC_CROP);//final static int 1
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri selectedImage = data.getData();
                //String[] filePath = {MediaStore.Images.Media.DATA};
                //Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                //c.moveToFirst();
                //c.close();
                File imageFile = new File(getRealPathFromURI(selectedImage));
                runCropImage(imageFile.getPath());
                //performCrop(selectedImage);

            }

            else if (requestCode == PIC_CROP) {
                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    getMode(selectedBitmap);
                    //retrieve a reference to the ImageView
                    //
                    //
                    int width = 50;
                    int height = 50;
                    Bitmap resizedbitmap = Bitmap.createScaledBitmap(selectedBitmap, width, height, true);
                    int index = 0;
                    int index2= 0;
                    degree = 0;
                    txtWelcome.setText("Please input level " + " " + Integer.toString(counter) + " " + " image");
                    if (color[counter] == MAX && counter == 0) {
                        color[counter] = Color.rgb(redMode, greenMode, blueMode);
                        CheckBox check1 = (CheckBox) findViewById(R.id.checkBox);
                        check1.setChecked(true);
                        ImageView picView1 = (ImageView) findViewById(R.id.ivImage);
                        picView1.setImageBitmap(resizedbitmap);
                        btnSelect1.setEnabled(false);
                        btnSelecto[counter].setEnabled(true);
                        btnSelecto[counter].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectImage();
                            }
                        });
                        counter++;
                    } else {
                        if (color[counter] == MAX) {
                            color[counter] = Color.rgb(redMode, greenMode, blueMode);
                            dist[counter - 1] = getColorDistLAB(color[0], color[counter]);
                            if (counter <= levelReagen - 2) {
                                btnSelecto[counter].setEnabled(true);
                                btnSelecto[counter].setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        selectImage();
                                    }
                                });
                            }
                            btnSelecto[counter - 1].setEnabled(false);
                            picView[counter-1].setImageBitmap(resizedbitmap);
                            check[counter - 1].setChecked(true);
                            counter++;
                        }
                    }
                    if (color[levelReagen - 1] != MAX) {
                        txtValue4.setVisibility(View.VISIBLE);
                        txtStatus.setVisibility(View.VISIBLE);
                        txtWelcome.setText("Showing Result . . .");
                        index = getNearestIndex();
                        limitIndex = index;
                        index2 = getSecondNearestIndex();
                        degree = getDegree(index + 1, index2 + 1);
                        colorreagen = new CieLAB(color[0]);
                        if (id_reagent != 4) {
                            if (index == 0) {status =
                                    "normal";txtStatus.setTextColor(Color.parseColor("#456612"));}
                            else if (index == 1) {status =
                                    "waspada";txtStatus.setTextColor(Color.parseColor("#e5a712"));}
                            else {status = "bahaya";txtStatus.setTextColor(Color.parseColor("#c50c0c"));}
                        } else {
                            if (index == 0) {status =
                                    "normal";txtStatus.setTextColor(Color.parseColor("#456612"));}
                            else {status = "bahaya";txtStatus.setTextColor(Color.parseColor("#c50c0c"));}
                        }
                        txtStatus.setText(status);
                        btnSave = (Button) findViewById(R.id.btnSaveResult);
                        btnSave.setVisibility(View.VISIBLE);
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new submitResult().execute();
                            }
                        });
                    }
                    txtValue4 = (TextView) findViewById(R.id.txtDist1);
                    txtValue4.setText(Double.toString(degree));
                }
            }
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
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
