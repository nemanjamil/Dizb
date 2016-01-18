package rs.direktnoizbaste.dizb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.app.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvLogin;
    TextInputLayout fullName;
    TextInputLayout emailRegister;
    TextInputLayout passwordRegister;
    EditText etFullName;
    EditText etEmailRegister;
    EditText etPasswordRegister;
    Button registerButton;

    SessionManager session;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing Views
        registerButton = (Button) findViewById(R.id.register_button);
        fullName = (TextInputLayout) findViewById(R.id.fullname_registerlayout);
        emailRegister = (TextInputLayout) findViewById(R.id.email_registerlayout);
        passwordRegister = (TextInputLayout) findViewById(R.id.password_registerlayout);
        etFullName = (EditText) findViewById(R.id.fullname_register);
        etEmailRegister = (EditText) findViewById(R.id.email_register);
        etPasswordRegister = (EditText) findViewById(R.id.password_register);
        tvLogin = (TextView) findViewById(R.id.tv_signin);

        //setting toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        tvLogin.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in
        if (session.isLoggedIn()) {
            // User is already logged in. Move to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    SensorListActivity.class);
            startActivity(intent);
            finish();
        }

    }

   /*
   function to register user details in mysql database
    */
    private void registerUser(final String name, final String last_name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registracija...");
        showDialog();

        String url = String.format(AppConfig.URL_REGISTER_GET,"register", email, password, name, last_name);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    Log.i("REGISTER_URL", response);
                    JSONObject jObj = new JSONObject(response);
/*TODO fix parsing of response JSON object
{"error_msg":"","tag":"register","error":false,"uid":33,"user":{"KomitentIme":"Milan","KomitentPrezime":"Milan","KomitentUserName":"a","email":"a@b.com","created_at":"2016-01-14 19:03:33"}} */

                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("KomitentIme");
                        String last_name = user.getString("KomitentPrezime");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        AppController.setString(RegisterActivity.this, "uid", uid);
                        AppController.setString(RegisterActivity.this, "name", name);
                        AppController.setString(RegisterActivity.this, "last_name", last_name);
                        AppController.setString(RegisterActivity.this, "email", email);
                        AppController.setString(RegisterActivity.this, "created_at", created_at);

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        if (errorMsg.equals("")) errorMsg = "Server communication error!";
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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "registrujAndroid");
                params.put("tag", "register");
                params.put("komitentime", name);
                params.put("komitentprezime", last_name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.i("REGISTER_URL", strReq.getUrl());

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin:
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
                finish();
            case R.id.register_button:
                String name = etFullName.getText().toString();
                /* TODO */
                String last_name = "PlaceHolder";
                String email = etEmailRegister.getText().toString();
                String password = etPasswordRegister.getText().toString();
/* TODO  Make better filed validation. Name can't contain spaces and/or UTF-8 letters.*/
/* TODO Add separate field for last name*/
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, last_name, email, password);
                } else {
                    Snackbar.make(v, "Unesite podatke!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}
