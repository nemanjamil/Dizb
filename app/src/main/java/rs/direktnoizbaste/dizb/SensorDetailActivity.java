package rs.direktnoizbaste.dizb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import rs.direktnoizbaste.dizb.app.SessionManager;
import rs.direktnoizbaste.dizb.dialogs.ProgressDialogCustom;

public class SensorDetailActivity extends AppCompatActivity {

    private ProgressDialogCustom progressDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String SensorMAC = null;
        Integer KulturaId = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            SensorMAC = extras.getString("SensorMAC"); // retrieve the data using keyName
            KulturaId = extras.getInt("KulturaId");
        }
        Toast.makeText(this, SensorMAC+" "+KulturaId, Toast.LENGTH_SHORT).show();

        //setting progressDialog
        progressDialog = new ProgressDialogCustom(this);
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        String userID = session.getUID();
        Log.d("testmiki", userID);
        Log.d("testmiki", "aaa");
        getResults(SensorMAC, KulturaId, userID);


    }

    private void getResults(final String sensorMAC, final Integer kulturaId, final String uid){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        progressDialog.showDialog("Getting informations...");
        String url = String.format(AppConfig.URL_LOGIN_GET, "login", sensorMAC, kulturaId,uid);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETINFOPARAMETER_POST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.hideDialog();

                try {

                   Log.d("testmiki", "Usao u TRY");
                   JSONObject jObj = new JSONObject(response);
                   boolean success = jObj.getBoolean("success");

                    if (success) {

                        JSONArray jArr = jObj.getJSONArray("podaciSenzor");

                        for (int i = 0; i < jArr.length(); i++)
                        {
                            String ImeKulture = jArr.getJSONObject(i).getString("ImeKulture");
                            Integer IdListaSenzora = jArr.getJSONObject(i).getInt("IdListaSenzora");
                            Integer IdKulture = jArr.getJSONObject(i).getInt("IdKulture");
                            Integer IdSenzorTip = jArr.getJSONObject(i).getInt("IdSenzorTip");
                            String senzorTipIme = jArr.getJSONObject(i).getString("senzorTipIme");

                        }

                    } else {
                        Log.d("testmiki", "Usao u ERROR");
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
                progressDialog.hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("tag", "login");
                //params.put("email", email);
                //params.put("password", password);

                params.put("action", "getsensordetailactivity");
                params.put("tag", "getdensorbasicinfo");
                params.put("sensormac", sensorMAC);
                params.put("id", uid);
                params.put("kulturaid", String.valueOf(kulturaId));

                Log.d("testmiki", String.valueOf(params));
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
