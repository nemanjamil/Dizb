package rs.direktnoizbaste.dizb.web_requests;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rs.direktnoizbaste.dizb.R;
import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.app.WebAppInterface;
import rs.direktnoizbaste.dizb.dialogs.ProgressDialogCustom;

/**
 * Created by 1 on 1/29/2016.
 */
public class PullGraphDataRequest {

    private WebView browser;
    private WebAppInterface webAppInterface;

    private Context context;
    private ProgressDialogCustom progressDialog;

    public PullGraphDataRequest(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialogCustom(context);
        browser = (WebView) context.findViewById(R.id.webView);
        webAppInterface = new WebAppInterface(context);
    }

    /**
     * function to pull data for showing graphs form web server
     */
    public void pullGraphData(final String uid, final String mac) {
        // Tag used to cancel the request
        String tag_string_req = "req_pull_graphs";
        progressDialog.showDialog("Učitavam grafike...");

        String url = String.format(AppConfig.URL_GRAPHS_DATA_GET, uid, mac);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.hideDialog();
                if (response != null) {
                    Log.d("RESPONSE", "Nije null");
                    Log.d("RESPONSE", response);
                } else {
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
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "povuciPodatkeSenzorId");
                params.put("id", uid);
                params.put("string", mac);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
