package com.example.tskysp.ebaysearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.*;
import org.json.JSONObject;

import java.io.*;


public class ebaySearch extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebay_search);
        Spinner spinner = (Spinner) findViewById(R.id.sort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinChoice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText key = (EditText)findViewById(R.id.keywords);
                EditText minTex = (EditText)findViewById(R.id.minPrice);
                EditText maxTex = (EditText)findViewById(R.id.maxPrice);
                float minNum = -1;
                float maxNum = -1;
                if(!minTex.getText().toString().matches("")){
                    minNum = Float.valueOf(minTex.getText().toString());
                }
                if(!maxTex.getText().toString().matches("")){
                    maxNum = Float.valueOf(maxTex.getText().toString());
                }
                Spinner sortby = (Spinner)findViewById(R.id.sort);
                TextView displayError = (TextView)findViewById(R.id.error);
                if(key.getText().toString().matches("")){
                    displayError.setText("Please Enter Keywords for Search!");
                    displayError.setTextColor(Color.RED);
                }else if((minNum!=-1)&&(maxNum!=-1)&&(minNum>maxNum)){
                    displayError.setText("Minimum Price should not exceed Maximum Price!");
                    displayError.setTextColor(Color.RED);
                }else{
                    displayError.setText("");
                    String urlToRequest = "http://csci571pushi-env.elasticbeanstalk.com/index.php?result=5&handle=&";
                    urlToRequest += "keywords="+key.getText().toString();
                    urlToRequest += "&minPrice="+(minNum==-1?"":minNum);
                    urlToRequest += "&maxPrice="+(maxNum==-1?"":maxNum);
                    String tosort = "";
                    if(sortby.getSelectedItemId()==0){
                        tosort = "BestMatch";
                    }else if(sortby.getSelectedItemId()==1){
                        tosort = "CurrentPriceHighest";
                    }else if(sortby.getSelectedItemId()==2){
                        tosort = "PricePlusShippingHighest";
                    }else if(sortby.getSelectedItemId()==3){
                        tosort = "PricePlusShippingLowest";
                    }
                    urlToRequest += "&sort="+tosort;
                    urlToRequest = urlToRequest.replaceAll(" ","%20");
                    Log.v("url", urlToRequest);


                    new jsonResponse(ebaySearch.this).execute(urlToRequest);

//                    String response = getResponse(urlToRequest);
//                    Log.v("resp", response);


                }
            }
        });
        Button clear = (Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText key = (EditText)findViewById(R.id.keywords);
                EditText minTex = (EditText)findViewById(R.id.minPrice);
                EditText maxTex = (EditText)findViewById(R.id.maxPrice);
                Spinner sortby = (Spinner)findViewById(R.id.sort);
                TextView displayError = (TextView)findViewById(R.id.error);

                key.setText("");
                minTex.setText("");
                maxTex.setText("");
                displayError.setText("");
                sortby.setSelection(0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ebay_search, menu);
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

class jsonResponse extends AsyncTask<String, Void, String>{

    ebaySearch myActivity = new ebaySearch();

    jsonResponse(ebaySearch a){
        this.myActivity = a;
    }

    public final static String EXTRA_MESSAGE = "com.example.tskysp.ebaysearch.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tskysp.ebaysearch.MESSAGE2";
    @Override
    protected String doInBackground(String... url){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url[0]);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
                content.close();
            } else {
                Log.e(ebaySearch.class.toString(),"Failedet JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    protected void onPostExecute(String jsonToParse){
        String result = "";
        try{
            JSONObject json = new JSONObject(jsonToParse);
            result = json.getString("ack");
        }catch (Throwable t){
            Log.e("parseerror", "Cannot be parsed");
        }
        if(result.matches("No results found")){
            TextView displayError = (TextView)myActivity.findViewById(R.id.error);
            displayError.setText("No results found");
            displayError.setTextColor(Color.RED);
        }else {
            Intent intent = new Intent(myActivity, ResultActivity.class);
            intent.putExtra(EXTRA_MESSAGE, jsonToParse);
            intent.putExtra(EXTRA_MESSAGE2, ((EditText)myActivity.findViewById(R.id.keywords)).getText().toString());
            myActivity.startActivity(intent);
        }
    }
}
