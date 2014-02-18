package mmpud.game.takecrown;


import java.util.Timer;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("InlinedApi")
public class TakeCrownActivity extends Activity {
	
	private Button btnPlayer1, btnPlayer2, btnPlayer3, btnPlayer4;
	private Button btnLight;
	int lightState;
	private ImageView win1, win2, win3, win4;
	private Boolean isP1Pressed, isP2Pressed, isP3Pressed, isP4Pressed;
	private Boolean isLocked;

	private int countDownSec;
	final int maxCountDownSec = 5000, minCountDownSec = 2000;
	Timer timer;
	private Handler handler = new Handler();
	private String TAG = "mmpud";
	
	Animation rotateAnim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//request for full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_take_crown);
		
		//detect multitouch
		PackageManager pm = this.getPackageManager();
	    boolean hasMultitouch = 
	        pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
	    if (hasMultitouch) {
	        Log.d(TAG,"has multi_touch");
	    } else {
	    	Log.d(TAG,"no multi_touch");
	    }
		
		lightState = 0;
		isLocked = true;
		btnPlayer1 = (Button)findViewById(R.id.btn_player1);
		btnPlayer2 = (Button)findViewById(R.id.btn_player2);
		btnPlayer3 = (Button)findViewById(R.id.btn_player3);
		btnPlayer4 = (Button)findViewById(R.id.btn_player4);
		btnLight = (Button)findViewById(R.id.btn_light);
		
		btnPlayer1.setOnTouchListener(myOnTouchListener);
		btnPlayer2.setOnTouchListener(myOnTouchListener);
		btnPlayer3.setOnTouchListener(myOnTouchListener);
		btnPlayer4.setOnTouchListener(myOnTouchListener);
		btnLight.setOnClickListener(myOnClickListener);
		//disable the buttons first
		btnPlayer1.setEnabled(false);
		btnPlayer2.setEnabled(false);
		btnPlayer3.setEnabled(false);
		btnPlayer4.setEnabled(false);

		win1 = (ImageView)findViewById(R.id.iv_win1);
		win2 = (ImageView)findViewById(R.id.iv_win2);
		win3 = (ImageView)findViewById(R.id.iv_win3);
		win4 = (ImageView)findViewById(R.id.iv_win4);
		
		rotateAnim = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		rotateAnim.setRepeatCount(Animation.INFINITE);
		handler.removeCallbacks(timesUp);
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
	   super.onWindowFocusChanged(hasFocus);
	   if (hasFocus) {

		   btnLight.startAnimation(rotateAnim);
	   }
		   
	}
	
	//the thread that defines the event when the time is up
	private Runnable timesUp= new Runnable() {
		public void run() {
			//release the lock so that it's time for players to press
			//the button as soon as possible
			isLocked = false;
			btnLight.setBackgroundResource(R.drawable.light_green);
		}
	};
	
	OnClickListener myOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.btn_light) {
				isP1Pressed = false;
				isP2Pressed = false;
				isP3Pressed = false;
				isP4Pressed = false;
				btnPlayer1.setEnabled(true);
				btnPlayer2.setEnabled(true);
				btnPlayer3.setEnabled(true);
				btnPlayer4.setEnabled(true);
				btnPlayer1.setBackgroundResource(R.drawable.btn_blue);
				btnPlayer2.setBackgroundResource(R.drawable.btn_green);
				btnPlayer3.setBackgroundResource(R.drawable.btn_red);
				btnPlayer4.setBackgroundResource(R.drawable.btn_yellow);
				win1.setVisibility(View.GONE);
				win2.setVisibility(View.GONE);
				win3.setVisibility(View.GONE);
				win4.setVisibility(View.GONE);
				//the red light starts
				btnLight.setBackgroundResource(R.drawable.light_red);
				btnLight.setClickable(false);
				//start the timer
				countDownSec = minCountDownSec+(int)(Math.random()*((maxCountDownSec - minCountDownSec)+1));
				handler.postDelayed(timesUp, countDownSec);
			}
		}
	};
	
	OnTouchListener myOnTouchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch(v.getId()) {
			case R.id.btn_player1:
				if(event.getAction()==MotionEvent.ACTION_DOWN) {
					btnPlayer1.setEnabled(false);
					Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
		            vb.vibrate(100);
					if(!isLocked) {
						win1.setVisibility(View.VISIBLE);
						btnPlayer2.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer3.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer4.setBackgroundResource(R.drawable.btn_grey);
						endGame();
					}
					else {
						btnPlayer1.setBackgroundResource(R.drawable.btn_grey);
						isP1Pressed = true;
						if(isP1Pressed && isP2Pressed && isP3Pressed && isP4Pressed) {
							endGame();
						}
					}
				}
				return true;
			case R.id.btn_player2:
				if(event.getAction()==MotionEvent.ACTION_DOWN) {
					btnPlayer2.setEnabled(false);
					Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
		            vb.vibrate(100);
					if(!isLocked) {
						win2.setVisibility(View.VISIBLE);
						btnPlayer1.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer3.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer4.setBackgroundResource(R.drawable.btn_grey);
						endGame();
					}
					else {
						btnPlayer2.setBackgroundResource(R.drawable.btn_grey);
						isP2Pressed = true;
						if(isP1Pressed && isP2Pressed && isP3Pressed && isP4Pressed) {
							endGame();
						}
					}
				}
				return true;
			case R.id.btn_player3:
				if(event.getAction()==MotionEvent.ACTION_DOWN) {
					btnPlayer3.setEnabled(false);
					Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
		            vb.vibrate(100);
					if(!isLocked) {
						win3.setVisibility(View.VISIBLE);
						btnPlayer1.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer2.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer4.setBackgroundResource(R.drawable.btn_grey);
						endGame();
					}
					else {
						btnPlayer3.setBackgroundResource(R.drawable.btn_grey);
						isP3Pressed = true;
						if(isP1Pressed && isP2Pressed && isP3Pressed && isP4Pressed) {
							endGame();
						}
					}
				}
				return true;
			case R.id.btn_player4:
				if(event.getAction()==MotionEvent.ACTION_DOWN) {
					btnPlayer4.setEnabled(false);
					Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
		            vb.vibrate(100);
					if(!isLocked) {
						win4.setVisibility(View.VISIBLE);
						btnPlayer1.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer2.setBackgroundResource(R.drawable.btn_grey);
						btnPlayer3.setBackgroundResource(R.drawable.btn_grey);
						endGame();
					}
					else {
						btnPlayer4.setBackgroundResource(R.drawable.btn_grey);
						isP4Pressed = true;
						if(isP1Pressed && isP2Pressed && isP3Pressed && isP4Pressed) {
							endGame();
						}
					}
				}
				return true;
			}
			return true;
		}
	};

	void endGame() {
		lightState = 0;
		isLocked = true;
		btnPlayer1.setEnabled(false);
		btnPlayer2.setEnabled(false);
		btnPlayer3.setEnabled(false);
		btnPlayer4.setEnabled(false);
		btnLight.setBackgroundResource(R.drawable.light_start);
		btnLight.setClickable(true);
		//remove the timer
		handler.removeCallbacks(timesUp);
	}
}