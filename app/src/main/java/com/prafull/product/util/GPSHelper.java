package com.prafull.product.util;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class GPSHelper {

	private Context context;
	// flag for GPS Status
	private boolean isGPSEnabled = false;
	// flag for network status
	private boolean isNetworkEnabled = false;
	private LocationManager locationManager;
	private Location location;
	private double latitude;
	private double longitude;
	GpsListner listner;

	public GPSHelper(Context context,GpsListner listner) {
		this.context = context;
		this.listner = listner;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

	}

	public interface GpsListner{
		void gotCurrentLocation(LatLng curentLatLng);
	}

	public void checkGPS() {
		if (isGPSenabled()) {
			// showSettingsAlert();

		} else {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();

			}
		}
	}

	public void getMyLocation() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> providers = locationManager.getProviders(true);
				Location l = null;
				for (int i = 0; i < providers.size(); i++) {
					l = locationManager.getLastKnownLocation(providers.get(i));
					if (l != null)
						break;
				}
				if (l != null) {
					latitude = l.getLatitude();
					longitude = l.getLongitude();

					listner.gotCurrentLocation(new LatLng(latitude,longitude));
				}
			}
		}).start();

	}

	public boolean isGPSenabled() {
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// getting network status
		isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return (isGPSEnabled || isNetworkEnabled);
	}

	/**
	 * Function to get latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Function to get longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Function to get Lat Long
	 */
	public LatLng getLatLong(){
		return new LatLng(latitude, longitude);
	}
}
