package rs.direktnoizbaste.dizb.web_requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

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

import rs.direktnoizbaste.dizb.R;
import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.callback_interfaces.WebRequestCallbackInterface;

/**
 * Created by 1 on 2/11/2016.
 */
public class PullSensorPlantsRequest {
    WebRequestCallbackInterface webRequestCallbackInterface;
    private JSONObject[] jsonObjects;
    Context _context;

    public PullSensorPlantsRequest(Context context) {
        this._context = context;
        webRequestCallbackInterface = null;
    }

    public void setCallbackListener(WebRequestCallbackInterface listener) {
        this.webRequestCallbackInterface = listener;
    }

    /**
     * function to pull sensor list form web server
     */
    public void pullPlantList() {
        // Tag used to cancel the request
        String tag_string_req = "req_pull_sensor_plants";


        String url = AppConfig.URL_SENSOR_PLANTS_GET;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.i("pullSensorResp:", "Nije null");
                    Log.i("pullSensorResp:", response);
                } else {
                    Log.d("pullSensorResp", "NULL RESPONSE");
                }

                try {

                    JSONObject jObj = new JSONObject(response);

                    if (!jObj.isNull("kulture")) {
                        JSONArray jsonArray = jObj.getJSONArray("kulture");
                        //create Array of JSON objects

                        jsonObjects = new JSONObject[1];
//                        for (int i = 0; i < jArr.length(); i++) {
//                            jsonObjects[i] = jArr.getJSONObject(i);
//                        }

                        jsonObjects[0] = jObj;

                        // store JsonObject to preferences
                        SharedPreferences pref;
                        SharedPreferences.Editor editor;
                        pref = PreferenceManager.getDefaultSharedPreferences(_context);
                        editor = pref.edit();

                        editor.putString(_context.getString(R.string.KEY_PLANTS), jsonArray.toString());
                        editor.commit();

                        webRequestCallbackInterface.webRequestSuccess(true, jsonObjects);

                    } else {
                        webRequestCallbackInterface.webRequestSuccess(false, jsonObjects);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                webRequestCallbackInterface.webRequestError(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
