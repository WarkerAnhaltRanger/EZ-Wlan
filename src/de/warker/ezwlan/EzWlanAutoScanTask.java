package de.warker.ezwlan;

import java.util.TimerTask;

public class EzWlanAutoScanTask extends TimerTask {

	@Override
	public void run() {
		EZWlanActivity.wifi.startScan();
	}

}
