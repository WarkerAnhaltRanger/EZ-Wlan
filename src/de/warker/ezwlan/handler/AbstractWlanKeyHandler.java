package de.warker.ezwlan.handler;

import java.util.Collection;
import java.util.LinkedList;

import android.net.wifi.ScanResult;

public abstract class AbstractWlanKeyHandler implements IWlanKeyHandler {
	
	protected Collection<String> SUPPORTED_MACS = new LinkedList<String>();
	protected Collection<String> SUPPORTED_SSID = new LinkedList<String>(); 
	
	
	@Override
	public Collection<String> getSupportedMacs() {
		return SUPPORTED_MACS;
	}

	@Override
	public boolean gotPossibleKey(ScanResult sr) {
		final String ssid = sr.SSID.toLowerCase();
		for(String ssid_prefix : SUPPORTED_SSID){
			if(ssid.startsWith(ssid_prefix.toLowerCase())){
				return true;
			}
		}
		return false;
	}

}
