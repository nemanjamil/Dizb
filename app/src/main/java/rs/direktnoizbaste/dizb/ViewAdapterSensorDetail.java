package rs.direktnoizbaste.dizb;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class ViewAdapterSensorDetail extends BaseAdapter { // Ovde moze da ide ListAdapter ili ArrayAdapter ali ne znam za sta sluzi

    String senzorTipIme = "senzorTipIme";

    TextView tvKategName, tvKategLink, tvKategId,tvDaliImaPodKat,tvBrojArtikalaKateg;

    private final Context context;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ViewAdapterSensorDetail(Context viewAdapterSensorDetail, ArrayList<HashMap<String, String>> arraylist) {
        this.context = viewAdapterSensorDetail;
        data = arraylist;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.onerowacitvity_sensor, parent, false);

        resultp = data.get(position);

        tvKategName = (TextView) itemView.findViewById(R.id.tvKategName);
        //tvKategName.setTypeface(null, Typeface.BOLD);

        String nazivKategorije = resultp.get(this.senzorTipIme);
        tvKategName.setText(nazivKategorije);

        return itemView;
    }


}

