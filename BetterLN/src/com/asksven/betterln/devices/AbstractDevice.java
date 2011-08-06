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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Abstract class to be extended by each device
 * Defines public methods to be overridden by each phone
 * and provides basic handling methods
 * @author sven
 *
 */
public abstract class AbstractDevice
{
	/** The thread responsible for blinking */
	private BlinkTask m_blinker;
	
	/**
	 * Must be overridden by each device to implement
	 * a simple blink notification 
	 */
	protected void notifyLed()
	{
		
	}
	
	/** must be overridden by every device to cancel 
	 * all notifications
	 */
	protected void cancelNotifyLed()
	{
		
	}
	
	/**
	 * Enables or disabled BLN
	 * @param bEnabled
	 */
	public void enableBln(boolean bEnabled)
	{
		
	}
	
	/** Applies an effect (color if available) to the LED */ 
	protected static void writeLed(String strEffectFile, int iEffect)
	{
		try
		{
			Log.d("Device.writeLed", "Writing effect " + iEffect + " to filesystem");
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
	
	/**
	 * Notifies a BlinkerPattern
	 * @param blinker a BlinkerPattern
	 */
	public void notify(BlinkerPattern blinker)
	{
		m_blinker = new BlinkTask();
		m_blinker.execute(blinker);
	}
	
	/** 
	 * Cancels a running notification pattern
	 */
	public void cancelNotify()
	{
		if (m_blinker != null)
		{
			m_blinker.cancel(true);
		}
	}
	
	/** 
	 * The task (thread) responsible for blinking
	 * @author sven
	 * @see http://code.google.com/p/makemachine/source/browse/trunk/android/examples/async_task/src/makemachine/android/examples/async/AsyncTaskExample.java
	 * for more details
	 */
	protected class BlinkTask extends AsyncTask<BlinkerPattern, Integer, Integer>
    {
        // -- run intensive processes here
        // -- notice that the datatype of the first param in the class definition matches the param passed to this method 
        // -- and that the datatype of the last param in the class definition matches the return type of this mehtod
        @Override
        protected Integer doInBackground( BlinkerPattern... blinker ) 
        {
                //-- on every iteration
                //-- runs a while loop that causes the thread to sleep for 50 milliseconds 
                //-- publishes the progress - calls the onProgressUpdate handler defined below
                //-- and increments the counter variable i by one
                int i =  0;
                while( i < blinker[0].m_iTimes ) 
                {
                        try{
                                Thread.sleep( blinker[0].m_iMillisOff );
                                blinker[0].m_device.notifyLed();
                                                                
                                Thread.sleep( blinker[0].m_iMillisOn );
                                blinker[0].m_device.cancelNotifyLed();
                                i++;
                        } catch( Exception e ){
                                Log.i("makemachine", e.getMessage() );
                        }
                }
                return 1;
        }
                
        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
                
        }
        
        // -- called from the publish progress 
        // -- notice that the datatype of the second param gets passed to this method
        @Override
        protected void onProgressUpdate(Integer... values) 
        {
                super.onProgressUpdate(values);
        }
        
        // -- called if the cancel button is pressed
        @Override
        protected void onCancelled()
        {
        	super.onCancelled();
        	
        }

        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute( Integer result ) 
        {
                super.onPostExecute(result);
        }
    }    
}
