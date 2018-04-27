package id.web.proditipolines.amop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.model.DataHistory;

public class AdapterHistory extends BaseAdapter {
    private Activity activity;
    private List<DataHistory> items;

    public AdapterHistory(Activity activity, List<DataHistory> items) {
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
        convertView = inflater.inflate(R.layout.list_row_history, null);

        TextView no = (TextView) convertView.findViewById(R.id.no);
        TextView qrcode = (TextView) convertView.findViewById(R.id.qrcode);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
        TextView kegiatan = (TextView) convertView.findViewById(R.id.kegiatan);
        TextView keterangan = (TextView) convertView.findViewById(R.id.keterangan);

        DataHistory dataHistory = items.get(position);

        no.setText(dataHistory.getNo());
        qrcode.setText(dataHistory.getQrcode());
        tanggal.setText(dataHistory.getTanggal());
        kegiatan.setText(dataHistory.getKegiatan());
        keterangan.setText(dataHistory.getKeterangan());
        return convertView;
    }
}
