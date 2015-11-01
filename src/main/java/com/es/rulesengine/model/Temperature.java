package com.es.rulesengine.model;

public class Temperature {
	
	public double TEMP;
	public double MAX_TEMP;
	public double MIN_TEMP;
	
	public Relay relay;
	
	public void setRelayOFF()
	{
		relay.setRelayOFF();
	}
	
	public void setRelayON()
	{
		relay.setRelayON();
	}
	
	public Relay getRelay()
	{
		return relay;
	}
	
	public void setRelay(Relay newRelay)
	{
		relay = newRelay;
	}
	
	public double getMaxTemp()
	{
		return this.MAX_TEMP;
	}
	
	public double getMinTemp()
	{
		return this.MIN_TEMP;
	}
	
	public void setMaxTemp(double max_temp)
	{
		this.MAX_TEMP = max_temp;
	}
	
	public void setMinTemp(double min_temp)
	{
		this.MIN_TEMP = min_temp;
	}
	

}
