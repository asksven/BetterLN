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

/**
 * A BlinkerPattern defines the number of times the led 
 * will be blinking and its frequency
 * @author sven
 */
public class BlinkerPattern
{
	/** the device we run */
	protected AbstractDevice m_device = null;
	
	/** Number of times to blink */
	protected int m_iTimes = 0;
	
	/** Time the LED will be on */
	protected int m_iMillisOn = 0;
	
	/** time the LED will be off */
	protected int m_iMillisOff = 0;
	
	/**
	 * Returns the device
	 * @return the m_device
	 */
	public AbstractDevice getDevice()
	{
		return m_device;
	}
	/**
	 * Sets the device
	 * @param m_device the m_device to set
	 */
	public void setDevice(AbstractDevice device)
	{
		this.m_device = device;
	}
	
	/**
	 * Returns how many times the notification should blink
	 * @return the m_iTimes
	 */
	public int getTimes()
	{
		return m_iTimes;
	}
	
	/**
	 * Sets how many times the notification should blink
	 * @param m_iTimes the number of times
	 */
	public void setTimes(int iTimes)
	{
		this.m_iTimes = iTimes;
	}
	
	/**
	 * Returns the number of millis the notification should be on
	 * @return the number of millis
	 */
	public int getMillisOn()
	{
		return m_iMillisOn;
	}
	
	/**
	 * Sets the number of millis the notification should be on
	 * @param the number of millis 
	 */
	public void setMillisOn(int iMillisOn)
	{
		this.m_iMillisOn = iMillisOn;
	}

	/**
	 * Returns the number of millis the notification should be on
	 * @return the number of millis
	 */
	public int getMillisOff()
	{
		return m_iMillisOff;
	}

	/**
	 * Sets the number of millis the notification should be on
	 * @param the number of millis 
	 */
	public void setMillisOff(int iMillisOff)
	{
		this.m_iMillisOff = iMillisOff;
	}
}
