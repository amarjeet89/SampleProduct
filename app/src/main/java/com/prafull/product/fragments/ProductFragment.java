package com.prafull.product.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.activity.ProductDetailsActivity;
import com.prafull.product.adapter.ProductListAdapter;
import com.prafull.product.pojo.Product;
import com.prafull.product.pojo.ProductImage;
import com.prafull.product.pulltorefresh.IonRefreshListener;
import com.prafull.product.pulltorefresh.PullToUpdateListView;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SHUBHANSU on 8/2/2015.
 */

public class ProductFragment extends Fragment implements AdapterView.OnItemClickListener, TextView.OnEditorActionListener {
    private ProgressDialog loadingProgress;
    private PullToUpdateListView productListView;
    private ArrayList<Product> productData;
    ArrayList<ArrayList<String>> images = new ArrayList<>();
    private boolean scrollDown = false;
    private EditText searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_product_list, null);
        initializeControls(view);
        return view;
    }

    private void initializeControls(View view) {
        loadingProgress = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
        productListView = (PullToUpdateListView) view.findViewById(R.id.product_listview);
        productListView.setPullMode(PullToUpdateListView.MODE.UP_AND_DOWN);
        productListView.setAutoLoad(false, 10);
        productListView.setPullMessageColor(Color.BLUE);
        productListView.setLoadingMessage("Loading Message");
        productListView.setPullRotateImage(getResources().getDrawable(R.drawable.loader));
        productListView.setOnItemClickListener(this);
        productListView.setOnRefreshListener(new IonRefreshListener() {
            @Override
            public void onRefreshUp() {
                //your code
                System.out.println("up");
                //Toast.makeText(getApplicationContext(),"refresh up",Toast.LENGTH_SHORT).show();
                loadProductListonScrollUp();
                productListView.onRefreshUpComplete();
            }

            @Override
            public void onRefeshDown() {
                //your cod
                System.out.println("down");
                //   Toast.makeText(getApplicationContext(),"refresh down",Toast.LENGTH_SHORT).show();
                loadProductListonScrollDown();
                productListView.onRefreshDownComplete(null);
            }

        });
        productListView.setOnItemClickListener(this);
        loadProductList();
        searchText = (EditText) view.findViewById(R.id.search_item);
        searchText.setOnEditorActionListener(this);

    }

    private void loadProductListonScrollDown() {
        int lastIndex = productData.size();
        if (lastIndex > 0) {
            String userId = productData.get(lastIndex - 1).getUserId();
            String token = ProductPreferences.getInstance(getActivity()).getAccessToken();
            String sellerListUrl = getString(R.string.base_url) + getString(R.string.seller_Url) + "?token=" + token + "&_id=" + userId;
            System.out.println("sellerListUrl : " + sellerListUrl);
            scrollDown = true;
            new BaseSync(productLoadonScrollListener, sellerListUrl, null, CommonUtil.HTTP_GET).execute();

        }
    }

    private void loadProductListonScrollUp() {
        int lastIndex = productData.size();
        String userId = productData.get(lastIndex - 1).getUserId();
        String token = ProductPreferences.getInstance(getActivity()).getAccessToken();
        String sellerListUrl = getString(R.string.base_url) + getString(R.string.seller_Url) + "?token=" + token + "&_id=" + userId + getString(R.string.list_pull_up);
        System.out.println("sellerListUrl : " + sellerListUrl);
        new BaseSync(productLoadonScrollListener, sellerListUrl, null, CommonUtil.HTTP_GET).execute();

    }

    private void loadProductList() {
        String token = ProductPreferences.getInstance(getActivity()).getAccessToken();
        String sellerListUrl = getString(R.string.base_url) + getString(R.string.seller_Url) + "?token=" + token + "&limit=" + getString(R.string.item_limit);
        System.out.println("sellerListUrl : " + sellerListUrl);
        loadingProgress.show();
        new BaseSync(productLoadListener, sellerListUrl, null, CommonUtil.HTTP_GET).execute();
    }




    BaseSync.OnTaskCompleted productLoadonScrollListener = new BaseSync.OnTaskCompleted() {

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
                            if (scrollDown) {
                                productData.addAll(parseProductData(obj));
                                scrollDown = false;
                            } else
                                productData = parseProductData(obj);

                            ProductListAdapter productDataAdapter = new ProductListAdapter(getActivity(), productData);
                            if (productListView != null)
                                productListView.setAdapter(productDataAdapter);
                        } else {
                            Toast.makeText(getActivity(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        public void onTaskFailure(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            if(searchText.getVisibility()==View.GONE){
                searchText.setVisibility(View.VISIBLE);
            }else{
                searchText.setVisibility(View.GONE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    BaseSync.OnTaskCompleted productLoadListener = new BaseSync.OnTaskCompleted() {

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
                            productData = parseProductData(obj);
                            ProductListAdapter productDataAdapter = new ProductListAdapter(getActivity(), productData);
                            if (productListView != null)
                                productListView.setAdapter(productDataAdapter);
                        } else {
                            Toast.makeText(getActivity(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }


        @Override
        public void onTaskFailure(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    private ArrayList<Product> parseProductData(JSONObject obj) {
        ArrayList<Product> productData = null;
        try {
            if (obj.has("data")) {
                JSONObject dataObj = obj.getJSONObject("data");
                if (dataObj.has("data")) {
                    productData = new ArrayList<Product>();
                    JSONArray dataArray = dataObj.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject itemData = dataArray.getJSONObject(i);
                        JSONArray productArray = itemData.getJSONArray("item_pics");
                        ArrayList<ProductImage> productImages = new ArrayList<ProductImage>();
                        ArrayList<String> loc_images = new ArrayList<>();
                        for (int j = 0; j < productArray.length(); j++) {
                            productImages.add(new ProductImage(productArray.getString(j)));
                            loc_images.add(productArray.getString(j));
                        }
                        images.add(loc_images);
                        double placeLatitude = 0.0;
                        double placeLogitude = 0.0;
                        if (itemData.has("location")) {
                            if (itemData.getJSONArray("location").length() > 0) {
                                placeLatitude = itemData.getJSONArray("location").getDouble(0);
                                placeLogitude = itemData.getJSONArray("location").getDouble(1);
                            }

                        }
                        productData.add(new Product(itemData.getString("title"), itemData.getString("user_id"),
                                itemData.getString("seller_address"), itemData.getJSONArray("item_pics").toString(),
                                itemData.getString("description"), itemData.getJSONArray("contacts").getString(0),
                                placeLatitude, placeLogitude));

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productData;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle b=new Bundle();
        b.putString(CommonUtil.USER_ID, productData.get(i).getUserId());
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtras(b);
        startActivity(intent);

    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean handled = false;
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            String searchText=textView.getText().toString();
            searchText=searchText.trim();
            if(searchText.length()>0){
                searchProductItem(searchText);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.seach_text),Toast.LENGTH_SHORT).show();
            }

            handled = true;
        }
        return handled;
    }

    private void searchProductItem(String searchText) {
        String token = ProductPreferences.getInstance(getActivity().getApplicationContext()).getAccessToken();
        String sellerListUrl = getString(R.string.base_url) + getString(R.string.seller_Url) + "?token=" + token+"&search="+searchText;
        System.out.println("sellerListUrl : " + sellerListUrl);
        loadingProgress = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Searching..");
        loadingProgress.show();
        new BaseSync(productLoadListener, sellerListUrl, null, CommonUtil.HTTP_GET).execute();
    }

}