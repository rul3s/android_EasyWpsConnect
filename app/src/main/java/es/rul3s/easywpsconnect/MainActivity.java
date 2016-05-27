package es.rul3s.easywpsconnect;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public Context context;
    public static WifiManager wifimgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        wifimgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        checkWifi();
    }

    private void checkWifi(){
        if(!wifimgr.isWifiEnabled()){
            Toast.makeText(this,"Wifi disabled, enabling...", Toast.LENGTH_SHORT).show();
            wifimgr.setWifiEnabled(true);
        }
    }

    public void btConnect(View view){
        String bssid = ((EditText)findViewById(R.id.main_etBSSID)).getText().toString();
        String wpspin = ((EditText)findViewById(R.id.main_etWPSPIN)).getText().toString();

        Toast.makeText(this,"Trying to connect...\nBSSID: " +bssid +"\nPIN: " +wpspin,Toast.LENGTH_SHORT).show();

        WpsInfo wpsConfig = new WpsInfo();
        wpsConfig.BSSID = bssid;
        wpsConfig.pin = wpspin;
        wifimgr.startWps(wpsConfig, mWpsCallback);
    }

    private final WifiManager.WpsCallback mWpsCallback = new WifiManager.WpsCallback() {
        @Override
        public void onStarted(String pin) {
            Toast.makeText(context,"Trying pin " +pin,Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSucceeded() {
            //mWpsComplete = true;
            Toast.makeText(context,"Connection Succeed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(int reason) {
            //mWpsComplete = true;
            String errorMessage;
            Toast.makeText(context,"REASON: " +reason,Toast.LENGTH_SHORT).show();
            switch (reason) {

                case WifiManager.WPS_OVERLAP_ERROR:
                    Toast.makeText(context,"Failed, WPS_OVERLAP_ERROR",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_overlap);
                    break;
                case WifiManager.WPS_WEP_PROHIBITED:
                    Toast.makeText(context,"Failed, WPS_WEP_PROHIBITED",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_wep);
                    break;
                case WifiManager.WPS_TKIP_ONLY_PROHIBITED:
                    Toast.makeText(context,"Failed, WPS_TKIP_ONLY_PROHIBITED",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_tkip);
                    break;
                case WifiManager.WPS_AUTH_FAILURE:
                    Toast.makeText(context,"Failed, AUTH_FAILURE",Toast.LENGTH_SHORT).show();
                    //mWifiManager.cancelWps(null);
                    //startWps();
                    return;
                case WifiManager.WPS_TIMED_OUT:
                    Toast.makeText(context,"Failed, WPS_TIMED_OUT",Toast.LENGTH_SHORT).show();
                    //startWps();
                    return;
                default:
                    Toast.makeText(context,"Failed, wifi_wps_failed_generic",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_generic);
                    break;
            }
            //displayFragment(createErrorFragment(errorMessage), true);
        }
    };
}
