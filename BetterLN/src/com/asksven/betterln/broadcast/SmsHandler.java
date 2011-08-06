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

/**
 * 
 */

import java.util.Timer;
import java.util.TimerTask;

import com.asksven.betterln.manager.EffectsFassade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;


/**
 * Specific broadcast handler for SMS-Events.
 * @author sven
 *
 */
public class SmsHandler extends BroadcastReceiver
{
	
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
        final EffectsFassade myEffectsMgr = EffectsFassade.getInstance(context);
        
		Log.d(getClass().getSimpleName(), "Received Broadcast " + intent.getAction());
		myEffectsMgr.notifySMS();
	}
}
