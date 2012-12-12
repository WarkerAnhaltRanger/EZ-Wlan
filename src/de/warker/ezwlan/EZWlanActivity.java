package de.warker.ezwlan;

import java.util.Date;
import java.util.Timer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import de.warker.ezwlan.list.WlanApEntry;
import de.warker.ezwlan.list.WlanListAdapter;

public class EZWlanActivity extends Activity implements OnClickListener{

	/**
	 * PLEASE: THIS SOFTWARE IS FOR SECURITY TESTING PRUPORSES ONLY!
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


	private WlanListAdapter list;

	private BroadcastReceiver broadcast_receiver;

	public static WifiManager wifi;
	public static Timer autoscan_timer;
	public static Settings settings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		settings = new Settings(getResources());

		ListView lv = (ListView) findViewById(R.id.listView1);
		list = new WlanListAdapter(this);
		lv.setAdapter(list);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(!wifi.isWifiEnabled()){
			Toast.makeText(getApplicationContext(), "Wifi off, activiating!", Toast.LENGTH_LONG).show();
			wifi.setWifiEnabled(true);
		}

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);

		autoscan_timer = new Timer();
		autoscan_timer.schedule(new EzWlanAutoScanTask(), new Date(), settings.getAutoscan_period());

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId() == R.id.Settings){
			startActivity(new Intent(this, EzWlanSettingActivity.class));
			//finish();
			return false;
		}
		return super.onMenuItemSelected(featureId, item); 
	}

	@Override
	public void onClick(View v) {
		wifi.startScan();
	}

}