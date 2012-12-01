package de.warker.ezwlan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import de.warker.ezwlan.list.WlanApEntry;
import de.warker.ezwlan.list.WlanListAdapter;

public class EZWlanActivity extends Activity{

	/**
	 * PLEASE: THIS SOFTWARE IS FOR SECURITY PRUPORSES ONLY!
	 * IF YOU DO SOMETHING ILLEGAL WITH IT YOU ARE AT YOUR OWN!
	 * I'M NOT RESPONSIBLE FOR ANYTHING YOU DO WITH IT!
	 */

	/*
	 * ----------------------------------------------------------------------------
	 * "THE BEER-WARE LICENSE" (Revision 42):
	 * <WarkerAnhaltRanger@gmail.com> wrote this file. As long as you retain this notice you
	 * can do whatever you want with this stuff. If we meet some day, and you think
	 * this stuff is worth it, you can buy me a beer in return Warker
	 * 
	 * follow me on Twitter: @warker
	 * ----------------------------------------------------------------------------
	 */


	//private static ArrayAdapter<String> list;
	private static WlanListAdapter list;
	public static WifiManager wifi;
	private static Set<String> scanned_list;

	private static BroadcastReceiver broadcast_receiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ListView lv = (ListView) findViewById(R.id.listView1);
		//list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
		list = new WlanListAdapter(this, R.layout.row, new ArrayList<WlanApEntry>());
		lv.setAdapter(list);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(!wifi.isWifiEnabled()){
			Toast.makeText(getApplicationContext(), "Wifi off, activiating!", Toast.LENGTH_LONG).show();
			wifi.setWifiEnabled(true);
		}

		scanned_list = new HashSet<String>();
		for(WifiConfiguration conf : wifi.getConfiguredNetworks()){
			Log.d("wifi", "Netconf: "+conf);
			scanned_list.add(conf.SSID.replaceAll("\"", " ").trim());
		}

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wifi.startScan();
			}
		});

		broadcast_receiver = new BroadcastReceiver()
		{
			@Override
			public synchronized void onReceive(Context c, Intent intent) 
			{
				list.clear();
				for(ScanResult sr : wifi.getScanResults()){
					list.add(new WlanApEntry(sr));
				}
				list.notifyDataSetChanged();
			}
		};

		registerReceiver(broadcast_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}

	@Override
	protected void onDestroy() {
		wifi.saveConfiguration();
		unregisterReceiver(broadcast_receiver);
		super.onDestroy();
	}
}