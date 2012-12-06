package de.warker.ezwlan;

import java.util.Date;
import java.util.Timer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EzWlanSettingActivity extends Activity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ez_wlan_setting);

		final SeekBar as_period_sb = (SeekBar)findViewById(R.id.scan_period_slider);
		final TextView as_val_tv = (TextView)findViewById(R.id.autoscan_value);

		as_period_sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser){
					if(progress >= 1000){
						as_val_tv.setText(progress+" ms");
					}
					else {
						as_val_tv.setText("Aus");
					}
					EZWlanActivity.settings.setAutoscan_period(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				EZWlanActivity.autoscan_timer.cancel();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//EZWlanActivity.autoscan_timer.cancel();
				int progress = seekBar.getProgress();
				EZWlanActivity.settings.setAutoscan_period(progress);
				if(progress >= 1000){
					EZWlanActivity.autoscan_timer = new Timer();
					EZWlanActivity.autoscan_timer.schedule(new EzWlanAutoScanTask(), 
							new Date(), progress);
				}
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ez_wlan_setting, menu);
		return true;
	}*/

}
