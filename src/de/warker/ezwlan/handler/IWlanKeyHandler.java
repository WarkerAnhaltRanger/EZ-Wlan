package de.warker.ezwlan.handler;

import java.util.Collection;

import android.net.wifi.ScanResult;

public interface IWlanKeyHandler {
	
	public Collection<String> getSupportedMacs();
	
	public boolean gotPossibleKey(ScanResult sr);
	
	public String getKey(ScanResult sr);

}
