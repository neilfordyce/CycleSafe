package com.example.cyclesafe;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class ServiceServer extends Service 
{
	
	
	
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
    
    private final Context mContext;
    
    // Declaring a Location Manager
    protected LocationManager locationManager;
    
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    
    
    
    public ServiceServer(Context context)
    {
    	mContext = context;
    }
    
	@Override
	public IBinder onBind(Intent intent) 
	{
	    return null;
	}
	
	@Override
	public void onCreate() 
	{
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onDestroy() 
	{
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
	
	}
	
	@Override
	public void onStart(Intent intent, int startid) 
	{
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		
	}
	
	 public Location getLocation() {
	        try {
	            locationManager = (LocationManager) mContext
	                    .getSystemService(LOCATION_SERVICE);
	 
	            // getting GPS status
	            isGPSEnabled = locationManager
	                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
	 
	            // getting network status
	            isNetworkEnabled = locationManager
	                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	 
	            if (!isGPSEnabled && !isNetworkEnabled) {
	                // no network provider is enabled
	            } else {
	                this.canGetLocation = true;
	                // First get location from Network Provider
	                if (isNetworkEnabled) {
	                    locationManager.requestLocationUpdates(
	                            LocationManager.NETWORK_PROVIDER,
	                            MIN_TIME_BW_UPDATES,
	                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
	                    Log.d("Network", "Network");
	                    if (locationManager != null) {
	                        location = locationManager
	                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                        if (location != null) {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
	                        }
	                    }
	                }
	                // if GPS Enabled get lat/long using GPS Services
	                if (isGPSEnabled) {
	                    if (location == null) {
	                        locationManager.requestLocationUpdates(
	                                LocationManager.GPS_PROVIDER,
	                                MIN_TIME_BW_UPDATES,
	                                MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
	                        Log.d("GPS Enabled", "GPS Enabled");
	                        if (locationManager != null) {
	                            location = locationManager
	                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                            if (location != null) {
	                                latitude = location.getLatitude();
	                                longitude = location.getLongitude();
	                            }
	                        }
	                    }
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 
	        return location;
	    }
	
	
	
	
	
}