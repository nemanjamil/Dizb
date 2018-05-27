package rs.direktnoizbaste.dizb;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

class ViewAdapterSensorDetail extends BaseAdapter {


    TextView opisnotifikacije_tv, dopodaciideal_tv,odpodaciideal_tv,vrednostsenzor_tv,senzortipime_tv,imekulture_tv,vremesenzor_tv;

    private Activity activity;
    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String,String> resultp = new HashMap<>();

    public ViewAdapterSensorDetail(Activity activity, ArrayList<HashMap<String, String>> arraylist) {
        this.activity = activity;
        data = arraylist;
    }


    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {




        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.onerowacitvity_sensor, null);

        resultp = data.get(position);

        // definisemo polja
        opisnotifikacije_tv = (TextView) view.findViewById(R.id.opisnotifikacije_tv);
        odpodaciideal_tv = (TextView) view.findViewById(R.id.odpodaciideal_tv);
        dopodaciideal_tv = (TextView) view.findViewById(R.id.dopodaciideal_tv);
        vrednostsenzor_tv = (TextView) view.findViewById(R.id.vrednostsenzor_tv);
        senzortipime_tv = (TextView) view.findViewById(R.id.senzortipime_tv);
        imekulture_tv = (TextView) view.findViewById(R.id.imekulture_tv);
        vremesenzor_tv = (TextView) view.findViewById(R.id.vremesenzor_tv);


        // GET INFORMATIONS
        String opisnotifikacije_str = resultp.get("OpisNotifikacije");
        String odpodaciideal_str = resultp.get("OdPodaciIdeal");
        String dopodaciideal_str = resultp.get("DoPodaciIdeal");
        String vrednostsenzor_str = resultp.get("vrednostSenzor");
        String senzortipime_str = resultp.get("senzorTipIme");
        String imekulture_str = resultp.get("ImeKulture");
        String vremesenzor_str = resultp.get("vremeSenzor");



        // upucavamo varijable u polja
        opisnotifikacije_tv.setText(opisnotifikacije_str);
        odpodaciideal_tv.setText(odpodaciideal_str);
        dopodaciideal_tv.setText(dopodaciideal_str);
        vrednostsenzor_tv.setText(vrednostsenzor_str);
        senzortipime_tv.setText(senzortipime_str);
        imekulture_tv.setText(imekulture_str);


        //2018-05-26 20:15:37
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(vremesenzor_str);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss"); // MMM dd,yyyy hh:mm a
        String newFormat = formatter.format(testDate).toString();
        //System.out.println(".....Date..."+newFormat);


        //SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", vremesenzor_str); // "HH:mm"
        //String date = dateF.format(Calendar.getInstance().getTime());
        vremesenzor_tv.setText(newFormat);


        return view;
    }
}

