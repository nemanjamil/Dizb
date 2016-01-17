package rs.direktnoizbaste.dizb.wifi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import rs.direktnoizbaste.dizb.R;


public class SensorAPActivity extends AppCompatActivity {
    WifiManager wifiManager;
    boolean wasWiFiEnabled;

    ListView lv;
    String wifis[];
    WifiScanReceiver wifiReciever;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_ap);
        // get Wifi service
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wasWiFiEnabled = wifiManager.isWifiEnabled();

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // enabling wifi
        wifiManager.setWifiEnabled(true);
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReciever, intentFilter);
        super.onResume();
    }

    /*
    function to show dialog
    */
    private void showDialog(String msg) {
        if (!progressDialog.isShowing()){
            progressDialog.setMessage(msg);
            progressDialog.show();
        }

    }

    /*
    function to hide dialog
     */
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            /*TODO Check for intent action*/
            String intentAction = intent.getAction();
            if (wifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intentAction)) {
                List<ScanResult> wifiScanList = wifiManager.getScanResults();
                wifis = new String[wifiScanList.size()];

                for (int i = 0; i < wifiScanList.size(); i++) {
                    wifis[i] = ((wifiScanList.get(i)).toString());
                }
                /* TODO define listView in layout file*/
                //lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
            }else
            if (wifiManager.WIFI_STATE_CHANGED_ACTION.equals(intentAction)){
                int wifi_state = intent.getIntExtra(wifiManager.EXTRA_WIFI_STATE, wifiManager.WIFI_STATE_UNKNOWN);
                if(wifi_state == wifiManager.WIFI_STATE_ENABLING){
                    // show progress dialog
                    showDialog("Enabling WiFi...");
                }else
                if(wifi_state == wifiManager.WIFI_STATE_ENABLED){
                    // hide progress dialog
                    hideDialog();
                    /* TODO fire up scanning*/
                    wifiManager.startScan();
                    showDialog("Searching for sensors...");
                }else
                if(wifi_state == wifiManager.WIFI_STATE_DISABLED){
                    // hide progress dialog
                    hideDialog();
                }
            }
        }
    }

}
