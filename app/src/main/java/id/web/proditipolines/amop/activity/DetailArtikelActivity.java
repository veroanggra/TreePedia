package id.web.proditipolines.amop.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.model.DataArtikel;

import static id.web.proditipolines.amop.util.AppConstans.TAG_ARTIKEL;
import static id.web.proditipolines.amop.util.Helper.KonversiTanggal;

public class DetailArtikelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar menu = getSupportActionBar();
        if (menu != null) {
            menu.setDisplayShowHomeEnabled(true);
            menu.setDisplayHomeAsUpEnabled(true);
            menu.setTitle("Detail Artikel");
        }

        DataArtikel dataHistory = getIntent().getParcelableExtra(TAG_ARTIKEL);

        TextView titleArtikel = (TextView) findViewById(R.id.titleArtikel);
        TextView tanggalArtikel = (TextView) findViewById(R.id.tanggalArtikel);
        TextView isiArtikel = (TextView) findViewById(R.id.isiArtikel);
        TextView pegawaiArtikel = (TextView) findViewById(R.id.pegawaiArtikel);

        titleArtikel.setText(dataHistory.getNama_artikel());
        tanggalArtikel.setText(KonversiTanggal(dataHistory.getWaktu_artikel()));
        isiArtikel.setText(dataHistory.getTeks_artikel());
        pegawaiArtikel.setText(String.format("By : %s", dataHistory.getNama_pegawai()));
    }

}
