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


import com.asksven.betterln.broadcast.PhoneOnOffHandler;
import com.asksven.betterln.broadcast.SmsHandler;
import com.asksven.betterln.manager.EffectsFassade;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


/**
 * The LedEffects Service keeps running even if the main Activity is not displayed/never called
 * The Services takes care of always running tasks and of tasks taking place once in the lifecycle
 * without user interaction.
 * @author sven
 *
 */
public class EffectsService extends Service
{
	private NotificationManager mNM;

	private boolean m_bRegistered = false;
	
	/** A broadcast handler that is registered by code */
	private PhoneOnOffHandler m_oScreenHandler = null;

	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder
    {
        EffectsService getService()
        {
            return EffectsService.this;
        }
    }

    @Override
    public void onCreate()
    {
    	Log.i(getClass().getSimpleName(), "onCreate called");

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
        // tried to fix bug http://code.google.com/p/android/issues/detail?id=3259
		// by programmatically registering to the event
        if (!m_bRegistered)
        {
        	m_oScreenHandler = new PhoneOnOffHandler();
            registerReceiver(m_oScreenHandler, new IntentFilter(Intent.ACTION_SCREEN_ON));
            registerReceiver(m_oScreenHandler, new IntentFilter(Intent.ACTION_SCREEN_OFF));
            
            m_bRegistered = true;
        }
   }

    /** 
     * Called when service is started
     */
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(getClass().getSimpleName(), "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        
        // enable BLN
        EffectsFassade.getInstance(this).enableBln(true);
        
        return Service.START_STICKY;
    }
    @Override
    /**
     * Called when Service is terminated
     */
    public void onDestroy()
    {        
    	// unregister the broadcastreceiver
        unregisterReceiver(m_oScreenHandler);
        
        EffectsFassade.getInstance(this).enableBln(true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

}

