package com.example.shwetha.equipmentinventory4;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity{


    //JSON Array URL
    private static String url = "http://corvette.ischool.utexas.edu/~smallya/list_inventory.php";

    //JSON Node names
    //private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    public JSONArray docs = null;
    public View.OnClickListener listener;
    private Intent intent;

   /* public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.make_inventory_button){

            GetFetcher fetcher = new GetFetcher();
            fetcher.execute();

        }

        else if(v.getId()==R.id.list_inventory_button){


            Intent i = new Intent(getApplicationContext(), MakeInventoryActivity.class);
            startActivity(i);



        }

    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Button make_inventory = (Button) findViewById(R.id.make_inventory_button);
        Button list_inventory = (Button) findViewById(R.id.list_inventory_button);


        //make_inventory.setOnClickListener(this);
        //list_inventory.setOnClickListener(this);

        listener = new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //check which view was clicked
                if(v.getId() == R.id.make_inventory_button){

                    Intent i = new Intent(getApplicationContext(), MakeInventoryActivity.class);
                    startActivity(i);



                }else if(v.getId() == R.id.list_inventory_button){

                    GetFetcher fetcher = new GetFetcher();
                    fetcher.execute();

                }


            }

        };



        /*make_inventory.setOnClickListener(new View.OnClickListener() {





            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MakeInventoryActivity.class);
                startActivity(i);

            }
        });

        //Button make_inventory = (Button) findViewById(R.id.scan_button);
        list_inventory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GetFetcher fetcher = new GetFetcher();
                fetcher.execute();

            }
        });*/


    }


    public void listInventory(View view)
    {

        GetFetcher fetcher = new GetFetcher();
        intent = new Intent(this, ListInventoryActivity.class);
        fetcher.execute();


        Log.d("TAG1", "Before Start new activity");




    }


    public void makeInventory(View view)
    {

        Intent i = new Intent(this, MakeInventoryActivity.class);
        Log.d("TAG1","Before Start new activity");
        startActivity(i);
        Log.d("TAG1", "After Start new activity");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* public void startListInventoryActivity(View view) {
        Intent objIndent = new Intent(getApplicationContext(), ListInventoryActivity.class);
        startActivity(objIndent);
    } */

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


    /**
     * First Inner class: GetFetcher
     * Fetch inventory data in background thread
     */
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



            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                //Clear mItems_
                //mItems_ = null;

                if (statusLine.getStatusCode() == 200) {
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

                                item_name = items.getString(TAG_NAME);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else
                {



                }
            }catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return item_name;

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
            intent.putExtra("item_name",item_name);
            startActivity(intent);


        }
    }

}
