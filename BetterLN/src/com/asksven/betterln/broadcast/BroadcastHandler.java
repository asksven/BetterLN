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

package com.asksven.betterln.broadcast;

import java.util.Timer;
import java.util.TimerTask;

import com.asksven.betterln.EffectsService;
import com.asksven.betterln.manager.EffectsFassade;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * General broadcast handler: handles event as registered on Manifest
 * @author sven
 *
 */
public class BroadcastHandler extends BroadcastReceiver
{
	private static final String ACTION_CALL 		= "android.intent.action.PHONE_STATE";
	private static final String ACTION_MAIL 		= "com.fsck.k9.intent.action.EMAIL_RECEIVED";
	private static final String ACTION_IM 			= "com.asksven.xtremepp.intent.action.MESSAGE_RECEIVED";
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{

        final EffectsFassade myEffectsMgr = EffectsFassade.getInstance(context);
        
        if ((intent.getAction().equals(ACTION_MAIL)))
        {
        	Log.d(getClass().getSimpleName(), "Received Broadcast ACTION_MAIL");
        	myEffectsMgr.notifyMail();
        }
        
        if ((intent.getAction().equals(ACTION_IM)))
        {
        	// todo: retrieve extras from Intent (from and message)
        	Log.d(getClass().getSimpleName(), "Received Broadcast ACTION_IM");
        	
        	myEffectsMgr.notifyIM();
        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			Log.i(getClass().getSimpleName(), "Received Broadcast ACTION_BOOT_COMPLETED");
			
			// retrieve default selections for spinners
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			boolean bAutostart = sharedPrefs.getBoolean("autostart", false);
			
			if (bAutostart)
			{
				Log.i(getClass().getSimpleName(), "Autostart is set so run, starting service");
				context.startService(new Intent(context, EffectsService.class));
			}
		}

		// Events for call
		// Incoming Call 
		if (intent.getAction().equals(ACTION_CALL))
		{
			Log.d(getClass().getSimpleName(), "Received Broadcast ACTION_CALL");
			String phoneState = intent.getExtras().getString("state");

			
			if (phoneState.equals(android.telephony.TelephonyManager.EXTRA_STATE_RINGING))
			{
				Log.d(getClass().getSimpleName(), "Received Broadcast EXTRA_STATE_RINGING");
				myEffectsMgr.setNotifyCallPending(true);
				// myEffectsMgr.notifyCall();
			}
			else if (phoneState.equals(android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK))
			{
				Log.d(getClass().getSimpleName(), "Received Broadcast EXTRA_STATE_OFFHOOK");
				myEffectsMgr.setNotifyCallPending(false);
				//myEffectsMgr.cancelNotify();
			}
			
		}
	}
}
