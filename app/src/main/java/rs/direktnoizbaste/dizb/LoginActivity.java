package rs.direktnoizbaste.dizb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.app.SessionManager;
import rs.direktnoizbaste.dizb.dialogs.ProgressDialogCustom;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button registerHere;
    Button signIn;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;

    private ProgressDialogCustom progressDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initializing toolbar
//        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolBar);
        //initializing views
        registerHere = (Button) findViewById(R.id.registerhere_button);
        signIn = (Button) findViewById(R.id.signin_button);
        emailLogin = (TextInputLayout) findViewById(R.id.email_loginlayout);
        passwordLogin = (TextInputLayout) findViewById(R.id.password_loginlayout);
        etEmailLogin = (EditText) findViewById(R.id.email_login);
        etPasswordLogin = (EditText) findViewById(R.id.password_login);
        //setting onclick listeners
        registerHere.setOnClickListener(this);
        signIn.setOnClickListener(this);

        //setting progressDialog
        progressDialog = new ProgressDialogCustom(this);
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        //If the session is logged in move to MainActivity
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * function to verify login details
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        progressDialog.showDialog("Povezivanje...");

        String url = String.format(AppConfig.URL_LOGIN_GET, "login", email, password);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);

                    //String userId= jObj.getString("uid");

                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        // user successfully logged in
                        // Create login session
                        String uid = jObj.getString("uid");

                        session.setLogin(true);
                        session.setUID(uid);

                        // collect user data
                        //"KomitentNaziv":null,"KomitentIme":"Xman","KomitentPrezime":"Xavier","KomitentAdresa":null,"KomitentPosBroj":null,"KomitentMesto":null
                        // ,"KomitentTelefon":null,"KomitentMobTel":null,"KomitentEmail":"x@y.z","KomitentUserName":"x","KomitentTipUsera":1,"KomitentFirma":null,
                        // "KomitentMatBr":null,"KomitentPIB":null,"KomitentFirmaAdresa":null}

                        String naziv = jObj.getString("KomitentNaziv");
                        String ime = jObj.getString("KomitentIme");
                        String prezime = jObj.getString("KomitentPrezime");
                        String adresa = jObj.getString("KomitentAdresa");
                        String posta = jObj.getString("KomitentPosBroj");
                        String mesto = jObj.getString("KomitentMesto");
                        String telefon = jObj.getString("KomitentTelefon");
                        String mobilni = jObj.getString("KomitentMobTel");
                        String email = jObj.getString("KomitentEmail");
                        String username = jObj.getString("KomitentUserName");
                        Integer tip = jObj.getInt("KomitentTipUsera");
                        String firma = jObj.getString("KomitentFirma");
                        String maticni = jObj.getString("KomitentMatBr");
                        String pib = jObj.getString("KomitentPIB");
                        String firma_adresa = jObj.getString("KomitentFirmaAdresa");

                        session.setGeneralName(naziv);
                        session.setName(ime);
                        session.setLastName(prezime);
                        session.setAddress(adresa);
                        session.setZip(posta);
                        session.setCity(mesto);
                        session.setPhone(telefon);
                        session.setMobile(mobilni);
                        session.setEmail(email);
                        session.setUsername(username);
                        session.setUserType(tip);
                        session.setFirmName(firma);
                        session.setFirmId(maticni);
                        session.setFirmPIB(pib);
                        session.setFirmAddress(firma_adresa);

                        // Launching  main activity
                        Intent intent = new Intent(LoginActivity.this,
                                DrawerActivity.class);
                        startActivity(intent);
                        finish();
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

                params.put("action", "povuciPodatkeAndroidKorisnik");
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //on clicking register button move to Register Activity
            case R.id.registerhere_button:
                Intent intent = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.signin_button:
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                // Check for empty data
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // show snackbar to enter credentials
                    Snackbar.make(v, "Unesite podatke!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}
