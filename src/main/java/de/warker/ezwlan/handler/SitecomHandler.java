package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;
import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SitecomHandler extends AbstractWlanKeyHandler {
	
	public SitecomHandler() {
		SUPPORTED_MACS.add("00:0C:F6");

		//test
		SUPPORTED_MACS.add("64:D1:A3");
		//debug
		SUPPORTED_MACS.add("aa:bb:cc");

		SUPPORTED_SSID.add("Sitecom");
	}
	
	private final String lt = "123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ"; // without i,I,l,L,o,O,0


	private String inc_bssid_by(String bssid, int val){
		return (bssid.substring(0, bssid.length() - 1) +
				String.format("%02X",
						(Integer.parseInt(bssid.substring(bssid.length() - 1), 16) + val))
						.substring(1))
				.toUpperCase();
	}

	@Override
	public String[] getKeys(ScanResult sr) {

		final List<String> keys = new LinkedList<String>();
		final String bssid = HandlerFactory.bssid_helper(sr.BSSID);
		String [] macs = {
				bssid.toLowerCase(),	// wlm2500
				bssid.toUpperCase(),	// wlm3500 thx to Rui Araújo (issue #2)
				sr.frequency > 2500 ? inc_bssid_by(bssid, 1) : // wlm5500 5ghz
						inc_bssid_by(bssid, 2) // wlm5500 2.4ghz?
		};

		Log.d("DEBUG", "sr.frequency = "+ sr.frequency);

		for(String mac : macs) {

			String wpakey = "";
			// atol
			String t = "0";
			for (int i = 6; i < mac.length(); ++i) {
				char c = mac.charAt(i);
				if (c >= '0' && c <= '9') {
					t += c;
				} else {
					break;
				}
			}

			int var_80 = Integer.parseInt(t);

			// algo
			wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(5)) * (mac.charAt(9) + mac.charAt(3) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(6)) * (mac.charAt(8) + mac.charAt(10) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(3) + mac.charAt(5)) * (mac.charAt(7) + mac.charAt(9) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(7) + mac.charAt(6)) * (mac.charAt(5) + mac.charAt(4) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(7) + mac.charAt(6)) * (mac.charAt(8) + mac.charAt(9) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(5)) * (mac.charAt(3) + mac.charAt(4) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(11) + mac.charAt(4)) * (mac.charAt(6) + mac.charAt(8) + mac.charAt(11))) % lt.length());
			wpakey += lt.charAt(((var_80 + mac.charAt(10) + mac.charAt(11)) * (mac.charAt(7) + mac.charAt(8) + mac.charAt(11))) % lt.length());

			keys.add(wpakey);
		}
		return keys.toArray(new String[0]);
	}
}
