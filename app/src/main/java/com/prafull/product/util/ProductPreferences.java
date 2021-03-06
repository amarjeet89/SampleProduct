package com.prafull.product.util;

/***
 * Shubhansu Gupta 21st may 2015
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ProductPreferences {
    private static final String LOGIN_STATUS = "login_status";
    private static final String LOGIN_WITH = "login_with";
    private static final String FB_ACCESS_TOKEN = "fb_access_token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_ID = "user_id";
    private static final String SELLER_ID = "seller_id";
    private static final String SELLER_USER_ID = "seller_user_id";
    private static final String OTP_STATUS = "otp";
    private static final String PREF_NAME = "product_preferences";
    private static final String CURRENT_LATITUDE = "current_latitude";
    private static final String CURRENT_LONGITUDE = "current_longitude";
    private static ProductPreferences instance;
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // Constructor
    public ProductPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static ProductPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new ProductPreferences(context);
        }
        return instance;
    }

    public Boolean getLoginStatus() {
        return pref.getBoolean(LOGIN_STATUS, false);
    }

    public void setLoginStatus(boolean loginStatus) {
        editor.putBoolean(LOGIN_STATUS, loginStatus);
        editor.commit();
    }

    public Boolean getOTPStatus() {
        return pref.getBoolean(OTP_STATUS, false);
    }

    public void setOTPStatus(boolean loginStatus) {
        editor.putBoolean(OTP_STATUS, loginStatus);
        editor.commit();
    }

    public void setLoginWith(String loginWith) {
        editor.putString(LOGIN_WITH, loginWith);
        editor.commit();
    }

    public String getFacebookAccessToken() {
        return pref.getString(FB_ACCESS_TOKEN, "");
    }

    public void setFacebookAccessToken(String facebookAccessToaken) {
        editor.putString(FB_ACCESS_TOKEN, facebookAccessToaken);
        editor.commit();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToaken) {
        editor.putString(ACCESS_TOKEN, accessToaken);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getSellerUserId() {
        return pref.getString(SELLER_USER_ID, "");
    }

    public void setSellerUserId(String userId) {
        editor.putString(SELLER_USER_ID, userId);
        editor.commit();
    }

    public String getSellerId() {
        return pref.getString(SELLER_ID, "");
    }

    public void setSellerId(String sellerId) {
        editor.putString(SELLER_ID, sellerId);
        editor.commit();
    }

    public Long getCurrentLatitude() {
        return pref.getLong(CURRENT_LATITUDE, 0L);
    }

    public void setCurrentLatitude(Long lat) {
        editor.putLong(CURRENT_LATITUDE, lat);
        editor.commit();
    }

    public Long getCurrentLongitude() {
        return pref.getLong(CURRENT_LONGITUDE, 0L);
    }

    public void setCurrentLongitude(Long lat) {
        editor.putLong(CURRENT_LONGITUDE, lat);
        editor.commit();
    }
}
