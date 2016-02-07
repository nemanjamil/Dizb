package rs.direktnoizbaste.dizb.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "AndroidSources";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_UID = "userID";
    private static final String KEY_SENSORS = "SensorsJSON";

    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getUID() {
        return pref.getString(KEY_UID, "");
    }

    public void setUID(String uid) {
        editor.putString(KEY_UID, uid);
        // commit changes
        editor.commit();
    }

    public void addSensor(String mac, boolean registered) {
        String sensors = pref.getString(KEY_SENSORS, "{\"sensors\":[]}");
        int index = -1;
        try {
            JSONObject jsonObject = new JSONObject(sensors);
            JSONArray jsonArray = jsonObject.getJSONArray("sensors");
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("mac").equals(mac)){
                    index = i;
                }
            }

            if (index > -1){
                // sensor already exists
                // so delete it before adding
                jsonArray.remove(index);
            }
            JSONObject jsonSensor = new JSONObject();
            jsonSensor.put("mac", mac);
            jsonSensor.put("registered", registered);
            jsonArray.put(jsonSensor);
            jsonObject.put("sensors", jsonArray);
            editor.putString(KEY_SENSORS, jsonObject.toString());
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isSensorRegistered(String mac) {
        String sensors = pref.getString(KEY_SENSORS, "{\"sensors\":[]}");
        try {
            JSONObject jsonObject = new JSONObject(sensors);
            JSONArray jsonArray = jsonObject.getJSONArray("sensors");
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("mac").equals(mac)){
                    return jsonArray.getJSONObject(i).getBoolean("registered");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteSensor(String mac) {
        int index = -1;
        String sensors = pref.getString(KEY_SENSORS, "{\"sensors\":[]}");
        try {
            JSONObject jsonObject = new JSONObject(sensors);
            JSONArray jsonArray = jsonObject.getJSONArray("sensors");
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("mac").equals(mac)){
                     index = i;
                }
            }
            if (index > -1){
                jsonArray.remove(index);
                jsonObject.put("sensors", jsonArray);
                editor.putString(KEY_SENSORS, jsonObject.toString());
                editor.commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}