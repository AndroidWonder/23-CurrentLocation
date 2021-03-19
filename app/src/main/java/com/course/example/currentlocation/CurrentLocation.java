/* This example will use GPS to find your current location.
   You can test it with the emulator extended controls.
   However, the emulator doesn't do periodic updates.

   Run this application with targetSdk at API 23 or higher.
   It will use the runtime permission capability to get GPS access.
   It first checks if the app has permission. If not,
   it will ask the user for permission and it will
   callback into the class for a yes or no .

 */

package com.course.example.currentlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CurrentLocation extends Activity {

	private LocationManager  locManager;
	private LocationListener locListener;
	private final int MY_PERMISSIONS_REQUEST_READ_LOCATIONS = 5;
	private String TAG = "CurrentLocation";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Use the LocationManager class to obtain GPS locations
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locListener = new MyLocationListener();

		Toast.makeText(this, "Start", Toast.LENGTH_LONG).show();
		Log.e(TAG, "Start");

		// check if app has permission
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

				//no, so request the permission.
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						MY_PERMISSIONS_REQUEST_READ_LOCATIONS);

				Toast.makeText(this, "Request Permission", Toast.LENGTH_LONG).show();
				Log.e(TAG, "Request Permission");

		} else {
			//yes, so start updates
			requestUpdates();
		}
	}

	//inner class used to create object that will receive Location update callbacks
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {

			double latitude = loc.getLatitude();
			double longitude = loc.getLongitude();

			String Text = "My current location is: " +
			"Latitude = " + latitude + "Longitude = " + longitude;

			Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(),
			"Gps Enabled", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) { }
		
	}// End MyLocationListener
	
	//stop updates
	public void onStop() {
		super.onStop();
		try {
			locManager.removeUpdates(locListener);
		} catch(SecurityException e){
			Log.e(TAG, "Error removing updates");
		};
	}

	//called when button is clicked
	public void stopUpdates(View view) {
		try {
			locManager.removeUpdates(locListener);
		}catch (SecurityException e) {Log.e(TAG, "Error removing updates");};
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_READ_LOCATIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// task you need to do. 

					requestUpdates();

					Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
					Log.e(TAG, "Permission Granted");

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.

					Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
					Log.e(TAG, "Permission Denied");

				}
				return;
			}

		}
	}

	public void requestUpdates(){

		//Register for location updates using the named provider, and a pending intent.
		//10 second minimum interval between updates, 0 meters minimum distance between updates
		try {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					locListener);
		} catch(SecurityException e) {
			Toast.makeText(getApplicationContext(), "updates requested exception", Toast.LENGTH_LONG)
					.show();
			Log.e("CurrentLocation", "Error requesting updates");}

	}
}
