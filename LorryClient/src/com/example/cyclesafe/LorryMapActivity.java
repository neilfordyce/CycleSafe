package com.example.cyclesafe;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.ImageView;

import android.app.Activity;
import android.location.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LorryMapActivity extends Activity 
{

	private static final int MAX_ZOOM = 21;
	private static final int TIMER_DELAY = 5000; // 5s
	
	private GoogleMap map;
	//private MapView map;
	private ImageView imageNotification;
	private Timer proximityTimer;
	
	private String android_id;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lorry_map_activity);
		
		// Setup Google Map Fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if (map != null)
		{
			GoogleMapOptions options = new GoogleMapOptions();
			options.mapType(GoogleMap.MAP_TYPE_NORMAL)
			    .compassEnabled(true)
			    .rotateGesturesEnabled(false)
			    .tiltGesturesEnabled(false);
			map.setIndoorEnabled(true);
			map.setMyLocationEnabled(true);
			map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() 
			{
				@Override
				public void onMyLocationChange(Location location) 
				{
					// Update camera to my location
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					LatLng latLng = new LatLng(latitude, longitude);
					map.moveCamera(CameraUpdateFactory.newLatLng(latLng));			
					
					// Post to server
					
				}
			});
		}
		
		// Unique device ID to recognise Lorry client
        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID); 
		
		// Get UI Elements
		//imageNotification = (ImageView) findViewById(R.id.image);
		// Set up map properties
//		map.setBuiltInZoomControls(true);
//		map.setTraffic(true);
//		map.setClickable(true);
//		map.getController().setZoom(MAX_ZOOM);
		
		//map.postInvalidateDelayed(2000);
		
		// Get cyclists periodically
        proximityTimer = new Timer();
		TimerTask task = new TimerTask() 
		{
			
			@Override
			public void run() 
			{
				List<Proximity> nearByCyclists = getCyclists(android_id);
				
				if (nearByCyclists == null)
					return;
					
				// Update map with nearby cyclists
				for (int i = 0; i < nearByCyclists.size(); i++)
				{
					double latitude = nearByCyclists.get(i).getLatitude();
					double longitude = nearByCyclists.get(i).getLongitude();
					double distance = nearByCyclists.get(i).getDistance();
					
					map.addMarker(new MarkerOptions()
			        	.position(new LatLng(latitude, longitude))
			        	.title(String.valueOf(distance)));
						
					Logger.getLogger("").log(Level.INFO, "Nearby Cyclist: {0}, {1}", new Object[]{latitude, longitude});
				}
			}
		};
		
		proximityTimer.scheduleAtFixedRate(task, 0, TIMER_DELAY);
	}

	/**
	 * Performs HTTP GET Request to server.  
	 * Receives list of cyclists
	 * @param lorryId
	 * @return
	 */
	public List<Proximity> getCyclists(String lorryId) 
	{
        Gson gson = new Gson();
		
        Type proximityListType = new TypeToken<ArrayList<Proximity>>(){}.getType();
       
		HttpGet getRequest = new HttpGet(
                "http://ec2-50-18-26-146.us-west-1.compute.amazonaws.com:8080/?id=" + lorryId);
        
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
	
	public void postLocation(double latitude, double longitude, String id,
			int vehicleType) {
        // Set up the POST request
        HttpPost postRequest = new HttpPost(
                "http://ec2-50-18-26-146.us-west-1.compute.amazonaws.com:8080/");
        
        //Add the params
        List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
        postParams.add(new BasicNameValuePair("type", String.valueOf(vehicleType)));
        postParams.add(new BasicNameValuePair("id", id));
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

	
	@Override
	public void onResume()
	{
		// Get my location
		super.onResume();
//		myLocation.enableMyLocation();
//		myLocation.runOnFirstFix(new Runnable() {
//			public void run() {
//				map.getController().setCenter(myLocation.getMyLocation());
//			}
//		});
	}
	
	@Override 
	public void onPause()
	{
		super.onPause();
//		myLocation.disableMyLocation();
	}
	
}
