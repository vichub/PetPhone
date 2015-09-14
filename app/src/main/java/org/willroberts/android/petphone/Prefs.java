package org.willroberts.android.petphone;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity{
	private static final String OPT_VIBRATE = "Vibrate";
	private static final boolean OPT_VIBRATE_DEF = false;
	private static final String OPT_DEFAULTIMG = "useDefaultImg";
	private static final boolean OPT_DEFAULTIMG_DEF = true;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	/** Get the value of the vibrate option **/
	public static boolean getVibrate(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_VIBRATE,OPT_VIBRATE_DEF);
		
	}
	public static boolean getuseDefaultImg(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_DEFAULTIMG,OPT_DEFAULTIMG_DEF);
		
	}
}
