package com.prafull.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.adapter.TrackAdapter;
import com.prafull.product.pojo.Track;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SHUBHANSU on 9/5/2015.
 */
public class OrderTrackingActivity extends AppCompatActivity {
    private TextView timeMessage;
    private ListView trackListView;
    private Button cancalOrder;
    private ArrayList<Track> trackList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        trackList=new ArrayList<Track>();
        Intent intent = getIntent();
        String message = "" + intent.getStringExtra(CommonUtil.MESSAGE);
        timeMessage = (TextView) findViewById(R.id.time);
        trackListView = (ListView) findViewById(R.id.track_list);
        cancalOrder = (Button) findViewById(R.id.cancel_order);
        timeMessage.setText(message);
        getOrderTrackData();
    }

    private void getOrderTrackData() {
        String profileURL = getString(R.string.base_url) + "/feed" + "?"
                + getString(R.string.token_Url) + ProductPreferences.getInstance(getApplicationContext()).getAccessToken()
                + "&uid=" + ProductPreferences.getInstance(getApplicationContext()).getUserId();

        new BaseSync(redrawListener, profileURL, null, CommonUtil.HTTP_GET).execute();
    }

    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(str);
                        if (obj.getString("status").equals("success")) {
                            JSONObject trackObject = obj.getJSONObject("data");
                            JSONArray relatedStories = trackObject.getJSONArray("data");
                            for (int i = 0; i < relatedStories.length(); i++) {
                                JSONObject relatedStory = (JSONObject) relatedStories.get(i);
                                trackList.add(new Track(relatedStory.getString("content"), relatedStory.getString("titleNoFormatting"), relatedStory.getString("unescapedUrl")));
                            }

                            TrackAdapter trackAdapter = new TrackAdapter(OrderTrackingActivity.this,trackList);
                            trackListView.setAdapter(trackAdapter);
                            /*JSONObject sellerObject = obj.getJSONObject("data").getJSONObject("data");
                            if (sellerObject.has("_id")) {
                                String sellerId = sellerObject.getString("_id");
                                ProductPreferences.getInstance(getActivity()).setSellerId(sellerId);
                                String sellerUserId = sellerObject.getString("user_id");
                                ProductPreferences.getInstance(getActivity()).setSellerUserId(sellerUserId);
                                String title = sellerObject.getString("title");
                                JSONArray itemPics = sellerObject.getJSONArray("item_pics");
                                //JSONArray contacts = sellerObject.getJSONArray("contacts");
                                JSONObject sellerAddressObject = sellerObject.getJSONObject("seller_address");
                                String description = sellerObject.getString("description");
                                String areaValue = sellerAddressObject.getString("area");
                                String cityValue = sellerAddressObject.getString("city");
                                String stateValue = "" + sellerAddressObject.getString("state");
                                String pinValue = sellerAddressObject.getString("pin");
                                String addressValue = sellerAddressObject.getString("house_no");
                                name.setText(title);
                                address.setText(addressValue);
                                aboutMe.setText(description);
                                area.setText(areaValue);
                                city.setText(cityValue);
                                state.setText(stateValue);
                                pincode.setText(pinValue);
                                for (int i = 0; i < itemPics.length(); i++) {
                                    imageList.add(itemPics.getString(i));
                                }

                            } else {


                            }*/
                        } else {

                            Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        @Override
        public void onTaskFailure(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

}
