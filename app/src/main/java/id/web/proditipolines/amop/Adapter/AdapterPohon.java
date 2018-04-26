package id.web.proditipolines.amop.Adapter;

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

import id.web.proditipolines.amop.Data.DataPohon;
import id.web.proditipolines.amop.R;

/**
 * Created by user on 01/06/2017.
 */

public class AdapterPohon extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
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

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
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
        String url = "http://monitoringpohon.semarangvice.com/dist/img/pohon/" + dataPohon.getFoto_pohon();
        Picasso.with(activity).load(url).error(R.mipmap.ic_launcher).into(foto_pohon);
        return convertView;
    }
}

