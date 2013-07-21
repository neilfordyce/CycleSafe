package com.example.cyclesafe;

import android.os.Bundle;
import android.widget.ImageView;

import android.location.*;
import com.google.android.maps.*;

public class LorryMapActivity extends MapActivity 
{

	private static final int MAX_ZOOM = 21;
	
	private MapView map;
	private ImageView imageNotification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lorry_map_activity);
		
		// Get UI Elements
		map = (MapView) findViewById(R.id.lorryMap);
		imageNotification = (ImageView) findViewById(R.id.image);
		
		// Set up map properties
		map.setBuiltInZoomControls(true);
		map.setTraffic(true);
		map.setClickable(true);
		map.getController().setZoom(MAX_ZOOM);
		
		//map.postInvalidateDelayed(2000);
		
		
		// TODO: Timer -> getCyclists
		
		// TODO: UICallback to update lorry location on map
	}

	@Override
	protected boolean isLocationDisplayed() 
	{
		return true;
	}
	
	@Override
	protected boolean isRouteDisplayed() 
	{
		return false;
	}


}
