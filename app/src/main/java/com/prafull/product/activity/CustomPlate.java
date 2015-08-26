package com.prafull.product.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.fragments.ImagePickerFragmentForPlate;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.MediaUtils;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * Created by Amar on 05-08-2015.
 */
public class CustomPlate extends AppCompatActivity implements View.OnClickListener, ImagePickerFragmentForPlate.DialogCallBack {
    private static final int RESULT_LOAD_IMG = 01;
    private static final int RESULT_CAPTURE_IMG = 02;
    private String[] mPrices;
    private String[] mDishes;
    Spinner price_spinner, dish_spinner;
    String mCurrency;
    String mDishtype;
    Button upload_image, create_plate;
    EditText mTitle, mDesc, mPrice, mCooktime, plate_item;
    private ProgressDialog loadingProgress;
    private ImagePickerFragmentForPlate imagePicker;
    private FragmentManager fragmentManager;
    private Uri imageUri;
    private String mSelectImgPath;
    private ArrayList<Object> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_plate);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        // Spinner element
        mTitle = (EditText) findViewById(R.id.plate_title);
        mDesc = (EditText) findViewById(R.id.plate_desc);
        mPrice = (EditText) findViewById(R.id.plate_price);
        mCooktime = (EditText) findViewById(R.id.plate_cookingtime);
        price_spinner = (Spinner) findViewById(R.id.price_spinnerlist);
        dish_spinner = (Spinner) findViewById(R.id.dishtype_spinner);
        mPrices = getResources().getStringArray(R.array.currency_list);
        mDishes = getResources().getStringArray(R.array.dish_list);
        upload_image = (Button) findViewById(R.id.upload_image);
        upload_image.setOnClickListener(this);
        create_plate = (Button) findViewById(R.id.create_plate);
        create_plate.setOnClickListener(this);
        plate_item = (EditText) findViewById(R.id.plate_item);
        imageList = new ArrayList<>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mPrices);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        price_spinner.setAdapter(dataAdapter);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mDishes);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dish_spinner.setAdapter(dataAdapter1);
        price_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrency = mPrices[i];
                Toast.makeText(getApplicationContext(), mCurrency, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dish_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDishtype = mDishes[i];
                Toast.makeText(getApplicationContext(), mDishtype, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadingProgress = new ProgressDialog(CustomPlate.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
    }

    private void sendPlateData() {
        if (!mTitle.getText().toString().isEmpty() && !mDesc.getText().toString().isEmpty() && !mPrice.getText().toString().isEmpty()
                && !mCooktime.getText().toString().isEmpty() && !plate_item.getText().toString().isEmpty()) {
            loadingProgress.show();
            JSONObject obj = new JSONObject();
            String loginUrl = getString(R.string.base_url) + getString(R.string.Create_Plate_Url);
            try {
                obj.put("cooking_time", mCooktime.getText().toString());
                obj.put("currency", mCurrency);
                obj.put("description", mDesc.getText().toString());
                obj.put("dish_type", mDishtype);
                obj.put("price", mPrice.getText().toString());
                obj.put("title", mTitle.getText().toString());
                obj.put("token", new ProductPreferences(getApplicationContext()).getAccessToken());
                obj.put("plate_item", plate_item.getText().toString());
                obj.put("quantity", 1);
                JSONArray itemPics = new JSONArray();
                for (int i = 0; i < imageList.size(); i++) {
                    itemPics.put(imageList.get(i));
                }

                obj.put("item_pics", itemPics);
                new BaseSync(redrawListener, loginUrl, obj, CommonUtil.HTTP_POST).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
        }

    }

    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    try {
                        JSONObject obj = new JSONObject(str);
                        System.out.println(obj);
                        if (obj.getString("status").equals("success")) {
                            Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("status") + obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_plate:
                sendPlateData();
                break;
            case R.id.upload_image:

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("image_picker");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                imagePicker = ImagePickerFragmentForPlate.newInstance("Mode", this);

                imagePicker.show(ft, "image_picker");
                break;
        }
    }

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
        imageUri = getContentResolver().insert(
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
            Cursor cursor = getContentResolver().query(pickedImage, filePath,
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
            Cursor cursor = getContentResolver().query(imageUri, filePath,
                    null, null, null);
            cursor.moveToFirst();
            mSelectImgPath = cursor.getString(cursor
                    .getColumnIndex(filePath[0]));
            cursor.close();
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
            obj.put("token", ProductPreferences.getInstance(getApplicationContext()).getAccessToken());
            obj.put("image", encodedImage);
            new BaseSync(imageUploadListener, imageUploadURL, obj, CommonUtil.HTTP_POST).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    BaseSync.OnTaskCompleted imageUploadListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadingProgress.cancel();
                        JSONObject obj = new JSONObject(str);
                        if (obj.getString("status").equals("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            imageList.add(data.getJSONArray("image").getString(0));
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
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
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
}
