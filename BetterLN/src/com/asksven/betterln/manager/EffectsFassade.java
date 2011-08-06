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

package com.asksven.betterln.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import com.asksven.betterln.MainActivity;
import com.asksven.betterln.R;
import com.asksven.betterln.devices.AbstractDevice;
import com.asksven.betterln.devices.BlinkerPattern;
import com.asksven.betterln.devices.SamsungSgs2;

/**
 * Fassade to all effect-related processing
 * EffectFassade is implemented as Singleton
 * @author sven
 */
public class EffectsFassade
{

	/** the singleton instance */
	private static EffectsFassade m_oFassade = null;
	
	/** The current device */
	private static AbstractDevice m_device = new SamsungSgs2();

	/** The descriptor for a notification pattern */
	private static BlinkerPattern m_blinker = null;
	
	/** A test is pending. It will be notified when the phone goes off */
	private static boolean m_bTestPending = false;
	
	/** A missed call notification is pending. It will be notified when the phone goes off */
	private static boolean m_bNotifyCallPending = false;

	/** we handle only one player */
	private static MediaPlayer m_myPlayer = null;
	
	private static Context m_context = null;

	/** returns the singleton's instance */
	public static EffectsFassade getInstance(Context context)
	{
		if (m_oFassade == null)
		{
			m_oFassade 	= new EffectsFassade();
			m_myPlayer	= new MediaPlayer();
			m_context 	= context;
			m_blinker = new BlinkerPattern();
			m_blinker.setDevice(m_device);
			m_blinker.setTimes(30);
			m_blinker.setMillisOff(1000);
			m_blinker.setMillisOn(1000);

		}
		
		return m_oFassade;
	}
	
	/**
	 * Notified a missed SMS
	 */
	public void notifySMS()
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);
		if ( (sharedPrefs.getBoolean("handle_sms", true))  && (this.getOkForNotify()) )
		{
			m_device.notify(m_blinker);
		}
	}
	
	/**
	 * Applies for a missed call notification (will be applied when the phone goes off)
	 * @param bPending true is a call was missed
	 */
	public void setNotifyCallPending(boolean bPending)
	{
		m_bNotifyCallPending = bPending;
	}

	/**
	 * Returns if there is a missed call notification pending 
	 * @return true if a call was missed
	 */ 
	public boolean isNotifyCallPending()
	{
		return m_bNotifyCallPending;
	}

	/**
	 * notify a missed call
	 */
	public void notifyCall()
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);
		if ( (sharedPrefs.getBoolean("handle_call", true)) && (this.getOkForNotify()) )
		{
			m_device.notify(getBlinker());
		}
	}
	
	/**
	 * notify an incoming mail
	 */
	public void notifyMail()
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);
		if ( (sharedPrefs.getBoolean("handle_mail", true)) && (this.getOkForNotify()) )
		{
			m_device.notify(getBlinker());
		}

		
	}
	
	/**
	 * Notify an incoming IM message
	 */
	public void notifyIM()
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);
		if ( (sharedPrefs.getBoolean("handle_im", true)) && (this.getOkForNotify()) )
		{
			m_device.notify(getBlinker());
		}

	}

	/**
	 * Apply for a test. The test is deferred till the phone is turned off
	 */
	public void notifyTest()
	{
//		m_device.notify(getBlinker());
		m_bTestPending = true;
	
	}

	/**
	 * Callback from PhoneOffHandler to start the test when the phone is turned off
	 */
	public void callbackNotifyTest()
	{
		m_device.notify(getTestBlinker());
		m_bTestPending = false;
	
	}

	
	/**
	 * Indicator of pending tests
	 * @return if a test is pending
	 */
	public boolean isTestPending()
	{
		return m_bTestPending;
	}

	/**
	 * cancel all notifications
	 */
	public void cancelNotify()
	{
		m_device.cancelNotify();
		m_bTestPending = false;
		m_bNotifyCallPending = false;
	}
	
	/**
	 * Enable BLN 
	 * @param bEnabled true to enable, false to disable
	 */
	public void enableBln(boolean bEnabled)
	{
		m_device.enableBln(bEnabled);
	}

	/**
	 * Returns true of BLN should be performed (depends on exceptions from prefs
	 * @return true if BLN shall be performed
	 */
	private boolean getOkForNotify()
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);
		
		LightSensorFassade mySensor = new LightSensorFassade(m_context);
		
		boolean bEnabled = sharedPrefs.getBoolean("enable_bl", true);
		boolean bOkLightSensor = sharedPrefs.getBoolean("disable_if_in_pocket", false) && mySensor.getDarkened();
		boolean bOkUpsideDown = sharedPrefs.getBoolean("disable_if_upside_down", false) && true;

		return bEnabled && bOkLightSensor && bOkUpsideDown;
	}
	
	/** the cctor is private as EffectFassade is a singleton */
	private EffectsFassade()
	{
		
	}
	
	/**
	 * Returns the blinker definition for the notification
	 * @return a BlinkerPattern instance
	 */
	private BlinkerPattern getBlinker()
	{
		
		return m_blinker;
	}
	/**
	 * Returns a tes blinker definition for the notification
	 * @return a BlinkerPattern instance
	 */
	private BlinkerPattern getTestBlinker()
	{
		BlinkerPattern testBlinker = new BlinkerPattern();
		testBlinker.setDevice(m_device);
		testBlinker.setTimes(2);
		testBlinker.setMillisOff(1000);
		testBlinker.setMillisOn(1000);
		
		return testBlinker;
	}

}

