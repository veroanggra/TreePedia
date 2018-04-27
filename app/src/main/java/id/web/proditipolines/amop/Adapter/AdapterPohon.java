package id.web.proditipolines.amop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.model.DataPohon;

import static id.web.proditipolines.amop.util.Server.URL_MONITOR;

public class AdapterPohon extends BaseAdapter {
    private Activity activity;
    private List<DataPohon> items;

    public AdapterPohon(Activity activity, List<DataPohon> items) {
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_row_pohon, null);

        ImageView foto_pohon = (ImageView) convertView.findViewById(R.id.imageView);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView last_update = (TextView) convertView.findViewById(R.id.last_update);
        TextView jenis_pohon = (TextView) convertView.findViewById(R.id.jenis_pohon);
        TextView usia_pohon = (TextView) convertView.findViewById(R.id.usia_pohon);
        TextView kondisi_pohon = (TextView) convertView.findViewById(R.id.kondisi_pohon);

        DataPohon dataPohon = items.get(position);

        id.setText(dataPohon.getId());
        last_update.setText(dataPohon.getLast_update());
        jenis_pohon.setText(dataPohon.getJenis_pohon());
        usia_pohon.setText(dataPohon.getUsia_pohon());
        kondisi_pohon.setText(dataPohon.getKondisi_pohon());
        String url = URL_MONITOR + dataPohon.getFoto_pohon();
        Picasso.with(activity).load(url).error(R.mipmap.ic_launcher).into(foto_pohon);
        return convertView;
    }
}

