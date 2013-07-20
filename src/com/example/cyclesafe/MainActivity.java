package com.example.cyclesafe;

//test comments
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button start, stop;
	Boolean riding;
	Intent gpsService;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
 	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        start = (Button) findViewById(R.id.btn_LetsRide);
        start.setOnClickListener(this);
        riding=false;
        
        gpsService = new Intent(getApplicationContext(), ServiceServer.class);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){

		case R.id.btn_LetsRide:
			
		 
		 
		 if(riding==false)
		 {
			 //start gps yo
			 riding =true;
			 startService(gpsService);
			 start.setBackgroundResource(R.drawable.custom_button_stop);
			 start.setText("FINISHED RIDING");
		 }
		 else if (riding ==true)
		 {
			 //end gps yo
			 riding = false;
			 start.setBackgroundResource(R.drawable.custom_button_start);
			 start.setText("LET'S RIDE");
		 }
		break;
		}
		
	}
    
}
