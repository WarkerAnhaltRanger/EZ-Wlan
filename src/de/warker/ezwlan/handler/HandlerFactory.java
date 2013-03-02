package de.warker.ezwlan.handler;

import java.util.HashMap;

import android.net.wifi.ScanResult;


public class HandlerFactory {
	
	// should use DI for that
	private static final HashMap<String, IWlanKeyHandler> mac_2_handler_map = new HashMap<String, IWlanKeyHandler>();
	
	static{
		add_handler(new ArcadyanWlanKeyHandler());
		add_handler(new PbsWlanKeyHandler());
		add_handler(new AliceSiemensWlanHandler());
	}
	
	public static void add_handler(IWlanKeyHandler handler){
		for(String mac : handler.getSupportedMacs()){
			mac_2_handler_map.put(bssid_helper(mac), handler);
		}
	}
	
	public static boolean mac_supported(ScanResult sr){
		String m = sr.BSSID.toUpperCase().replace(":", "").replace("-", "").trim().substring(0, 6);
		return mac_2_handler_map.containsKey(m);
	}
	
	public static boolean possibly_know_the_key(ScanResult sr){
		return get_handler(sr.BSSID).gotPossibleKey(sr);
	}
	
	public static IWlanKeyHandler get_handler(String mac){
		return mac_2_handler_map.get(bssid_helper(mac).substring(0, 6));
	}
	
	public static String bssid_helper(String mac){
		return mac.toUpperCase().replace(":", "").replace("-", "").replace(" ", "").trim();
	}
	
	public static String get_key(ScanResult sr){
		String mac = bssid_helper(sr.BSSID);
		IWlanKeyHandler handler = get_handler(mac);
		if(handler != null){
			return handler.getKey(sr);
		}
		return null;
	}

}
