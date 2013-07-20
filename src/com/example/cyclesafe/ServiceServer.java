package com.example.cyclesafe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.app.Service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServiceServer extends Service implements LocationListener
{
	
	public class ServiceBinder extends Binder {
		ServiceServer getService() {
			return ServiceServer.this;
		}
	}
	
	private final IBinder mBinder = new ServiceBinder(); 
	
	private Context mContext;
	private LocationManager locationManager;
	
	private double latitude; 
	private double longitude; 
	
    private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean canGetLocation = false;
		
	private Location location;
	

	// Minimum distance to travel before an update ( metres )
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
																
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1; //5 seconds

	public void toastLocation()
	{
		Toast.makeText(this, "Lat: " + latitude + "," + "Long: " + longitude, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}

	  

   public Location getLocation() 
   {
       try 
       {
	       locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

	       // getting GPS status
	       isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	       // getting network status
	       isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	       if (!isGPSEnabled && !isNetworkEnabled) 
	       {
	                // no network provider is enabled
	       } else 
	       {
	           this.canGetLocation = true;
	           
	           if (isNetworkEnabled) 
	           {
	               locationManager.requestLocationUpdates(
	               LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	               Log.d("Network", "Network");
	               if (locationManager != null) 
	               {
	                   location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                   if (location != null) 
	                   {
	                        	
	                       latitude = location.getLatitude();
	                       longitude = location.getLongitude();
	                            
	              }
	           }
	       }
	       //if GPS Enabled get lat/long using GPS Services
	       if (isGPSEnabled) 
           {
	           if (location == null) 
	           {
	               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	               Log.d("GPS Enabled", "GPS Enabled");
	               if (locationManager != null) 
	               {
	                   location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                   if (location != null) 
	                   {
	                       latitude = location.getLatitude();
	                       longitude = location.getLongitude();
	                   }
	               }
	           }
            } 
	    }
       }
	   catch (Exception e) 
	   {
	       e.printStackTrace();
	   }

    return location;
    }
	   
	    /**
	     * Stop using GPS listener
	     * Calling this function will stop using GPS in your app
	     * */
	 
	    public void stopUsingGPS()
	    {
	        if(locationManager != null)
	        {
	            locationManager.removeUpdates(ServiceServer.this);
	        }       
	    }
	   
	    /**
	     * Function to get latitude
	     * */
	    public double getLatitude()
	    {
	        if(location != null)
	        {
	            latitude = location.getLatitude();
	        }
	       
	        // return latitude
	        return latitude;
	    }
	   
	    /**
	     * Function to get longitude
	     * */
	    public double getLongitude()
	    {
	        if(location != null)
	        {
	            longitude = location.getLongitude();
	        }
	       
	        // return longitude
	        return longitude;
	    }
	   
	    /**
	     * Function to check GPS/wifi enabled
	     * @return boolean
	     * */
	    public boolean canGetLocation() 
	    {
	        return this.canGetLocation;
	    }
	   
	    /**
	     * Function to show settings alert dialog
	     * On pressing Settings button will lauch Settings Options
	     * */
	    public void showSettingsAlert()
	    {
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
	        
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");

	        // Setting Dialog Message
	        alertDialog.setMessage("GPS is not enabled. Do you want to enable it now?");

	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() 
	        {
	            public void onClick(DialogInterface dialog,int which) 
	            {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                mContext.startActivity(intent);
	            }
	        });

	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	        {
	            public void onClick(DialogInterface dialog, int which) 
	            {
	            dialog.cancel();
	            }
	        });

	        // Showing Alert Message
	        alertDialog.show();
	    }

	    @Override
	    public void onLocationChanged(Location location) 
	    {
	    	latitude = location.getLatitude();
	    	longitude = location.getLongitude();
	    	toastLocation();
	    	postLocation(latitude, longitude, 1234, 0);
	    }

	    @Override
	    public void onProviderDisabled(String provider) 
	    {
	    }

	    @Override
	    public void onProviderEnabled(String provider) 
	    {
	    }

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) 
	    {
	    }


	public void onCreate() 
	{
		mContext = getApplicationContext();
		getLocation();
	}


	@Override
	public void onDestroy() 
	{
	}

	@Override
	public void onStart(Intent intent, int startid) 
	{
	}

	public void postLocation(double latitude, double longitude, int id,
			int vehicleType) {
        // Set up the POST request
        HttpPost postRequest = new HttpPost(
                "http://ec2-50-18-26-146.us-west-1.compute.amazonaws.com:8080/");
        
        //Add the params
        List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
        postParams.add(new BasicNameValuePair("type", String.valueOf(vehicleType)));
        postParams.add(new BasicNameValuePair("id", String.valueOf(id)));
        postParams.add(new BasicNameValuePair("long", String.valueOf(longitude)));
        postParams.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
        
        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParams);
            postRequest.setEntity(formEntity);
            
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(postRequest);
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) { 
        	e.printStackTrace();
        }
	}
	
	public List<Proximity> getCyclists(int lorryId) {
        Gson gson = new Gson();
		
        Type proximityListType = new TypeToken<ArrayList<Proximity>>(){}.getType();
        
		HttpGet getRequest = new HttpGet(
                "http://ec2-50-18-26-146.us-west-1.compute.amazonaws.com:8080/?id" + lorryId);
        
        try {           
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(getRequest);
            
    		InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
    		List<Proximity> cyclists = gson.fromJson(isr, proximityListType);
            
            return cyclists;
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) { 
        	e.printStackTrace();
        }
        
        return null;
	}
}
