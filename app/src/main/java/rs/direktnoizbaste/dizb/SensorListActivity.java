package rs.direktnoizbaste.dizb;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import rs.direktnoizbaste.dizb.app.SessionManager;

public class SensorListActivity extends AppCompatActivity {
    ListView listView;
    CustomArrayAdapter customArrayAdapter;
    JSONObject[] jsonObjects;

    private ProgressDialog progressDialog;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        //initializing toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        //initializing views
        listView = (ListView)findViewById(R.id.listView);

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //Pulling sensor list from server
        pullSensorList("1");
    }

    /**
     * function to pull sensor list form web server
     * */
    private void pullSensorList(final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_pull_sensors";
        showDialog("Updating sensor list...");

        String url =  String.format(AppConfig.URL_SENSOR_LIST_GET, uid);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean success = jObj.getBoolean("success");


                    if (success) {
                        //create Array of JSON objects
                        JSONArray jArr= jObj.getJSONArray("senzor");

                        jsonObjects = new JSONObject[jArr.length()];
                        for (int i=0; i<jArr.length(); i++){
                            jsonObjects[i] = jArr.getJSONObject(i);
                        }
                        customArrayAdapter = new CustomArrayAdapter(getBaseContext(), jsonObjects);

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

    private class RawSensorData{

    }

    private class CustomArrayAdapter extends ArrayAdapter<JSONObject> {
        private final Context context;
        private final JSONObject[] values;

        public CustomArrayAdapter(Context context, JSONObject[] values) {
            super(context, R.layout.activity_sensor_list_row_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*TODO use view recycling for smooth scrolling...*/
            View rowView = inflater.inflate(R.layout.activity_sensor_list_row_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            TextView textView_desc = (TextView) rowView.findViewById(R.id.secondLine);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            try {
                textView.setText(values[position].getString("ImeKulture") + " - " + values[position].getString("LokacijaIme"));
                textView_desc.setText(values[position].getString("SenzorSifra"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rowView;
        }
    }
}
