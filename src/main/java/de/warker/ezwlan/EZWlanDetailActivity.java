package de.warker.ezwlan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import de.warker.ezwlan.handler.HandlerFactory;
import de.warker.ezwlan.handler.IWlanKeyHandler;

/**
 * Created by warker on 23.02.15.
 */
public class EZWlanDetailActivity extends Activity implements OnItemClickListener, OnClickListener {

    private ScanResult scanResult;
    private TextView SsidTextView;
    private TextView BssidTextView;
    private ListView KeyListView;
    private ArrayAdapter<String> KeyListAdapter;
    private IWlanKeyHandler WlanKeyHandler;
    private Button GuessKeyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datail_layout);

        BssidTextView = (TextView)findViewById(R.id.BssidTextView);
        SsidTextView = (TextView)findViewById(R.id.SsidTextView);
        KeyListView = (ListView)findViewById(R.id.KeyListView);

        scanResult = getIntent().getParcelableExtra(ScanResult.class.getName());
        if(scanResult == null) {
            // something went wrong
        }
        else{
            WlanKeyHandler = HandlerFactory.get_handler(scanResult.BSSID);
            BssidTextView.setText(scanResult.BSSID);
            SsidTextView.setText(scanResult.SSID);
        }

        KeyListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new LinkedList<String>());
        KeyListView.setAdapter(KeyListAdapter);
        KeyListView.setOnItemClickListener(this);

        GuessKeyButton = (Button)findViewById(R.id.GuessKeyButton);
        GuessKeyButton.setOnClickListener(this);
        KeyListAdapter.setNotifyOnChange(false);
    }

    @Override
    public void onClick(View view) {
        new GenerateKeysTask().execute(WlanKeyHandler);
    }

    private class GenerateKeysTask extends AsyncTask<IWlanKeyHandler, Integer, String[]>{

        private ProgressDialog processDialog;

        @Override
        protected void onPreExecute() {
            processDialog = ProgressDialog.show(GuessKeyButton.getContext(),
                    "", "Guessing Keys.. this may take some time", true, false);
            KeyListAdapter.clear();
        }

        @Override
        protected String[] doInBackground(IWlanKeyHandler... iWlanKeyHandlers) {
            if(iWlanKeyHandlers.length > 0 && scanResult != null){ // only supports one yet
                return iWlanKeyHandlers[0].getKeys(scanResult);
            }
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null && strings.length > 0){
                KeyListAdapter.addAll(strings);
            }
            else{
                Toast.makeText(EZWlanDetailActivity.this,
                        "No keys found.. sorry.", Toast.LENGTH_LONG)
                        .show();
            }
            KeyListAdapter.notifyDataSetChanged();
            processDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final String key = (String) KeyListAdapter.getItem(position);
        final ScanResult sr = scanResult;
        if (sr != null) {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + sr.SSID + "\"";
            conf.hiddenSSID = true;
            conf.status = WifiConfiguration.Status.ENABLED;

            if (sr.capabilities.contains("WPA")) {
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            }

            if (sr.capabilities.contains("WPA2")) {
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            }

            if (sr.capabilities.contains("TKIP")) {
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            if (sr.capabilities.contains("CCMP")) {
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            }

            conf.preSharedKey = "\"" + key + "\"";
            int netid = EZWlanActivity.wifi.addNetwork(conf);
            if (EZWlanActivity.wifi.enableNetwork(netid, false)) {
                Toast.makeText(this, "Connecting with Key: " + key, Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(this, "Enable Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
