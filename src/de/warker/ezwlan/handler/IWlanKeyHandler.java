package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

public interface IWlanKeyHandler {
	
	public String[] getSupportedMacs();
	
	public String getKey(ScanResult sr);

}
