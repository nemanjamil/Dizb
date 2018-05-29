package rs.direktnoizbaste.dizb.array_adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import rs.direktnoizbaste.dizb.MainTenance;
import rs.direktnoizbaste.dizb.R;
import rs.direktnoizbaste.dizb.SensorDetailActivity;
import rs.direktnoizbaste.dizb.web_requests.kulture.Kulture;

/**
 * Created by brajan on 10/5/2017.
 */

public class KultureAdapter extends ArrayAdapter<Kulture> {
    private String SenzorMac;
    private Dialog dialog;

    private class ViewHolder {
        private ImageView imgKulura, imgMainTenance;
        private TextView txtKulturaNaziv;
        private LinearLayout llOpenDialog;
    }

    public KultureAdapter(@NonNull Context context, List<Kulture> object, String senzorMac, Dialog dialog) {
        super(context, 0, object);

        this.SenzorMac = senzorMac;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_kulture, parent, false);
            SetKultureFields(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SetKultureValues(viewHolder, position);

        return convertView;
    }

    private void SetKultureValues(ViewHolder viewHolder, int position) {
        final Kulture item = getItem(position);

        Glide.with(getContext()).load(item.slikaKulture).into(viewHolder.imgKulura);

        viewHolder.txtKulturaNaziv.setText(item.ImeKulture);

        viewHolder.llOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SensorDetailActivity.class);
                intent.putExtra("SensorMAC", SenzorMac);
                intent.putExtra("KulturaId", item.IdKulture);
                getContext().startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private void SetKultureFields(ViewHolder viewHolder, View convertView) {
        viewHolder.txtKulturaNaziv = (TextView) convertView.findViewById(R.id.txtKulturaNaziv);
        viewHolder.imgKulura = (ImageView) convertView.findViewById(R.id.imgKulura);
        viewHolder.llOpenDialog = (LinearLayout) convertView.findViewById(R.id.llOpenDialog);
    }
}
