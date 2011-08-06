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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Fassade to simple sensor operations
 * @author sven
 *
 */
public class LightSensorFassade implements SensorEventListener
{

	private final SensorManager mSensorManager;
	private static String TAG = "SensorFassade";
	private final Sensor m_lightSensor;
	private final Context m_context;
	
	private static float THR_DARK = 200; 
	float m_fCurrentLux = Float.MIN_VALUE;

	public LightSensorFassade(Context context) 
	{
		m_context = context;
	    mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
	    m_lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	    mSensorManager.registerListener((SensorEventListener) this, m_lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    Log.i(TAG, "registered Listener");
}


	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
	    if(sensor.getType() == Sensor.TYPE_LIGHT)
	    {
	    }
	}

	/**
	 * When sensor delivers one value we store it and unregister
	 */	
	public void onSensorChanged(SensorEvent event) 
	{
	    mSensorManager.unregisterListener((SensorEventListener) this);
	    Log.i(TAG, "unregistered Listener");
	    
		if( event.sensor.getType() == Sensor.TYPE_LIGHT)
	    {
			float[] vals;
			vals = event.values;
			m_fCurrentLux = vals[0];
	    }
	}
	
	/** 
	 * returns true if the sensor is covered (e.g. in pocket or pouch)
	 * @return true if sensor is covered
	 */
	public boolean getDarkened()
	{
		boolean bRet = false;
		
		if ( (m_fCurrentLux > Float.MIN_VALUE) && (m_fCurrentLux < THR_DARK) )
		{
			bRet = true;
		}
		return bRet;
	}
}
