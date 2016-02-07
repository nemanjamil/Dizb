package rs.direktnoizbaste.dizb.wifi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rs.direktnoizbaste.dizb.DrawerActivity;
import rs.direktnoizbaste.dizb.LoginActivity;
import rs.direktnoizbaste.dizb.R;
import rs.direktnoizbaste.dizb.array_adapters.SensorAPListAdapter;


public class SensorAPActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    WifiManager wifiManager;
    boolean wasWiFiEnabled;
    boolean wasWiFiEnabledBeforeSensorConfig;
    boolean configuringSensor;
    int wasNetworkId;

    ListView listView;
    Toolbar toolbar;
    WifiScanReceiver wifiReciever = new WifiScanReceiver();

    ProgressDialog progressDialog;

    List<ScanResult> wifiScanList;
    List<ScanResult> wifiSensorAPList;
    List<ScanResult> wifiAPList;
    String ssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_ap);
        listView = (ListView) findViewById(R.id.lv_access_points);
        listView.setOnItemClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // get Wifi service
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wasWiFiEnabled = wifiManager.isWifiEnabled();

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setSupportActionBar(toolbar);
        // enabling wifi

        configuringSensor = false;
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
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        registerReceiver(wifiReciever, intentFilter);
        Log.i("SensorScan", "onResume");
        super.onResume();
    }

    /*
    function to show dialog
    */
    private void showDialog(String msg) {
        if (!progressDialog.isShowing()) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // the list item was clicked
        /* TODO save current Wifi state*/
        showDialog("Povezivanje sa senzorom...");
        wasNetworkId = wifiManager.getConnectionInfo().getNetworkId();
        wasWiFiEnabledBeforeSensorConfig = wifiManager.isWifiEnabled();
        configuringSensor = true;

        ssid = wifiSensorAPList.get(position).SSID;
        final WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"12345678\"";
        config.priority = 999;

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        int networkId = wifiManager.addNetwork(config);
        wifiManager.saveConfiguration();
        //wifiManager.disconnect();
        wifiManager.enableNetwork(networkId, true);
        //wifiManager.reconnect();
        Log.i("OKClicked", "enabling " + ssid);

        String title = getResources().getString(R.string.set_wifi_dialog_title);
        title = title + " " + ssid;
    }

    private void showSnack(String msg) {
        Snackbar.make(listView, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            /*TODO Check for intent action*/
            String intentAction = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intentAction)) {
                Log.i("BroadCastRec", "scan results available");

                wifiScanList = wifiManager.getScanResults();
                wifiSensorAPList = new ArrayList<ScanResult>();

                for (int i = 0; i < wifiScanList.size(); i++) {
                    if (wifiScanList.get(i).SSID.startsWith("SENZOR"))
                        wifiSensorAPList.add(wifiScanList.get(i));
                }

                if (wifiSensorAPList.size() == 0)
                    showSnack("Nije pronađen ni jedan senzor.\nDa li ste prebacili senzor u mod za podešavanje?");

                listView.setAdapter(new SensorAPListAdapter(getApplicationContext(), wifiSensorAPList));
                hideDialog();

            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intentAction)) {
                int wifi_state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                if (wifi_state == WifiManager.WIFI_STATE_ENABLING) {
                    Log.i("BroadCastRec", "wifi enabling");

                    // show progress dialog
                    showDialog("Uključujem WiFi...");
                } else if (wifi_state == WifiManager.WIFI_STATE_ENABLED) {
                    // hide progress dialog
                    Log.i("BroadCastRec", "wifi enabled");

                    hideDialog();

                    wifiManager.startScan();
                    showDialog("Tražim senzore...");

                } else if (wifi_state == WifiManager.WIFI_STATE_DISABLED) {
                    // hide progress dialog
                    Log.i("BroadCastRec", "wifi disabled");

                    hideDialog();
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intentAction)) {
                Log.i("BroadCastRec", "connection change action");
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

                if (info != null) {
                    Log.i("BroadCastRec", info.getState().toString());
                }


                if (info != null && info.isConnected()) {

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    Log.i("BroadCastRec", ssid);
                    Log.i("BroadCastRec", String.valueOf(configuringSensor));
                    Log.i("BroadCastRec", String.valueOf(ssid.startsWith("SENZOR", 1)));

                    if (configuringSensor && ssid.startsWith("SENZOR", 1)) {
                        Log.i("BroadCastRec", "Sakrij dialog");
                        hideDialog();
                        showSnack("Povezan sa senzorom");
                        configuringSensor = false;
                        //Send web request to sensor
                        String url = "http://192.168.4.1/read";
                        // Start new activity to configure sensor
                        Intent config_intent = new Intent(SensorAPActivity.this, SensorConfig.class);
                        startActivity(config_intent);
                        finish();
                    }
                }

                if (info != null && (info.getDetailedState() == NetworkInfo.DetailedState.FAILED)) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    if (configuringSensor && ssid.startsWith("SENZOR", 1)) {
                        Log.i("BroadCastRec", "Sakrij dialog");
                        hideDialog();
                        showSnack("Nije uspelo povezivanje sa senzorom");
                        configuringSensor = false;
                    }
                }
            }
        }
    }
}

