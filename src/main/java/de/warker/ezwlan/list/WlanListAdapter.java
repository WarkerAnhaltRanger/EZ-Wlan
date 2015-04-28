package de.warker.ezwlan.list;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.warker.ezwlan.EZWlanDetailActivity;
import de.warker.ezwlan.R;
import de.warker.ezwlan.R.drawable;
import de.warker.ezwlan.handler.HandlerFactory;

public class WlanListAdapter extends ArrayAdapter<ScanResult> implements OnClickListener {

	private LayoutInflater vi;

	public WlanListAdapter(Context context) {
		super(context, R.layout.row, new ArrayList<ScanResult>());
		vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View v = vi.inflate(R.layout.row, parent, false);
		ScanResult sr = getItem(position);
		if (sr != null) {

			v.setTag(sr);

			ImageView iv = (ImageView) v.findViewById(R.id.dot_image);
			TextView tt = (TextView) v.findViewById(R.id.ssid);
			TextView bt = (TextView) v.findViewById(R.id.mac);

			tt.setText(sr.SSID);
			bt.setText(sr.BSSID);

			if(HandlerFactory.mac_supported(sr)){
				if(HandlerFactory.possibly_know_the_key(sr)){  
					iv.setImageResource(drawable.green_dot);
				}
				else{
					iv.setImageResource(drawable.yellow_dot);
				}

				//bt.setText(bt.getText() +" Key: "+ HandlerFactory.get_key(sr));
				v.setOnClickListener(this);
			}
			else{
				iv.setImageResource(drawable.red_dot);
			}
		}
		return v;
	}

    @Override
    public void onClick(View v) {
        ScanResult sr = (ScanResult)v.getTag();
        Intent i = new Intent(getContext(), EZWlanDetailActivity.class);
        i.putExtra(ScanResult.class.getName(), sr);
        getContext().startActivity(i);
    }
}
