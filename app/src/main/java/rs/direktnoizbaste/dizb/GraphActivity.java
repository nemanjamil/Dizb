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

public class GraphActivity extends AppCompatActivity {
    WebView browser;
    WebAppInterface webAppInterface;

    JSONObject[] jsonObjects;
    private ProgressDialog progressDialog;

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
        pullGraphData("1");
    }

    /**
     * function to pull data for showing graphs form web server
     * */
    private void pullGraphData(final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_pull_graphs";
        showDialog("Učitavam grafike...");

        String url =  String.format(AppConfig.URL_GRAPHS_DATA_GET, uid, "5CCF7F747A7");

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                if (response!=null){
                    Log.d("RESPONSE", "Nije null");
                    Log.d("RESPONSE", response);
                }else
                {
                    Log.d("RESPONSE", "NULL RESPONSE");
                }

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        webAppInterface.setJsonObject(jObj);
                        browser.addJavascriptInterface(webAppInterface, "Android");
                        browser.loadUrl("file:///android_asset/www/graphs.htm");
                    } else {
                        // login error
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                params.put("action","povuciSensorUid");
                params.put("id", uid);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

}
