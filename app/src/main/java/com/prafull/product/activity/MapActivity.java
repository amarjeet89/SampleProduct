package com.prafull.product.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prafull.product.R;
import com.prafull.product.adapter.PlaceArrayAdapter;
import com.prafull.product.util.GPSHelper;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, GPSHelper.GpsListner, TextView.OnEditorActionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_ENABLE_GPS = 5000;
   Button set_address;
    LatLngBounds NEW_YORK_BOUND;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private GoogleApiClient mGoogleApiClient;
    private LatLng mLastLocation;
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private GPSHelper gpsHelper;
    private AutoCompleteTextView tv_current_loc;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private String lastLocation;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            com.google.android.gms.location.places.Place place = places.get(0);

            lastLocation = place.getAddress().toString();
            mLastLocation = place.getLatLng();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        set_address=(Button)findViewById(R.id.set_address);
        set_address.setOnClickListener(this);
        tv_current_loc = (AutoCompleteTextView) findViewById(R.id.cur_loc_text);
        tv_current_loc.setOnEditorActionListener(this);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        tv_current_loc.setThreshold(1);
        tv_current_loc.addTextChangedListener(textWatcher);
        tv_current_loc.setOnItemClickListener(mAutocompleteClickListener);
      //  buildGoogleApiClient();
       // mapFragment.getMapAsync(this);
        checkGPSSetting();
        NEW_YORK_BOUND = new LatLngBounds(
                new LatLng(40.5986013, -74.0112667), new LatLng(40.6345656, -74.1197567));
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,NEW_YORK_BOUND, null);
        tv_current_loc.setAdapter(mPlaceArrayAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();


    }

    void checkGPSSetting(){
        gpsHelper = new GPSHelper(this,this);
        if (!gpsHelper.isGPSenabled()) {
            Toast.makeText(getApplicationContext(), "Please switch on GPS", Toast.LENGTH_LONG).show();
            showSettingsDialog("Enable GPS");
        } else {
            getGpsLocation();

        }
    }

    void getGpsLocation() {
        if (gpsHelper != null) {
            gpsHelper.getMyLocation();
        }

    }

    private void showSettingsDialog(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(
                                new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                REQUEST_ENABLE_GPS);
                    }
                });

        dialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        dialogBuilder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_GPS) {
            if (gpsHelper.isGPSenabled()) {
                getGpsLocation();

            } else {
                Toast.makeText(getApplicationContext(), "GPS not enabled", Toast.LENGTH_LONG).show();
                this.getActionBar().setSubtitle("GPS not enabled");
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_address:
                if (mLastLocation != null) {
                    Bundle b = new Bundle();
                    Intent intent = new Intent(MapActivity.this, NavigationDrawerActivity.class);
                    b.putDouble("latitude", mLastLocation.latitude);
                    b.putDouble("longitude", mLastLocation.longitude);
                  /*  intent.putExtra("latitude", mLastLocation.latitude);
                    intent.putExtra("longitude", mLastLocation.longitude);*/
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.location_not_find), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void gotCurrentLocation(final LatLng curentLatLng) {
        if (curentLatLng != null) {
            // this.currentLatLng = currentLatLong;
            // setDefaultMarker();
            mLastLocation = curentLatLng;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMap != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curentLatLng, 13));

                        mMap.addMarker(new MarkerOptions()
                                .title("Address")
                                .position(curentLatLng));
                    }
                }
            });



        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mPlaceArrayAdapter != null){
            mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        }else {
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, NEW_YORK_BOUND, null);
            mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        }

        Log.i("Logger", "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Logger", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("Logger", "Google Places API connection suspended.");
    }


}
