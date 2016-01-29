package rs.direktnoizbaste.dizb.web_requests;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import rs.direktnoizbaste.dizb.array_adapters.SensorListAdapter;
import rs.direktnoizbaste.dizb.callback_interfaces.WebRequestCallbackInterface;
import rs.direktnoizbaste.dizb.dialogs.ProgressDialogCustom;

/**
 * Created by 1 on 1/29/2016.
 */
public class DeleteSensorRequest {
    private Context context;
    private ProgressDialogCustom progressDialog;
    WebRequestCallbackInterface webRequestCallbackInterface;

    public DeleteSensorRequest(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialogCustom(context);
        webRequestCallbackInterface = null;
    }

    public void setCallbackListener(WebRequestCallbackInterface listener) {
        this.webRequestCallbackInterface = listener;
    }

    /**
     * function to pull sensor list form web server
     */
    public void deleteSensorRequest(final String uid, final String mac) {
        // Tag used to cancel the request
        String tag_string_req = "req_del_sensor";
        progressDialog.showDialog(context.getString(R.string.progress_delete_sensor));

        String url = String.format(AppConfig.URL_DEL_SENSOR_GET, uid, mac);

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

                        //______||||_____
                        // show snackbar saying delete was successful
                        // show snackbar to enter credentials
                        Activity act =  (Activity)context;
                        Snackbar.make(act.getCurrentFocus(), "Uspešno obrisano!", Snackbar.LENGTH_LONG)
                                .show();
                        webRequestCallbackInterface.webRequestSuccess(true);

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        webRequestCallbackInterface.webRequestSuccess(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.hideDialog();
                webRequestCallbackInterface.webRequestError(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "obrisiSenzorId");
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
