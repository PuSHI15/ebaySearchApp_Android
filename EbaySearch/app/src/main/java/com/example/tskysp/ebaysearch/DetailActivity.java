package com.example.tskysp.ebaysearch;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;


public class DetailActivity extends ActionBarActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentFromResult = getIntent();
        String json = intentFromResult.getStringExtra(ResultActivity.displayResultInList.jsonToPass);
        final String title = intentFromResult.getStringExtra(ResultActivity.displayResultInList.titleToPass);
        final String price = intentFromResult.getStringExtra(ResultActivity.displayResultInList.priceToPass);
        Log.v("json", json);
        Log.v("title", title);
        Log.v("price,", price);

        JSONObject basic = new JSONObject();
        JSONObject seller = new JSONObject();
        JSONObject shipping = new JSONObject();

        try{
            basic = new JSONObject(json).getJSONObject("basicInfo");
            seller = new JSONObject(json).getJSONObject("sellerInfo");
            shipping = new JSONObject(json).getJSONObject("shippingInfo");
        }catch(Throwable t){
            Log.v("error", t.toString());
        }

        String superSizeURL = "";
        String imageURL = "";
        String location = "";
        String topRated = "";
        String itemURL = "";
        String categoryName = "";
        String condition = "";
        String format = "";
        String userName = "";
        String feedbackScore = "";
        String positiveFeedback = "";
        String feedbackRating = "";
        String topRatedSeller = "";
        String store = "";
        String storeURL= "";
        String shippingType = "";
        String handling = "";
        String shippingLocation ="";
        String expeditedShipping = "";
        String oneday = "";
        String returnAccepted = "";

        try{
            superSizeURL = basic.getString("pictureURLSuperSize");
            imageURL = basic.getString("galleryURL");
            location = basic.getString("location");
            topRated = basic.getString("topRatedListing");
            itemURL = basic.getString("viewItemURL");
            categoryName = basic.getString("categoryName");
            condition = basic.getString("conditionDisplayName");
            format = basic.getString("listingType");
            userName = seller.getString("sellerUserName");
            feedbackScore = seller.getString("feedbackScore");
            positiveFeedback = seller.getString("positiveFeedbackPercent");
            feedbackRating = seller.getString("feedbackRatingStar");
            topRatedSeller = seller.getString("topRatedSeller");
            store = seller.getString("sellerStoreName");
            storeURL = seller.getString("sellerStoreURL");
            shippingType = shipping.getString("shippingType");
            handling = shipping.getString("handlingTime");
            shippingLocation = shipping.getString("shipToLocations");
            expeditedShipping = shipping.getString("expeditedShipping");
            oneday = shipping.getString("oneDayShippingAvailable");
            returnAccepted = shipping.getString("returnsAccepted");
        }catch (Throwable T){

        }

        ImageView detailImage = (ImageView)findViewById(R.id.imageDisplay);

        if(superSizeURL.matches("")){
            new getImageFromURL(detailImage).execute(imageURL);
        }else{
            new getImageFromURL(detailImage).execute(superSizeURL);
        }

        final String currentSuperSizeUrl = superSizeURL;

        TextView displayTitle = (TextView)findViewById(R.id.titleDisplay);
        TextView displayPrice = (TextView)findViewById(R.id.priceDisplay);
        TextView displayLocation = (TextView)findViewById(R.id.locationDisplay);

        displayTitle.setText(title);
        displayPrice.setText(price);
        displayLocation.setText(location);

        final String currentLocation = location;



        if(topRated.matches("true")){
            ImageView topimage = (ImageView)findViewById(R.id.topRated);
            topimage.setImageResource(R.drawable.itemtoprated);
        }
        ImageView fb = (ImageView)findViewById(R.id.facebook);
        fb.setImageResource(R.drawable.fb);

        Button buyitnow = (Button)findViewById(R.id.buynow);
        final Button basicInfo = (Button)findViewById(R.id.basic);
        final Button sellerInfo = (Button)findViewById(R.id.seller);
        final Button shippingInfo = (Button)findViewById(R.id.shipping);

        sellerInfo.setBackgroundColor(Color.WHITE);
        shippingInfo.setBackgroundColor(Color.WHITE);
        basicInfo.setBackgroundColor(Color.WHITE);

        final TextView title1 = (TextView)findViewById(R.id.title1);
        final TextView title2 = (TextView)findViewById(R.id.title2);
        final TextView title3 = (TextView)findViewById(R.id.title3);
        final TextView title4 = (TextView)findViewById(R.id.title4);
        final TextView title5 = (TextView)findViewById(R.id.title5);
        final TextView title6 = (TextView)findViewById(R.id.title6);

        final TextView detail1 = (TextView)findViewById(R.id.detail1);
        final TextView detail2 = (TextView)findViewById(R.id.detail2);
        final TextView detail3 = (TextView)findViewById(R.id.detail3);
        final TextView detail4 = (TextView)findViewById(R.id.detail4);
        final TextView detail5 = (TextView)findViewById(R.id.detail5);

        final ImageView image1 = (ImageView)findViewById(R.id.image1);
        final ImageView image2 = (ImageView)findViewById(R.id.image2);
        final ImageView image3 = (ImageView)findViewById(R.id.image3);


        final String currentURL = itemURL;
        final String currentCategory = categoryName;
        final String currentCondition = condition;
        final String currentFormat = format;
        final String currentUserName = userName;
        final String currentScore = feedbackScore;
        final String currentPositive = positiveFeedback;
        final String currentRating = feedbackRating;
        final String currentTop = topRatedSeller;
        final String currentStore = store;
        final String currentStoreURL = storeURL;
        final String currentType = shippingType;
        final String currentHandle = handling;
        final String currentShipping = shippingLocation;
        final String currentExp = expeditedShipping;
        final String currentOne = oneday;
        final String currentReturn = returnAccepted;

        buyitnow.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentURL));
                startActivity(browserIntent);
            }
        });

        basicInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                sellerInfo.setBackgroundColor(Color.WHITE);
                shippingInfo.setBackgroundColor(Color.WHITE);
                basicInfo.setBackgroundColor(Color.BLUE);

                title4.setVisibility(View.GONE);
                title5.setVisibility(View.GONE);
                title6.setVisibility(View.GONE);
                detail4.setVisibility(View.GONE);
                detail5.setVisibility(View.GONE);
                image1.setVisibility(View.GONE);
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);



                title1.setText("Category Name");
                title1.setVisibility(View.VISIBLE);
                detail1.setText(currentCategory);
                detail1.setVisibility(View.VISIBLE);
                title2.setText("Condition");
                title2.setVisibility(View.VISIBLE);
                detail2.setText(currentCondition);
                detail2.setVisibility(View.VISIBLE);
                title3.setText("Buying Formats");
                title3.setVisibility(View.VISIBLE);
                detail3.setText(currentFormat);
                detail3.setVisibility(View.VISIBLE);
            }
        });

        sellerInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                sellerInfo.setBackgroundColor(Color.BLUE);
                shippingInfo.setBackgroundColor(Color.WHITE);
                basicInfo.setBackgroundColor(Color.WHITE);

                image1.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);

                title1.setText("User Name");
                title1.setVisibility(View.VISIBLE);
                detail1.setText(currentUserName);
                detail1.setVisibility(View.VISIBLE);
                title2.setText("Feedback Score");
                title2.setVisibility(View.VISIBLE);
                detail2.setText(currentScore);
                detail2.setVisibility(View.VISIBLE);
                title3.setText("Positive Feedback");
                title3.setVisibility(View.VISIBLE);
                detail3.setText(currentPositive);
                detail3.setVisibility(View.VISIBLE);
                title4.setText("Feedback Rating");
                title4.setVisibility(View.VISIBLE);
                detail4.setText(currentRating);
                detail4.setVisibility(View.VISIBLE);
                title5.setText("Top Rated");
                title5.setVisibility(View.VISIBLE);
                if(currentTop.matches("true")){
                    image2.setImageResource(R.drawable.yes);
                    image2.setVisibility(View.VISIBLE);
                }else{
                    image2.setImageResource(R.drawable.no);
                    image2.setVisibility(View.VISIBLE);
                }
                title6.setText("Store");
                title6.setVisibility(View.VISIBLE);
                if(currentStore.matches("")){
                    detail5.setText("N/A");
                    detail5.setVisibility(View.VISIBLE);
                }else{
                    detail5.setText(currentStore);
                    detail5.setVisibility(View.VISIBLE);
                    detail5.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentStoreURL));
                            startActivity(browserIntent);
                        }
                    });
                }

            }
        });

        shippingInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                sellerInfo.setBackgroundColor(Color.WHITE);
                shippingInfo.setBackgroundColor(Color.BLUE);
                basicInfo.setBackgroundColor(Color.WHITE);

                detail4.setVisibility(View.GONE);
                detail5.setVisibility(View.GONE);

                title1.setText("Shipping Type");
                title1.setVisibility(View.VISIBLE);
                detail1.setText(currentType);
                detail1.setVisibility(View.VISIBLE);
                title2.setText("Handling Time");
                title2.setVisibility(View.VISIBLE);
                detail2.setText(currentHandle+" day(s)");
                detail2.setVisibility(View.VISIBLE);
                title3.setText("Shipping Locations");
                title3.setVisibility(View.VISIBLE);
                detail3.setText(currentShipping);
                detail3.setVisibility(View.VISIBLE);
                title4.setText("Expedited Shipping");
                title4.setVisibility(View.VISIBLE);
                if(currentExp.matches("true")){
                    image1.setImageResource(R.drawable.yes);
                    image1.setVisibility(View.VISIBLE);
                }else{
                    image1.setImageResource(R.drawable.no);
                    image1.setVisibility(View.VISIBLE);
                }

                title5.setText("One Day Shipping");
                title5.setVisibility(View.VISIBLE);
                if(currentOne.matches("true")){
                    image2.setImageResource(R.drawable.yes);
                    image2.setVisibility(View.VISIBLE);
                }else{
                    image2.setImageResource(R.drawable.no);
                    image2.setVisibility(View.VISIBLE);
                }
                title6.setText("Return Accepted");
                title6.setVisibility(View.VISIBLE);
                if(currentReturn.matches("true")){
                    image3.setImageResource(R.drawable.yes);
                    image3.setVisibility(View.VISIBLE);
                }else{
                    image3.setImageResource(R.drawable.no);
                    image3.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageView fbShare = (ImageView)findViewById(R.id.facebook);
        fbShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                FacebookSdk.sdkInitialize(getApplicationContext());
                callbackManager = CallbackManager.Factory.create();
                shareDialog = new ShareDialog(DetailActivity.this);


                Log.v("alert", "show dialog");


                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(currentURL))
                            .setImageUrl(Uri.parse(currentSuperSizeUrl))
                            .setContentTitle(title)
                            .setContentDescription(price + ", " + currentLocation)
                            .build();

                    shareDialog.show(content);
                }

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result){
                        if(result.getPostId() != null){
                            Toast toast = Toast.makeText(DetailActivity.this, "Posted Story, ID: "+result.getPostId(), Toast.LENGTH_SHORT);
                            toast.show();
                        }else{
                            onCancel();
                        }

                    }

                    @Override
                    public void onCancel(){
                        Toast toast = Toast.makeText(DetailActivity.this, "Post Cancelled", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onError(FacebookException exception){
                        Log.v("exception",exception.toString());
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
