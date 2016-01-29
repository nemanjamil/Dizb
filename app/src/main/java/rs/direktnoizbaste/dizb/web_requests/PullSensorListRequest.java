package rs.direktnoizbaste.dizb.web_requests;

import android.app.Activity;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
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

import rs.direktnoizbaste.dizb.R;
import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.array_adapters.SensorListAdapter;
import rs.direktnoizbaste.dizb.dialogs.ProgressDialogCustom;

/**
 * Created by 1 on 1/29/2016.
 */
public class PullSensorListRequest {
    private Context context;
    private ProgressDialogCustom progressDialog;

    private SensorListAdapter customArrayAdapter;
    private JSONObject[] jsonObjects;

    ListView listView;

    public PullSensorListRequest(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialogCustom(context);
        listView = (ListView) context.findViewById(R.id.listView);
    }

    /**
     * function to pull sensor list form web server
     */
    public void pullSensorList(final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_pull_sensors";
        progressDialog.showDialog(context.getString(R.string.progress_update_sensor_list));

        String url = String.format(AppConfig.URL_SENSOR_LIST_GET, uid);

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
                        //create Array of JSON objects
                        JSONArray jArr = jObj.getJSONArray("senzor");

                        jsonObjects = new JSONObject[jArr.length()];
                        for (int i = 0; i < jArr.length(); i++) {
                            jsonObjects[i] = jArr.getJSONObject(i);
                        }
                        customArrayAdapter = new SensorListAdapter(context, jsonObjects);
                        listView.setAdapter(customArrayAdapter);

                    } else {
                        // login error
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "povuciSensorUid");
                params.put("id", uid);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}
