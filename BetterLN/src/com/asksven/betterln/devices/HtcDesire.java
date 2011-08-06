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

import java.io.DataOutputStream;
import android.util.Log;

public class HtcDesire
{
	private static final String LED_AMBER = "/sys/devices/platform/leds-microp/leds/amber/brightness";
	private static final String LED_GREEN = "/sys/devices/platform/leds-microp/leds/green/brightness";
	private static final String LED_BLUE = "/sys/devices/platform/leds-microp/leds/blue/brightness";
	private static final String LED_BACKLIGHT =	"/sys/devices/platform/leds-microp/ledsbutton-backlight/brightness";
	
	public static void writeEffect(int iEffect)
	{
		switch (iEffect)
		{
		case 0 : 	// none
			writeEffect(LED_BLUE, 0);
			writeEffect(LED_GREEN, 0);
			writeEffect(LED_AMBER, 0);
			break;

			case 1 : 	// blue
				writeEffect(LED_BLUE, 1);
				writeEffect(LED_GREEN, 0);
				writeEffect(LED_AMBER, 0);
				break;
				
			case 2 : 	// amber
				writeEffect(LED_BLUE, 0);
				writeEffect(LED_GREEN, 0);
				writeEffect(LED_AMBER, 1);
				break;
				
			case 3 :  	// green 
				writeEffect(LED_BLUE, 0);
				writeEffect(LED_GREEN, 1);
				writeEffect(LED_AMBER, 0);
				break;
				
			case 4 :  	// pink 
				writeEffect(LED_BLUE, 1);
				writeEffect(LED_GREEN, 0);
				writeEffect(LED_AMBER, 1);
				break;
				
			case 5 :  	// white 
				writeEffect(LED_BLUE, 1);
				writeEffect(LED_GREEN, 1);
				writeEffect(LED_AMBER, 0);
				break;
				
			default :
				// do nothing
		}
	}
	
	private static void writeEffect(String strEffectFile, int iEffect)
	{
		try
		{
			Log.d("EffectManager.doEffect", "Writing effect " + iEffect + " to filesystem");
			// dirty hack: http://code.google.com/p/market-enabler/wiki/ShellCommands
			Process process = Runtime.getRuntime().exec("sh");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			String strCommand = "echo " + iEffect + " > " + strEffectFile;
			os.writeBytes(strCommand + "\n");
			os.flush();
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
