package com.example.gameapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Ball {
	
	    /* coefficient of restitution */
	    private static final float COR = 0.7f;
	    private static final int SHORT_DELAY = 2000; // 2 seconds
	     
	    public float mPosX = 100;
	    public float mPosY = -400;
	    private float mVelX;
	    private float mVelY;
	    Context context1;
	    AudioManager audiomanager;
	
	    
	    public Ball(Context context){
	    	  context1 = context;
		   
	    }
	     
	    public boolean updatePosition(float sx, float sy, float sz, long timestamp,int soundID,boolean loaded,SoundPool soundpool,AudioManager audioManager) {
	    	boolean goaled = false;
	    	audiomanager = audioManager;
	        float dt = (System.nanoTime() - timestamp) / 1000000000.0f;
	        mVelX += -sx * dt;
	        mVelY += -sy * dt;
	         
	        mPosX += mVelX * dt;
	        mPosY += mVelY * dt;
	        if((mPosX<10 && mPosX>-10) && (mPosY<10 && mPosY>-10)){
	        	goaled = true;
	        	playSound(soundID,loaded,soundpool);
	        }else{
	        	goaled = false;
	        }
	        return goaled;
	    }
	     
	    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
	        if (mPosX > mHorizontalBound) {
	            mPosX = mHorizontalBound;
	            mVelX = -mVelX * COR;
	        } else if (mPosX < -mHorizontalBound) {
	            mPosX = -mHorizontalBound;
	            mVelX = -mVelX * COR;
	        }
	        if (mPosY > mVerticalBound) {
	            mPosY = mVerticalBound;
	            mVelY = -mVelY * COR;
	        } else if (mPosY < -mVerticalBound) {
	            mPosY = -mVerticalBound;
	            mVelY = -mVelY * COR;
	        }
	    }
	    
//	    public void resolveCollisionWithHole(Context context, float xHole, float yHole){
//	    	 if (mPosX <= xHole  || mPosY <= yHole) {
//	    		 final Toast toast = Toast.makeText(context, "Collision", Toast.LENGTH_SHORT);
//	    		    toast.show();
//
//	    		    Handler handler = new Handler();
//	    		        handler.postDelayed(new Runnable() {
//	    		           @Override
//	    		           public void run() {
//	    		               toast.cancel(); 
//	    		           }
//	    		    }, 500);
//	    	
//	    }
//	}
	    
	    
	    public void playSound(int soundID,boolean loaded, SoundPool soundpool){
	    	float volume = 15;
	
//	        float actualVolume = (float) audiomanager
//	            .getStreamVolume(AudioManager.STREAM_MUSIC);
	        
//	        volume = actualVolume / maxVolume;
	    	
	    	//int origionalVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    	audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
	    	float streamVolume  = (float) audiomanager.getStreamVolume(audiomanager.STREAM_MUSIC);
		          

	    	if (loaded) {
	            soundpool.play(soundID, streamVolume, streamVolume, 1, 0, 1f);
	            Log.e("Test", "Played sound");
	          }
	    	
	    }
	    
	    
	    
}
	          
