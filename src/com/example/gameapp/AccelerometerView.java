package com.example.gameapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AccelerometerView extends View implements SensorEventListener{

	
		 
	    private SensorManager sensormanager;
	    private Sensor accelerometer;
	    private Display display;   
	     
	    private Bitmap background;
	    private Bitmap hole;
	    private Bitmap bitmap;
	    private static final int SIZE_BALL = 40;
	    private static final int SIZE_HOLE = 60;
	    
	    private static final int INITIAL_X = 100;
	    private static final int INITIAL_Y = -400;
	    Context context1;
	     
	    private float xOrigin;
	    private float yOrigin;
	    private float hBound;
	    private float vBound;
	    
	    private float xSensor;
	    private float ySensor;
	    private float zSensor;
	    private long timeSensor;
	 
		private SoundPool soundpool;
		private int soundID;
		boolean loaded = false;
		AudioManager audioManager;
		TextView tv;
		
		
	    Ball ball = new Ball(context1);
	    
	     
	    public AccelerometerView(Context context) {
	        super(context);
	        context1 = context;
	         
	        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
	        bitmap = Bitmap.createScaledBitmap(ball, SIZE_BALL, SIZE_BALL, true);
	         
	        Bitmap target = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
	        hole = Bitmap.createScaledBitmap(target, SIZE_HOLE, SIZE_HOLE, true);
	         
	        Options opts = new Options();
	        opts.inDither = true;
	        opts.inPreferredConfig = Bitmap.Config.RGB_565;
	        background = BitmapFactory.decodeResource(getResources(), R.drawable.grass, opts);
	       

	        
	        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	        display = windowmanager.getDefaultDisplay();
	         
	         sensormanager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	        accelerometer = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        
	    	 audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	        
	        
	        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		    // Load the sound
		    soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		    soundpool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
		      @Override
		      public void onLoadComplete(SoundPool soundPool, int sampleId,
		          int status) {
		        loaded = true;
		      }
		    });
		    soundID = soundpool.load(context,R.raw.sound, 1);
	        
		    
		    tv = new TextView(context);

	        //5000 is the starting number (in milliseconds)
	        //1000 is the number to count down each time (in milliseconds)
	        MyCount counter = new MyCount(20000,1000);

	        counter.start();
		    
	        
	    }
	 
	    private void setVolumeControlStream(int streamMusic) {
			// TODO Auto-generated method stub
			
		}

		@Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        xOrigin = w * 0.5f;
	        yOrigin = h * 0.5f;
	         
	        hBound = (w - SIZE_BALL) * 0.5f;
	        vBound = (h - SIZE_BALL) * 0.5f;
	    }

	

		public void startSimulation() {
		    sensormanager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		}
		 
		public void stopSimulation() {
		    sensormanager.unregisterListener(this);
		}
		
			
		
		@Override
		public void onSensorChanged(SensorEvent event) {
		    if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
		        return;
		     
		    switch (display.getRotation()) {
		    case Surface.ROTATION_0:
		        xSensor = event.values[0];
		        ySensor = event.values[1];
		        break;
		    case Surface.ROTATION_90:
		        xSensor = -event.values[1];
		        ySensor = event.values[0];
		        break;
		    case Surface.ROTATION_180:
		        xSensor = -event.values[0];
		        ySensor = -event.values[1];
		        break;
		    case Surface.ROTATION_270:
		        xSensor = event.values[1];
		        ySensor = -event.values[0];
		        break;
		    }
		    zSensor = event.values[2];
		    timeSensor = event.timestamp;
		
		
		
		}
		 
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		 
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
		    super.onDraw(canvas);
		     
		    canvas.drawBitmap(background, 0, 0, null);
		    canvas.drawBitmap(hole, xOrigin - SIZE_HOLE/2, yOrigin - SIZE_HOLE/2, null);
		   
		    Boolean goalDone = false;
		    goalDone = ball.updatePosition(xSensor, ySensor, zSensor, timeSensor,soundID,loaded,soundpool,audioManager);
		    if(goalDone){
		    	final Toast toast = Toast.makeText(context1, "Goal Done", Toast.LENGTH_SHORT);
    		    toast.show();

    		    Handler handler = new Handler();
    		        handler.postDelayed(new Runnable() {
    		           @Override
    		           public void run() {
    		               toast.cancel(); 
    		           }
    		    }, 500);
    		        ball.mPosX = INITIAL_X;
    		        ball.mPosY = INITIAL_Y;
		    }
		    ball.resolveCollisionWithBounds(hBound, vBound);
		   // ball.resolveCollisionWithHole(context1,xOrigin - SIZE_HOLE/2,yOrigin - SIZE_HOLE/2);
		 
		    canvas.drawBitmap(bitmap, 
		                        (xOrigin - SIZE_BALL/2) + ball.mPosX, 
		                        (yOrigin - SIZE_BALL/2) - ball.mPosY, null);
		     
		    invalidate();
		}
		
		 public class MyCount extends CountDownTimer{

		        public MyCount(long millisInFuture, long countDownInterval) {
		            super(millisInFuture, countDownInterval);
		        }

		        @Override
		        public void onFinish() {
		            tv.setText("done!");
		        }

		        @Override
		        public void onTick(long millisUntilFinished) {
		            tv.setText("Left: " + millisUntilFinished/1000);

		        }

		    }

	}
	   
