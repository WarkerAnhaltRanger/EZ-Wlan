package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

public interface IWlanKeyHandler {
	
	public String[] getSupportedMacs();
	
	public boolean gotPossibleKey(ScanResult sr);
	
	public String getKey(ScanResult sr);

}
