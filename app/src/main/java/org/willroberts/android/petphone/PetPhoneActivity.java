package org.willroberts.android.petphone;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;
// import android.util.Log;

public class PetPhoneActivity extends Activity {
	public View helloText;
	public View digitalClock;
	public View strokeText;
	public View tapText;
	public LinearLayout toplevelA;
	public LinearLayout vertLayoutA;
	public LinearLayout horizLayoutA;
	public MediaPlayer mp;
	public MediaPlayer mp2;
	boolean doVib = false; // holds the vibrator pref setting
	String vibstat;
	boolean vibOn;
	private static final int REQUEST_CODE10 = 10;
	private static final int REQUEST_CODE5 = 5;
/*
 * 	(non-Javadoc)
 * @see android.app.Activity#onCreate(android.os.Bundle)
 */
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		toplevelA = (LinearLayout)findViewById(R.id.linearLayout2);
		tapText = (View)findViewById(R.id.editText3);
		toplevelA.setOnTouchListener(dragt);
		tapText.setOnClickListener(clickd);
	   mp = MediaPlayer.create(this, R.raw.purr1);
	    mp2 = MediaPlayer.create(this, R.raw.angry6);   
		/*
		 * Get preferences
		 * petsettings = preferences file
		 * mypet is key in key/value pair points to full path of image
		 * pet is value in key/value pair
		 * if value = $none$ then no pref was set
		 */
		SharedPreferences petsettings = getPreferences(MODE_PRIVATE);
		String pet = petsettings.getString("mypet", "$none$");
//		String vibOn = petsettings.getString("vib", "$none$");
/**Prefs.getrVibrate(context) needs to be fixed 
 * Fix: Using global var context = getApplicationContext()
 * Using the application context is recommended **/
	    if (Prefs.getVibrate(getApplicationContext())){
	    	  Toast.makeText(getApplicationContext(),"Vibration is ON",1).show();
	    	  vibOn = true;
	    }else {
//	    	  Toast.makeText(getApplicationContext(),"Vib is OFF",1).show();  
	      }
		/* ---------------------------------------------------- */
		if (pet == "$none$" || Prefs.getuseDefaultImg(getApplicationContext())){
//			Toast.makeText(this,"No kitty selected - Loading default.",5000).show();
			toplevelA.setBackgroundResource(R.drawable.cats);
			
		}else {
			toplevelA.setBackgroundDrawable(Drawable.createFromPath(pet));
		}
		if (vibOn){
			SharedPreferences.Editor editor = petsettings.edit();
		      editor.putString("vib", "OFF");
		      doVib = false; //ON for testing, OFF for production
		      editor.commit();
		} else {
			doVib = vibOn;
		}
//		Toast.makeText(this,"doVib is "+vibOn+" in onCreate.",1).show();
	}//onCreate
/*
 * END OF onCREATE    
 */
/*
 * clickd handles screen tap. makes angry sound
 */
    OnClickListener clickd = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			audioPlayer();
		} //onClick
    };//OnClickLintener
/*
 * dragt handles drag on screen for petting. makes purring sound
 */
    OnTouchListener dragt = new OnTouchListener()
    {
    	@Override
    	public boolean onTouch(View v, MotionEvent event){
    		audioPlayer2(event);
    		return true;
    	}//onTouch
    };//onTouchListener
/** 
 * 
 *     Display the menu
 */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
/** 
 * 
 * Respond to menu item selection
 * Menu Items:
 *** Options
 *** Choose Pet
 *** About PetPhone 
**/    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.choosepet)
        {

            // react to the menu item being selected...
        	Intent intent = new Intent(this,FileChooser.class);
        	startActivityForResult(intent, REQUEST_CODE10);
            return true;
        }
        if(item.getItemId() == R.id.about)
        {
            // react to the menu item being selected...
        	Intent intent = new Intent(this,AboutPetPhoneActivity.class);
        	startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.options)
        {
            // react to the Options menu item being selected...
        	Intent intent = new Intent(getApplicationContext(),Prefs.class);
        	startActivity(intent);        	
            return true;
        }
        return false;
    }
