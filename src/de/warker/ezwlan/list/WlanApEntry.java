package de.warker.ezwlan.list;

import android.net.wifi.ScanResult;

public class WlanApEntry {
	
	private ScanResult scan_result;
	private String key;
	
	public WlanApEntry(ScanResult scan_result) {
		super();
		this.scan_result = scan_result;
	}
	
	public ScanResult getScan_result() {
		return scan_result;
	}
	
	public void setScan_result(ScanResult scan_result) {
		this.scan_result = scan_result;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
