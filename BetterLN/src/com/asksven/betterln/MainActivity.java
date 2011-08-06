/*
 * Copyright (C) 2011 asksven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asksven.betterln;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.asksven.betterln.manager.EffectsFassade;
import com.asksven.betterln.manager.SensorFassade;
import com.asksven.betterln.R;

/**
 * The main Activity of LedEffects
 * @author sven
 *
 */
public class MainActivity extends Activity
{
    private boolean m_bIsStarted;
    
    static final int DIALOG_PREFERENCES_ID 	= 0;
    static final int DIALOG_ABOUT_ID 		= 1;
    static final int DIALOG_SETTINGS_ID 	= 2;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set UI stuff
        setContentView(R.layout.main);

                
        // run the underlying service
        startService();
        
    }
    @Override
    /**
     * Called when Activity is paused
     */
    protected void onPause()
    {
    	super.onPause();
    }
    
    @Override
    /**
     * Called when activity is stopped
     */
    public void onStop()
    {
    	super.onStop();
                
    }
    
    @Override
    /**
     * Called after Activity was first created and/or when it resumes
     */
    protected void onResume()
    {
        // cancel any notification that we started by us
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(R.string.app_name);
        
        // when we open the main activity we want all notifications to be cleared
        EffectsFassade.getInstance(this).cancelNotify();

    	super.onResume();                
    }
        
    /**
     * Called only the first time the options menu is displayed.
     * Create the menu entries.
     * Menu adds items in the order shown.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Preferences");

        menu.add("About");
//        SubMenu myGroup = menu.addSubMenu("More...");
//        myGroup.add("Stop Service");
//        myGroup.add("Start Service");
        menu.add("Test");
        menu.add("Sensors");
        
        return true;
    }

    /**
     * handle menu selected
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {

    	if (item.getTitle().equals("Preferences"))
    	{
    		Intent intent = new Intent(this, PreferencesActivity.class);
   			startActivity(intent);
    		return true;
    	}
    	if (item.getTitle().equals("About"))
    	{
    		Intent intent = new Intent(this, AboutActivity.class);
   			startActivity(intent);
    		return true;
    	}

    	else if (item.getTitle().equals("Stop Service"))
    	{
    		stopService();
    		return true;
    	}
   		else if (item.getTitle().equals("Start Service"))
   		{
   			startService();
   			return true;
   		}
   		else if (item.getTitle().equals("Test"))
   		{
   			EffectsFassade.getInstance(this).notifyTest();
   			Toast.makeText(this, "Turn off your device. The test will notify 2 times", Toast.LENGTH_SHORT).show();
   			return true;
   		}
   		else if (item.getTitle().equals("Sensors"))
   		{
   			SensorFassade mySensor = new SensorFassade(this);
   			mySensor.getDarkened();
   			return true;
   		}

    	else
    	{
    		return false;
    	}
    }

    /** 
     * Starts the service 
     */
	private void startService()
	{
		if( m_bIsStarted )
		{
			Toast.makeText(this, "Service already started", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Intent i = new Intent();
			i.setClassName( "com.asksven.betterln", "com.asksven.betterln.EffectsService" );
			startService( i );
			Log.i(getClass().getSimpleName(), "startService()");
			m_bIsStarted = true;
		}
	}

	/**
	 * Stops the service
	 */
	private void stopService()
	{
		if( m_bIsStarted )
		{
			Intent i = new Intent();
			i.setClassName( "com.asksven.ledeffects", "com.asksven.ledeffects.EffectsService" );
			stopService( i );
			Log.i(getClass().getSimpleName(), "stopService()");
			m_bIsStarted = false;
		}
		else
		{
			Toast.makeText(this, "Service already started", Toast.LENGTH_SHORT).show();
		}
	}   
}