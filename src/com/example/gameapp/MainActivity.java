package com.example.gameapp;


import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.widget.TextView;

import com.example.gameapp.AccelerometerView;

public class MainActivity extends Activity {

	private static final String myTag = "com.example.gameapp";
	private WakeLock wakelock;
	private AccelerometerView accelerometerview;

	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		PowerManager powermanager = (PowerManager)getSystemService(POWER_SERVICE);
		wakelock = powermanager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, myTag);
		accelerometerview = new AccelerometerView(this);
		    setContentView(accelerometerview);
		    
		  
	       
		  
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	
	}
	
	@Override
	public void onResume(){
		super.onResume();
		wakelock.acquire();
		accelerometerview.startSimulation();
		
	}
	
	@Override
	public void onPause(){
		super.onPause();				
		accelerometerview.stopSimulation();
		wakelock.release();
	}

}
