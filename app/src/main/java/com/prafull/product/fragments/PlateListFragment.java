package com.prafull.product.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.activity.CustomPlate;
import com.prafull.product.adapter.PlateListViewAdapter;
import com.prafull.product.pojo.PlateItem;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amarjeet on 8/2/2015.
 */

public class PlateListFragment extends Fragment {
    private ProgressDialog loadingProgress;
    private ListView plateListView;
    private ArrayList<PlateItem> plateData;
    BaseSync.OnTaskCompleted plateLoadListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    try {
                        JSONObject obj = new JSONObject(str);
                        System.out.println(obj);
                        if (obj.getString("status").equals("success")) {
                            plateData = parsePlateData(obj);
                            PlateListViewAdapter productDataAdapter = new PlateListViewAdapter(getActivity().getApplicationContext(), plateData);
                            if (plateListView != null)
                                plateListView.setAdapter(productDataAdapter);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    plateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {
                            System.out.println("item clicked");
                            Toast.makeText(getActivity().getApplicationContext(), "position : " + i, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


        @Override
        public void onTaskFailure(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_plate_list, null);
        setHasOptionsMenu(true);
        plateListView=(ListView)view.findViewById(R.id.plate_list_view);
        loadPlateList();
        return view;
    }

    private void loadPlateList() {
        String token = ProductPreferences.getInstance(getActivity().getApplicationContext()).getAccessToken();
        String sellerId = ProductPreferences.getInstance(getActivity().getApplicationContext()).getUserId();
        String sellerListUrl = getString(R.string.base_url) + getString(R.string.get_plate_list_url) + "?token=" + token+"&seller_user_id="+sellerId;
        System.out.println("sellerListUrl : " + sellerListUrl);
        loadingProgress = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
        loadingProgress.show();
        new BaseSync(plateLoadListener, sellerListUrl, null, CommonUtil.HTTP_GET).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_plate_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            startActivity(new Intent(getActivity(),CustomPlate.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private ArrayList<PlateItem> parsePlateData(JSONObject obj) {
        ArrayList<PlateItem> plateDataArray = null;
        try {
            if (obj.has("data")) {
                JSONObject dataObj = obj.getJSONObject("data");
                if (dataObj.has("data")) {
                    plateDataArray = new ArrayList<>();
                    JSONArray dataArray = dataObj.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject itemData = dataArray.getJSONObject(i);

                        plateDataArray.add(new PlateItem(itemData.getString("title"), itemData.getString("cooking_time"),
                                itemData.getString("currency"),itemData.getString("_id"),itemData.getString("description"),
                                itemData.getString("dish_type"),itemData.getString("user_id"),itemData.getInt("quantity"),
                                itemData.getInt("price"),itemData.getString("rating"),itemData.getJSONArray("item_pics")
                        ));
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plateDataArray;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
