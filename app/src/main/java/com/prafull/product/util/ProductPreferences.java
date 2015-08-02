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
    private static ProductPreferences instance;
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "product_preferences";

    public static ProductPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new ProductPreferences(context);
        }
        return instance;
    }

    // Constructor
    public ProductPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLoginStatus(boolean loginStatus) {
        editor.putBoolean(LOGIN_STATUS, loginStatus);
        editor.commit();
    }
    public Boolean getLoginStatus() {
        return pref.getBoolean(LOGIN_STATUS, false);
    }

    public void setLoginWith(String loginWith) {
        editor.putString(LOGIN_WITH, loginWith);
        editor.commit();
    }

    public void setFacebookAccessToken(String facebookAccessToaken) {
        editor.putString(FB_ACCESS_TOKEN, facebookAccessToaken);
        editor.commit();
    }

    public String getFacebookAccessToken() {
        return pref.getString(FB_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToaken) {
        editor.putString(ACCESS_TOKEN, accessToaken);
        editor.commit();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }
}
