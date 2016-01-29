package rs.direktnoizbaste.dizb;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.app.WebAppInterface;
import rs.direktnoizbaste.dizb.web_requests.PullGraphDataRequest;

public class GraphActivity extends AppCompatActivity {
    WebView browser;
    WebAppInterface webAppInterface;

    JSONObject[] jsonObjects;
    private ProgressDialog progressDialog;
    private PullGraphDataRequest pgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        browser = (WebView) findViewById(R.id.webView);

        webAppInterface = new WebAppInterface(this);
        browser.addJavascriptInterface(webAppInterface, "Android");
        browser.getSettings().setJavaScriptEnabled(true);

        if (pgd == null){
            pgd = new PullGraphDataRequest(this);
        }
        pgd.pullGraphData("1", "5CCF7F752BA");
    }
}
