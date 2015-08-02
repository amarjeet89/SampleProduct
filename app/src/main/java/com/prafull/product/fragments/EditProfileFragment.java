package com.prafull.product.fragments;

/**
 * Created by SHUBHANSU on 8/2/2015.
 */

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView mobileNo;
    private TextView address;
    private TextView country;
    private TextView aboutMe;
    private Button save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_user_edit, null);
        initializeControls(view);
        return view;
    }

    private void initializeControls(View view){
        /*loadingProgress = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");*/
        firstName = (TextView)view.findViewById(R.id.f_name);
        lastName = (TextView)view.findViewById(R.id.l_name);
        email = (TextView)view.findViewById(R.id.email);
        mobileNo = (TextView)view.findViewById(R.id.mobile);
        address = (TextView)view.findViewById(R.id.address);
        country = (TextView)view.findViewById(R.id.country);
        aboutMe = (TextView)view.findViewById(R.id.about_me);
        save = (Button)view.findViewById(R.id.save);
        save.setOnClickListener(this);
        getProfileData();

    }
    private void getProfileData() {
        String profileURL = getString(R.string.base_url)+getString(R.string.user_Url)+
                ProductPreferences.getInstance(getActivity()).getUserId()+"?"+getString(R.string.token_Url)+
                ProductPreferences.getInstance(getActivity()).getAccessToken();

        new BaseSync(redrawListener,profileURL, null , CommonUtil.HTTP_GET).execute();
    }

    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(str);
                        if(obj.getString("status").equals("success")){
                            JSONObject profileObject = obj.getJSONObject("data").getJSONObject("data");
                            email.setText(profileObject.getString("email"));
                            firstName.setText(profileObject.getString("fname"));
                            lastName.setText(profileObject.getString("lname"));
                            mobileNo.setText(profileObject.getString("mobile"));
                            address.setText(profileObject.getString("address"));
                            country.setText(profileObject.getString("country"));
                            aboutMe.setText(profileObject.getString("about_me"));
                        }else{
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
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    public void onClick(View v) {
        if(v.getId() == save.getId()){
            updateProfileData();
        }

    }

    private void updateProfileData() {
        String updateProfileURL = getString(R.string.base_url)+getString(R.string.user_Url)+
                ProductPreferences.getInstance(getActivity()).getUserId();

        JSONObject obj = new JSONObject();
        try {
            obj.put("fname", firstName.getText().toString());
            obj.put("email", email.getText().toString());
            obj.put("lname", lastName.getText().toString());
            obj.put("mobile", mobileNo.getText().toString());
            obj.put("address", address.getText().toString());
            obj.put("country", country.getText().toString());
            obj.put("about_me", aboutMe.getText().toString());
            obj.put("token", ProductPreferences.getInstance(getActivity()).getAccessToken());
            new BaseSync(updateListener,updateProfileURL, obj, CommonUtil.HTTP_PUT).execute();
        } catch (JSONException e) {
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
                        if(obj.getString("status").equals("success")){
                            Toast.makeText(getActivity(), obj.getString("Updated"), Toast.LENGTH_SHORT).show();
                        }else{
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
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
}
