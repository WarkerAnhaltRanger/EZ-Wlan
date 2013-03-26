package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

public class TplinkWlanHandler implements IWlanKeyHandler {

	private static final String[] SUPPORTED_MACS = {"F8:D1:11"};

	private static final String[] SUPPORTED_SSID = {"tp"}; 

	@Override
	public String[] getSupportedMacs() {
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


	@Override
	public String getKey(ScanResult sr) {
		final String mac = HandlerFactory.bssid_helper(sr.BSSID);
		return mac.substring(4, mac.length()).toUpperCase();
	}
}
