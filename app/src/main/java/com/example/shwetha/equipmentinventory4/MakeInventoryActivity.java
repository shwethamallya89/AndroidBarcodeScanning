package com.example.shwetha.equipmentinventory4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class MakeInventoryActivity extends AppCompatActivity implements View.OnClickListener{

    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    public String url = "http://corvette.ischool.utexas.edu/~smallya/make_inventory.php";
    public String input_name;
    public StatusLine statusLine;


    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.scan_button){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_inventory);
        //new IntentIntegrator.initiateScan(this);

        //if (savedInstanceState == null) {
          //  getSupportFragmentManager().beginTransaction()
            //        .add(R.id.container, new PlaceholderFragment()).commit();
        //}
        Log.d("TAG1", "New activity started");

       // Intent i = getIntent();

        scanBtn = (Button)findViewById(R.id.scan_button);

        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        //if(scanBtn == null)
          //  Log.d("TAG1", "**********************  SCAN Button is NULL  ************************");
        scanBtn.setOnClickListener(this);

    }

    public void makeInventory(View view)
    {

        EditText input_text = (EditText)findViewById(R.id.editText);

        input_name = input_text.getText().toString().trim();

        if(input_name == "")
        {
            CharSequence no_text = "No input entered";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(MakeInventoryActivity.this, no_text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
        else
        {
            GetFetcher fetcher = new GetFetcher();
            fetcher.execute();
            Log.d("TAG1", "Input entered");

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class GetFetcher extends AsyncTask<Void, Void, String> {
        /**
         * Fill static variable mItems_ in DataManager with info from DPLA and tags from local DB
         * @return returnStatus
         */

        String item_name = null;
        protected String doInBackground(Void... params) {
           /* DataManager manager = DataManager.getInstance();
            String returnStatus = manager.getSearchResultsWithTags(mKeyword_,StartActivity.this) ;
            return returnStatus; */
            String GET_URL = url+"?name="+input_name;

            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(GET_URL);



            try {

                //String input = "string_sample";
                //String GET_URL = String.format(url,input);


                HttpResponse response = client.execute(get);
                statusLine = response.getStatusLine();
                Log.d("TAG1",response.toString());

                /*if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(content, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    String contentString;
                    while ((contentString = streamReader.readLine()) != null)
                        responseStrBuilder.append(contentString);

                    String json_string = responseStrBuilder.toString();

                    if (json_string != null) {
                        try {
                            JSONArray inventoryArray = new JSONArray(json_string);
                            // Getting JSON Array node
                            // docs = jsonObj.getJSONArray(TAG_DOCS);


                            for (int iter = 0; iter < inventoryArray.length(); iter++) {

                                JSONObject items = inventoryArray.getJSONObject(iter);

                                //item_name = items.getString(TAG_NAME);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else
                {



                }*/
            }catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return input_name;

        }

        /**
         * Display searching results in mListView
         * Display error message in toast
         * @param result : returnStatus returned by doInBackground
         */
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //final TextView answerlabel = (TextView) findViewById(R.id.text);
            //String answer = "Yes";
            // item_name = "yes";
            //answerlabel.setText(item_name);
            //intent.putExtra("item_name",item_name);
            //startActivity(intent);

            if (statusLine.getStatusCode() == 200) {

                CharSequence success_text = "Input successful";
                int duration_new = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(MakeInventoryActivity.this, success_text, duration_new);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            }

        }
    }

}
