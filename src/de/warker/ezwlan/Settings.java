package de.warker.ezwlan;

import android.content.res.Resources;

public class Settings {
	
	private Resources resources;
	private int autoscan_period;
	
	public Settings(Resources resources) {
		this.resources = resources;
		if(resources != null){
			autoscan_period = resources.getInteger(R.integer.scan_period_ms);
		}
	}

	public int getAutoscan_period() {
		return autoscan_period;
	}

	public void setAutoscan_period(int autoscan_period) {
		this.autoscan_period = autoscan_period;
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

}
