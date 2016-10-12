package com.example.tskysp.ebaysearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;


public class ResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intentReceived = getIntent();
        String jsonReceived = intentReceived.getStringExtra(jsonResponse.EXTRA_MESSAGE);
        String keywords = intentReceived.getStringExtra(jsonResponse.EXTRA_MESSAGE2);

        TextView searchKeywords = (TextView)findViewById(R.id.searchKeywords);
        searchKeywords.setText("Results for \'"+keywords+"\'");

        JSONObject jsonConverted = null;
        try{
            jsonConverted = new JSONObject(jsonReceived);
        }catch(Throwable t){
            Log.v("hint",t.toString());
        }

        Log.v("message", jsonReceived);

        displayResultInList itemList = new displayResultInList(jsonConverted, ResultActivity.this);

        ListView list = (ListView)findViewById(R.id.displayResult);

        list.setAdapter(itemList);



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    class displayResultInList extends BaseAdapter{

        JSONObject jsonToProcess = new JSONObject();
        ResultActivity ra = new ResultActivity();
        public final static String jsonToPass = "com.example.tskysp.ebaysearch.json";
        public final static String titleToPass = "com.example.tskysp.ebaysearch.title";
        public final static String priceToPass = "com.example.tskysp.ebaysearch.prie";

        displayResultInList(JSONObject pass, ResultActivity a){
            this.jsonToProcess = pass;
            this.ra = a;
        }



        @Override
        public int getCount(){
            return 5;
        }

        @Override
        public JSONArray getItem(int position){
            JSONArray jsonarray = null;
            try {
                jsonarray = jsonToProcess.getJSONArray("item" + position);
            }catch (Throwable t){
                Log.v("error", "No item in this position");
            }

            return jsonarray;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) ResultActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent,false);
            }

            TextView itemTitle = (TextView)convertView.findViewById(R.id.itemTitle);
            TextView itemPrice = (TextView)convertView.findViewById(R.id.itemPrice);
            ImageView itemImage = (ImageView)convertView.findViewById(R.id.itemImage);

            String title = "";
            String price = "";
            String imageURL="";
            String itemURL = "";
            JSONObject item = new JSONObject();
            try{
                item = jsonToProcess.getJSONObject("item" + position);
                title = (item.getJSONObject("basicInfo")).getString("title");
                price = "Price: $"+(item.getJSONObject("basicInfo")).getString("convertedCurrentPrice");
                if((item.getJSONObject("basicInfo").getString("shippingServiceCost") !="" && item.getJSONObject("basicInfo").getString("shippingServiceCost").matches("0"))
                ||item.getJSONObject("shippingInfo").getString("shippingType").matches("Free")||item.getJSONObject("basicInfo").getString("shippingServiceCost").matches("")){
                    price += " (FREE Shipping)";
                }else{
                    price +=" (+ $"+item.getJSONObject("basicInfo").getString("shippingServiceCost")+" Shipping)";
                }
                imageURL = (item.getJSONObject("basicInfo")).getString("galleryURL");
                itemURL = (item.getJSONObject("basicInfo")).getString("viewItemURL");
            }catch (Throwable t){
                Log.v("error", t.toString());
            }

            final String currentURL = itemURL;
            final String currentTitle = title;
            final String currentPrice = price;
            final JSONObject currentItem = item;


            itemTitle.setText(title);
            itemPrice.setText(price);
            new getImageFromURL(itemImage).execute(imageURL);
            Log.v("position", Integer.toString(position));
            Log.v("url", imageURL);

            itemImage.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentURL));
                    startActivity(browserIntent);
                }
            });

            itemTitle.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent detail = new Intent(ra,DetailActivity.class);

                    detail.putExtra(jsonToPass, currentItem.toString());
                    detail.putExtra(titleToPass, currentTitle);
                    detail.putExtra(priceToPass, currentPrice);
                    startActivity(detail);
                }
            });

            return convertView;
        }


    }



}


