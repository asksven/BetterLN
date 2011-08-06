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

package com.asksven.betterln.devices;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Device definition for SGS2
 * using ninpo's kernel 2.04+
 * @author sven
 */
public class SamsungSgs2 extends AbstractDevice 
{
	/** the LED file */
	private static final String LED_BLN = "/sys/devices/virtual/misc/backlightnotification/notification_led";
	
	/** the file to control BLN */
	private static final String ENABLE_BLN = "/sys/devices/virtual/misc/backlightnotification/enabled";

	/**
	 * Issue a LED notification and switch it off (or not) depending on settings 
	 */
	protected void notifyLed()
	{
		
		writeLed(LED_BLN, 1);
		
		// simple case: turn off after 10 seconds (if user does not do before by turning the phone on) 
		Timer timer = new Timer();
    	
    	timer.schedule(
    			new TimerTask()
    			{
			        public void run()
			        {
			        	
			        	SamsungSgs2.this.cancelNotifyLed();
			        }
			     }, 20*1000);
	}

	
	protected void cancelNotifyLed()
	{
		this.writeLed(LED_BLN, 0);
	}

	public void enableBln(boolean bEnabled)
	{
		if (bEnabled)
		{
			this.writeLed(ENABLE_BLN, 1);
		}
		else
		{
			this.writeLed(ENABLE_BLN, 0);
		}
	}
}
