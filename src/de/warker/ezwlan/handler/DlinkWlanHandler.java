package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;


/* Code taken from Router Keygen by Rui Araújo, Luís Fonseca under GPL v3 License
 */

public class DlinkWlanHandler extends AbstractWlanKeyHandler {
	
	public DlinkWlanHandler() {
		SUPPORTED_MACS.add("00:05:5D");
		SUPPORTED_MACS.add("00:0D:88");
		SUPPORTED_MACS.add("00:0F:3D");
		SUPPORTED_MACS.add("00:11:95");
		SUPPORTED_MACS.add("00:13:46");
		SUPPORTED_MACS.add("00:15:E9");
		SUPPORTED_MACS.add("00:17:9A");
		SUPPORTED_MACS.add("00:19:5B");
		SUPPORTED_MACS.add("00:1B:11");
		SUPPORTED_MACS.add("00:1C:F0");
		SUPPORTED_MACS.add("00:1E:58");
		SUPPORTED_MACS.add("00:21:91");
		SUPPORTED_MACS.add("00:22:B0");
		SUPPORTED_MACS.add("00:24:01");
		SUPPORTED_MACS.add("00:26:5A");
		SUPPORTED_MACS.add("14:D6:4D");
		SUPPORTED_MACS.add("1C:7E:E5");
		SUPPORTED_MACS.add("28:10:7B");
		SUPPORTED_MACS.add("34:08:04");
		SUPPORTED_MACS.add("5C:D9:98");
		SUPPORTED_MACS.add("84:C9:B2");
		SUPPORTED_MACS.add("90:94:E4");
		SUPPORTED_MACS.add("AC:F1:DF");
		SUPPORTED_MACS.add("B8:A3:86");
		SUPPORTED_MACS.add("BC:F6:85");
		SUPPORTED_MACS.add("C8:BE:19");
		SUPPORTED_MACS.add("CC:B2:55");
		SUPPORTED_MACS.add("F0:7D:68");
		SUPPORTED_MACS.add("FC:75:16");
		
		SUPPORTED_SSID.add("dlink-");
	}
	

	final static char hash[] =  { 'X', 'r', 'q', 'a', 'H', 'N',
		'p', 'd', 'S', 'Y', 'w', 
		'8', '6', '2', '1', '5'};
	

	@Override
	public String getKey(ScanResult sr) {
		final char[] key = new char[20];
		final String mac = HandlerFactory.bssid_helper(sr.BSSID);
		key[0]=mac.charAt(11);
		key[1]=mac.charAt(0);
		 
		key[2]=mac.charAt(10);
		key[3]=mac.charAt(1);
		
		key[4]=mac.charAt(9);
		key[5]=mac.charAt(2);
		 
		key[6]=mac.charAt(8);
		key[7]=mac.charAt(3);
		
		key[8]=mac.charAt(7);
		key[9]=mac.charAt(4);
		
		key[10]=mac.charAt(6);
		key[11]=mac.charAt(5);
		
		key[12]=mac.charAt(1);
		key[13]=mac.charAt(6);
		
		key[14]=mac.charAt(8);
		key[15]=mac.charAt(9);
		
		key[16]=mac.charAt(11);
		key[17]=mac.charAt(2);
		
		key[18]=mac.charAt(4);
		key[19]=mac.charAt(10);
		char[] newkey = new char[20];
		char t;
		int index = 0;
		for (int i=0; i < 20 ; i++)
		{
			t=key[i];
			if ((t >= '0') && (t <= '9'))
				index = t-'0';
			else
			{
				t=Character.toUpperCase(t);
				if ((t >= 'A') && (t <= 'F'))
					index = t-'A'+10;
				else
				{
					return null;
				}
			}
			newkey[i]=hash[index];
		}
		return String.valueOf(newkey, 0, 20);
	}

}
