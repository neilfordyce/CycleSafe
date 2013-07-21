package com.example.cyclesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class LorryActivity extends Activity 
{
	Button startButton;
	boolean trucking;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		trucking = false;
		
		startButton = (Button) findViewById(R.id.btn_LetsTruck);
        startButton.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
				if(trucking == false)
				 {
					 // Start Service
					 trucking = true;
					 startService(new Intent(getApplicationContext(), ServiceServer.class));
					 startButton.setBackgroundResource(R.drawable.custom_button_stop);
					 startButton.setText(R.string.finishTrucking);
				 }
				 else if (trucking == true)
				 {
					 // End Service
					 trucking = false;
					 startButton.setBackgroundResource(R.drawable.custom_button_start);
					 startButton.setText(R.string.getTrucking);
				 }
				
			}
		});
        
	}


}
