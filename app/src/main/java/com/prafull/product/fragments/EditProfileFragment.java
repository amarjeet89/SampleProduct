package com.prafull.product.fragments;

/**
 * Created by SHUBHANSU on 8/2/2015.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.MediaUtils;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EditProfileFragment extends Fragment implements View.OnClickListener, ImagePickerFragment.DialogCallBack {
    private static final int RESULT_LOAD_IMG = 01;
    private static final int RESULT_CAPTURE_IMG = 02;
    private TextView name;
    private TextView area;
    private TextView mobileNo;
    private TextView address;
    private TextView state;
    private TextView aboutMe;
    private Button save;
    private Button pickImage;
    private ImagePickerFragment imagePicker;
    private FragmentManager fragmentManager;
    private Uri imageUri;
    private String mSelectImgPath;
    private TextView pincode;
    private TextView city;
    private ArrayList<String> imageList;
    private ArrayList<String> contactList;
    private ProgressDialog loadingProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_edit, null);
        initializeControls(view);
        return view;
    }

    private void initializeControls(View view) {
        imageList = new ArrayList<>();
        contactList = new ArrayList<>();
        name = (TextView) view.findViewById(R.id.name);
        mobileNo = (TextView) view.findViewById(R.id.mobile);
        area = (TextView) view.findViewById(R.id.area);
        city = (TextView) view.findViewById(R.id.city);
        address = (TextView) view.findViewById(R.id.address);
        state = (TextView) view.findViewById(R.id.state);
        pincode = (TextView) view.findViewById(R.id.pincode);
        aboutMe = (TextView) view.findViewById(R.id.about_me_text);
        pickImage = (Button) view.findViewById(R.id.pick_img);
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(this);
        pickImage.setOnClickListener(this);
        loadingProgress = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
        getProfileData();
        fragmentManager = getActivity().getFragmentManager();

    }

    private void getProfileData() {
        /*String profileURL = getString(R.string.base_url)+getString(R.string.user_url)+
                ProductPreferences.getInstance(getActivity()).getUserId()+"?"+getString(R.string.token_Url)+
                ProductPreferences.getInstance(getActivity()).getAccessToken();*/

        String profileURL = getString(R.string.base_url) + getString(R.string.seller_url_param) + "?"
                + getString(R.string.token_Url) + ProductPreferences.getInstance(getActivity()).getAccessToken()
                + "&uid=" + ProductPreferences.getInstance(getActivity()).getUserId();

        new BaseSync(redrawListener, profileURL, null, CommonUtil.HTTP_GET).execute();
    }

    private boolean update = false;
    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(str);
                        if (obj.getString("status").equals("success")) {
                            JSONObject sellerObject = obj.getJSONObject("data").getJSONObject("data");
                            if (sellerObject.has("_id")) {
                                update = true;
                                String sellerId = sellerObject.getString("_id");
                                ProductPreferences.getInstance(getActivity()).setSellerId(sellerId);
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
                                /*StringBuilder contact = new StringBuilder();
                                for (int i= 0; i<contacts.length();i++ ){
                                    contactList.add(contacts.getString(i));
                                    contact.append(contacts.getString(i)).append(",");
                                }
                                mobileNo.setText(contact.toString());*/

                            } else {
                                update = false;

                            }
                            loadingProgress.cancel();
                        } else {
                            loadingProgress.cancel();
                            Toast.makeText(getActivity(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        loadingProgress.cancel();
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
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    public void onClick(View v) {
        if (v.getId() == save.getId()) {
            boolean checkFieldError = getFieldError();
            if(!checkFieldError) {
                loadingProgress.show();
                if (update)
                    updateProfileData();
                else
                    addSellerData();
            }else{
                Toast.makeText(getActivity(),"All fields are required.",Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == pickImage.getId()) {
            Fragment fragment = this;
            imagePicker = ImagePickerFragment.newInstance("Mode", fragment);
            imagePicker.show(fragmentManager, "fragment_alert");
        }

    }

    private boolean getFieldError() {
        boolean status = true;
        if (!TextUtils.isEmpty(name.getText().toString().trim()) &&
                !TextUtils.isEmpty(aboutMe.getText().toString().trim()) &&
                !TextUtils.isEmpty(address.getText().toString().trim()) &&
                !TextUtils.isEmpty(area.getText().toString().trim()) &&
                !TextUtils.isEmpty(city.getText().toString().trim()) &&
                !TextUtils.isEmpty(state.getText().toString().trim()) &&
                !TextUtils.isEmpty(pincode.getText().toString().trim())
                )
            status =false;
         return status;
    }

    private void addSellerData() {
        //http://ec2-52-8-187-187.us-west-1.compute.amazonaws.com:9999/v1/seller
        String addSellerURL = getString(R.string.base_url) + getString(R.string.seller_url_param);

        JSONObject obj = new JSONObject();
        try {
            //obj.put("about_me", aboutMe.getText().toString());
            obj.put("token", ProductPreferences.getInstance(getActivity()).getAccessToken());
            obj.put("title", name.getText().toString());

            obj.put("location", new JSONArray());
            obj.put("description", aboutMe.getText().toString());
            JSONObject adddressJsonObject = new JSONObject();
            adddressJsonObject.put("house_no", address.getText().toString());
            adddressJsonObject.put("area", area.getText().toString());
            adddressJsonObject.put("city", city.getText().toString());
            adddressJsonObject.put("state", state.getText().toString());
            adddressJsonObject.put("pin", pincode.getText().toString());
            obj.put("seller_address", adddressJsonObject);
            JSONArray itemPics = new JSONArray();
            JSONArray contacts = new JSONArray();
            for (int i = 0; i < imageList.size(); i++) {
                itemPics.put(imageList.get(i));
            }

            obj.put("item_pics", itemPics);

            new BaseSync(addSelllerListener, addSellerURL, obj, CommonUtil.HTTP_POST).execute();
        } catch (JSONException e) {
            loadingProgress.cancel();
            e.printStackTrace();
        }
    }

    private void updateProfileData() {

        // http://ec2-52-8-187-187.us-west-1.compute.amazonaws.com:9999/v1/seller/55b7c83d69ccf13b319da8c0
        String updateSellerURL = getString(R.string.base_url) + getString(R.string.seller_url_param) + "/"
                + ProductPreferences.getInstance(getActivity()).getSellerId();

        JSONObject obj = new JSONObject();
        try {
            //obj.put("about_me", aboutMe.getText().toString());
            obj.put("token", ProductPreferences.getInstance(getActivity()).getAccessToken());
            obj.put("title", name.getText().toString());

            obj.put("location", new JSONArray());
            obj.put("description", aboutMe.getText().toString());
            JSONObject adddressJsonObject = new JSONObject();
            adddressJsonObject.put("house_no", address.getText().toString());
            adddressJsonObject.put("area", area.getText().toString());
            adddressJsonObject.put("city", city.getText().toString());
            adddressJsonObject.put("state", state.getText().toString());
            adddressJsonObject.put("pin", pincode.getText().toString());
            obj.put("seller_address", adddressJsonObject);
            JSONArray itemPics = new JSONArray();
            for (int i = 0; i < imageList.size(); i++) {
                itemPics.put(imageList.get(i));
            }
            obj.put("item_pics", itemPics);
            new BaseSync(updateListener, updateSellerURL, obj, CommonUtil.HTTP_PUT).execute();
        } catch (JSONException e) {
            loadingProgress.cancel();
            e.printStackTrace();
        }

    }

    BaseSync.OnTaskCompleted updateListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(str);
                        loadingProgress.cancel();
                        if (obj.getString("status").equals("success")) {
                            Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        loadingProgress.cancel();
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

    BaseSync.OnTaskCompleted addSelllerListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadingProgress.cancel();
                        JSONObject obj = new JSONObject(str);
                        if (obj.getString("status").equals("success")) {
                            Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        loadingProgress.cancel();
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

    @Override
    public void gallary() {
        System.out.println("Gallary");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void takeAPicture() {
        System.out.println("Take a Picture");
        String fileName = String.valueOf(System.currentTimeMillis());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, RESULT_CAPTURE_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && data != null) {
            System.out.println("Gallary Success");
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath,
                    null, null, null);
            cursor.moveToFirst();
            mSelectImgPath = cursor.getString(cursor
                    .getColumnIndex(filePath[0]));
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(mSelectImgPath);
            mSelectImgPath = MediaUtils.decodeFile(mSelectImgPath,
                    bitmap.getWidth() - 20, bitmap.getHeight() - 30);

            Bitmap bm = BitmapFactory.decodeFile(mSelectImgPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            uploadImage(encodedImage);

        } else if (requestCode == RESULT_CAPTURE_IMG && resultCode == Activity.RESULT_OK) {
            System.out.println("Take a picture Success");
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(imageUri, filePath,
                    null, null, null);
            cursor.moveToFirst();
            mSelectImgPath = cursor.getString(cursor
                    .getColumnIndex(filePath[0]));
            cursor.close();
            //mSelectImgPath = imageUri.getPath();
            Bitmap bm = BitmapFactory.decodeFile(mSelectImgPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 65, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            uploadImage(encodedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(String encodedImage) {
        loadingProgress.show();
        String imageUploadURL = getString(R.string.base_url) + "/upload";
        JSONObject obj = new JSONObject();
        try {
            obj.put("token", ProductPreferences.getInstance(getActivity()).getAccessToken());
            obj.put("image", encodedImage);
            new BaseSync(imageUploadListener, imageUploadURL, obj, CommonUtil.HTTP_POST).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    BaseSync.OnTaskCompleted imageUploadListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadingProgress.cancel();
                        JSONObject obj = new JSONObject(str);
                        if (obj.getString("status").equals("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            imageList.add(data.getJSONArray("image").getString(0));
                            Toast.makeText(getActivity(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
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
}
