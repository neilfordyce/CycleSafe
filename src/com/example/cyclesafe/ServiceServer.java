package com.example.cyclesafe;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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

	 
	public void postLocation(double latitude, double longitude, int id, int vehicleType){
		// Set up the POST request
		HttpPost postRequest = new HttpPost("http://ec2-50-18-26-146.us-west-1.compute.amazonaws.com:8080/");
		
		HttpParams params = new BasicHttpParams();
		params.setDoubleParameter("lat", latitude);
		params.setDoubleParameter("long", longitude);
		params.setDoubleParameter("id", id);
		params.setDoubleParameter("type", vehicleType);
		postRequest.setParams(params);
		
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 30000);
	
		try {
			// Do the request
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(postRequest);
			
			// Close the stream
			response.getEntity().getContent().close();
	
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e){  //TODO Remove Pokemon
			e.printStackTrace();
		}
	}
	
	public void getCyclists(){
		
	}
	
	
}
