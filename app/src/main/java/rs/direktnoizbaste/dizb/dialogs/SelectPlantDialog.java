package rs.direktnoizbaste.dizb.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.direktnoizbaste.dizb.R;

/**
 * Created by 1 on 2/11/2016.
 */
public class SelectPlantDialog extends DialogFragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_plant_title);
        CharSequence[] plants = new CharSequence[0];

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String plants_str = pref.getString(getString(R.string.KEY_PLANTS),"");
        try {

            JSONArray jsonArray = new JSONArray(plants_str);

            plants = new CharSequence[jsonArray.length()];
            JSONObject jObjPlant;
            for (int i=0; i < jsonArray.length(); i++){
                jObjPlant = jsonArray.getJSONObject(i);
                plants[i] = jObjPlant.getString("ImeKulture");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.setItems(plants, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                /* TODO update server with the new sensor data*/
            }
        });
        return builder.create();
    }
}
