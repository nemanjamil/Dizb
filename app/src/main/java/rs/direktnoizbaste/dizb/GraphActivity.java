package rs.direktnoizbaste.dizb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.json.JSONObject;

import rs.direktnoizbaste.dizb.app.SessionManager;
import rs.direktnoizbaste.dizb.app.WebAppInterface;
import rs.direktnoizbaste.dizb.callback_interfaces.WebRequestCallbackInterface;
import rs.direktnoizbaste.dizb.web_requests.PullGraphDataRequest;

public class GraphActivity extends AppCompatActivity {
    WebView browser;
    WebAppInterface webAppInterface;

    SessionManager session;

    JSONObject[] jsonObjects;
    private ProgressDialog progressDialog;
    private PullGraphDataRequest pgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        browser = (WebView) findViewById(R.id.webView);

        session = new SessionManager(getApplicationContext());
        webAppInterface = new WebAppInterface(this);
        browser.addJavascriptInterface(webAppInterface, "Android");
        browser.getSettings().setJavaScriptEnabled(true);

        if (pgd == null) {
            pgd = new PullGraphDataRequest(this);
        }
        pgd.setCallbackListener(new WebRequestCallbackInterface() {
            @Override
            public void webRequestSuccess(boolean success, JSONObject[] jsonObjects) {
                if (!success) {
                    showSnack("Nije uspelo prikazivanje podataka!");
                }
            }

            @Override
            public void webRequestError(String error) {
                showSnack(error);
            }
        });
        String uid = session.getUID();
        String mac = getIntent().getStringExtra("SensorMAC");
        int br = getIntent().getIntExtra("KulturaId", 0);
        pgd.pullGraphData(uid, mac, String.valueOf(br));
    }

    private void showSnack(String msg) {
        Snackbar.make(browser, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
