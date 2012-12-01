package de.warker.ezwlan.list;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.warker.ezwlan.EZWlanActivity;
import de.warker.ezwlan.R;
import de.warker.ezwlan.R.drawable;
import de.warker.ezwlan.handler.HandlerFactory;

public class WlanListAdapter extends ArrayAdapter<WlanApEntry> implements OnLongClickListener{

	public WlanListAdapter(Context context, int textViewResourceId,
			List<WlanApEntry> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		WlanApEntry entry = getItem(position);
		if (entry != null) {
			
			ScanResult sr = entry.getScan_result();
			v.setTag(entry);

			ImageView iv = (ImageView) v.findViewById(R.id.dot_image);
			TextView tt = (TextView) v.findViewById(R.id.ssid);
			TextView bt = (TextView) v.findViewById(R.id.mac);

			if(tt != null){
				tt.setText(sr.SSID);
			}

			if(bt != null){
				bt.setText(sr.BSSID);
			}
			
			if(iv != null){
				if(HandlerFactory.mac_in_filter(sr)){
					iv.setImageResource(drawable.green_dot);
					bt.setText(bt.getText() +" Key: "+ HandlerFactory.get_key(sr));
					v.setOnLongClickListener(this);
				}
				else{
					iv.setImageResource(drawable.red_dot);
				}
			}

		}
		return v;
	}

	@Override
	public boolean onLongClick(View v) {
		WlanApEntry wlan_entry = (WlanApEntry)v.getTag();
		ScanResult sr = wlan_entry.getScan_result();
		if(sr != null){
			Log.d("wifi", "connect"+ sr);
			WifiConfiguration conf = new WifiConfiguration();
			conf.SSID = "\""+sr.SSID+"\"";
			conf.hiddenSSID = true;
			conf.status = WifiConfiguration.Status.ENABLED;
			if(sr.capabilities.contains("WPA")){
				conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			}
			if(sr.capabilities.contains("WPA2")){
				conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			}
			if(sr.capabilities.contains("TKIP")){
				conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			}

			if(sr.capabilities.contains("CCMP")){
				conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			}
			// do handle with IWlanKeyHandler
			String key = HandlerFactory.get_key(sr);
			//Log.d("ezwlan", "SSID:"+ sr.SSID + " key: "+ key);
			
			conf.preSharedKey = "\""+ key+"\"";
			int netid = EZWlanActivity.wifi.addNetwork(conf);
			if(EZWlanActivity.wifi.enableNetwork(netid, false)){
				Toast.makeText(/*getApplicationContext()*/ getContext(), "Connecting with Key: "+ key, Toast.LENGTH_LONG).show();
				return true;
			}
			else{
				Toast.makeText(/*getApplicationContext()*/ getContext(), "Enable Failed", Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}

}
