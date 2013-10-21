package de.warker.ezwlan.handler;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.net.wifi.ScanResult;
import de.warker.ezwlan.helper.Base64Coding;

public class AliceSiemensWlanHandler extends AbstractWlanKeyHandler {
	
	public AliceSiemensWlanHandler() {
		SUPPORTED_MACS.add("00:25:5e");
		SUPPORTED_SSID.add("alice-wlan");
		SUPPORTED_SSID.add("o2");
	}
	
	@Override
	public String getKey(ScanResult sr) {
		final String mac_wlan = HandlerFactory.bssid_helper(sr.BSSID);
		String mac_eth = String.format("%s%02x", 
				mac_wlan.substring(0, mac_wlan.length()-2), 
				(byte)(Integer.parseInt(mac_wlan.substring(mac_wlan.length()-2), 16)-1));
		mac_eth = mac_eth.toLowerCase();
		System.out.println(mac_eth); // OK
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(mac_eth.getBytes());
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
			return Base64Coding.encode(hashtext).substring(0, 16);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
