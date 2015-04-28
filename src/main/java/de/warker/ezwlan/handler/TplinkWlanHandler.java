package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

import java.util.Collection;
import java.util.LinkedList;

public class TplinkWlanHandler extends AbstractWlanKeyHandler {
	
	public TplinkWlanHandler() {
		SUPPORTED_MACS.add("F8:D1:11");
		SUPPORTED_SSID.add("tp");
	}

	@Override
	public String[] getKeys(ScanResult sr) {
        final String mac = HandlerFactory.bssid_helper(sr.BSSID);
		return new String[]{mac.substring(4, mac.length()).toUpperCase()};
	}
}