/*
 * 
 * Result coming back from option menu
 * REQUEST_CODE10 is for background image
 * REQUEST_CODE5 is for vibrate
 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE10) {
			if (data.hasExtra("mypet") && data.hasExtra("retName") && data.hasExtra("retData")) {
								// Variable with the path to the background
			     String bg_path =  data.getExtras().getString("mypet");
			     String bg_data =  data.getExtras().getString("retData");
			     String bg_name =  data.getExtras().getString("retName");
			     if (Integer.decode(bg_data.substring(1))> 190000){
						Toast.makeText(this,"Image must be < 190K",5000).show();
					}else {
						if (!Prefs.getuseDefaultImg(getApplicationContext())){
						toplevelA = (LinearLayout)findViewById(R.id.linearLayout2);
					    //Change background of Activity if useDefaultImg is not set      
					    toplevelA.setBackgroundDrawable(Drawable.createFromPath(bg_path));
						}
					      SharedPreferences settings = getPreferences(MODE_PRIVATE);
					      SharedPreferences.Editor editor = settings.edit();
					      editor.putString("mypet", bg_path);
					      editor.putString("retData", bg_data);
					      editor.putString("retName", bg_name);
					      // Commit the edits!
					      editor.commit();
//					      Toast.makeText(this,"Name: "+bg_name+"\nData: "+bg_data+"\nPath: "+bg_path,dur).show();
					}

			}else {
				Toast.makeText(this,"Bad return path!",2000).show();
			} // if data has extra
		}// if result_code request_code
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE5){
			String isVibOn =  data.getExtras().getString("vibStatus");
		      SharedPreferences settings = getPreferences(MODE_PRIVATE);
		      SharedPreferences.Editor editor = settings.edit();
		      editor.putString("vib", isVibOn);
		      Toast.makeText(this,"Vib is set to "+isVibOn,5000).show();
		      // Commit the edits!
		      editor.commit();
		      if (Prefs.getVibrate(getApplicationContext())){
		    	  doVib = true;
		      }
//		      Toast.makeText(this,"Vib is "+doVib,1000).show();
		}
	} // onActivityResult
//Create media players
    
    public void audioPlayer(){
        //set up MediaPlayer
    	mp2.start();
    }// audioplayer
    
// A second MediaPlayer    
    public void audioPlayer2(MotionEvent Event){
        //set up MediaPlayer    
        int action = Event.getAction();
	    if (action == MotionEvent.ACTION_DOWN) {
	    	mp.start();
  	    }
	    if (action == MotionEvent.ACTION_MOVE) {
//	    	Toast.makeText(this,"Vib is "+doVib,1).show();
//	    	Log.v("MotionEvent", "Action = MOVE");
//	    	Log.v("MotionEvent", "Is playing is " + mp.isPlaying());
		      if (Prefs.getVibrate(getApplicationContext())){
		    	  doVibrate(20);
		      }
/*
 * 			if MediaPlayer is not playing, start it.	    	
 */
	    	if (!mp.isPlaying()) {
	    		mp.start();
	    	}
	    }
   	    if (action == MotionEvent.ACTION_UP) {
   	         // do something
//       	    Log.v("MotionEvent", "Action = " + action + "UP");
//       	    Log.v("PlayerCommand","Reset");
//   	    	mp.reset();
   	    }
   	    if (action == MotionEvent.ACTION_CANCEL){
   	    	mp.reset();
   	    }//if action == CANCEL
    }// audioplayer2
    public void doVibrate(long howLong){
    	// Get instance of Vibrator from current Context
    	Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	// Vibrate for 300 milliseconds
    	vib.vibrate(howLong);
    }
}//activity
