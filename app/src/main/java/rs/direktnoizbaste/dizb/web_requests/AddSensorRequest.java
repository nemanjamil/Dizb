package rs.direktnoizbaste.dizb.web_requests;

import android.app.Activity;
import android.content.Context;

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
 * Created by milan on 1/30/2016.
 */
public class AddSensorRequest {
    private Context context;
    private ProgressDialogCustom progressDialog;
    private WebRequestCallbackInterface webRequestCallbackInterface;
    private SensorListAdapter customArrayAdapter;
    private JSONObject[] jsonObjects;

    //ListView listView;

    public AddSensorRequest(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialogCustom(context);
        progressDialog.setCancelable(false);
        webRequestCallbackInterface = null;
        //listView = (ListView) context.findViewById(R.id.listView);
    }

    public void setCallbackListener(WebRequestCallbackInterface listener) {
        this.webRequestCallbackInterface = listener;
    }

    /**
     * function to pull sensor list form web server
     */
    public void addSensor(final String uid, final String mac, final String kind) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_sensor";
        progressDialog.showDialog(context.getString(R.string.progress_add_sensor_list));

        String url = String.format(AppConfig.URL_ADD_SENSOR_GET, uid, mac, kind);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_SENSOR_POST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.hideDialog();
                if (response != null) {
                    AppConfig.logDebug("RESPONSE", "Nije null");
                    AppConfig.logDebug("RESPONSE", response);
                } else {
                    AppConfig.logDebug("RESPONSE", "NULL RESPONSE");
                }

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean success = jObj.getBoolean("success");
                    jsonObjects = new JSONObject[1];
                    jsonObjects[0] = jObj;

                    if (success) {
                        webRequestCallbackInterface.webRequestSuccess(true, jsonObjects);

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        webRequestCallbackInterface.webRequestSuccess(false, jsonObjects);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hideDialog();
                webRequestCallbackInterface.webRequestError(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                //params.put("action", "dodajSenzorId");
                params.put("id", uid);
                params.put("string", mac);
                //params.put("br", kind);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(AppConfig.DEFAULT_TIMEOUT_MS, AppConfig.DEFAULT_MAX_RETRIES, AppConfig.DEFAULT_BACKOFF_MULT));
        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
