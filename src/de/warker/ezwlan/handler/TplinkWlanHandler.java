package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

public class TplinkWlanHandler extends AbstractWlanKeyHandler {
	
	public TplinkWlanHandler() {
		SUPPORTED_MACS.add("F8:D1:11");
		SUPPORTED_SSID.add("tp");
	}

	@Override
	public String getKey(ScanResult sr) {
		final String mac = HandlerFactory.bssid_helper(sr.BSSID);
		return mac.substring(4, mac.length()).toUpperCase();
	}
}
