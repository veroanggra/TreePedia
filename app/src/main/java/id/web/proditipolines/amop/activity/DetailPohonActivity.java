package id.web.proditipolines.amop.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.web.proditipolines.amop.R;

import static id.web.proditipolines.amop.util.AppConstans.TAG_ID;
import static id.web.proditipolines.amop.util.AppConstans.TAG_ID_PEGAWAI;
import static id.web.proditipolines.amop.util.AppConstans.TAG_JENIS_POHON;
import static id.web.proditipolines.amop.util.AppConstans.TAG_KETERANGAN;
import static id.web.proditipolines.amop.util.AppConstans.TAG_KONDISI_POHON;
import static id.web.proditipolines.amop.util.AppConstans.TAG_LAST_UPDATE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_LATITUDE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_LONGTITUDE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_QRCODE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_STATUS;
import static id.web.proditipolines.amop.util.AppConstans.TAG_USIA_POHON;
import static id.web.proditipolines.amop.util.Server.URL_MONITOR;

public class DetailPohonActivity extends AppCompatActivity {

    ImageView imageView;
    TextView mId, mIdPegawai, mLastUpdate, mJenisPohon, mUsiaPohon, mKondisiPohon, mLatitude, mLongitude, mKeterangan, mStatus, mQrcode;
    String id;
    public static String idx, id_pegawai, last_update, jenis_pohon, kondisi_pohon, latitude, longitude, usia_pohon, ket, status, code, foto_pohon;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pohon);

        ActionBar menu = getSupportActionBar();
        if (menu != null) {
            menu.setDisplayShowHomeEnabled(true);
            menu.setDisplayHomeAsUpEnabled(true);
            menu.setTitle("Detail Pohon");
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        mId = (TextView) findViewById(R.id.id);
        mIdPegawai = (TextView) findViewById(R.id.id_pegawai);
        mLastUpdate = (TextView) findViewById(R.id.last_update);
        mJenisPohon = (TextView) findViewById(R.id.jenis_pohon);
        mUsiaPohon = (TextView) findViewById(R.id.usia_pohon);
        mKondisiPohon = (TextView) findViewById(R.id.kondisi_pohon);
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mKeterangan = (TextView) findViewById(R.id.keterangan);
        mStatus = (TextView) findViewById(R.id.status);
        mQrcode = (TextView) findViewById(R.id.qrcode);

        Bundle bundle = getIntent().getExtras();
        String statusPohon = "1";
        if (bundle != null) {
            try {
                id = bundle.getString(TAG_ID);
                mId.setText(id);
                mIdPegawai.setText(bundle.getString(TAG_ID_PEGAWAI));
                mLastUpdate.setText(bundle.getString(TAG_LAST_UPDATE));
                mJenisPohon.setText(bundle.getString(TAG_JENIS_POHON));
                mUsiaPohon.setText(bundle.getString(TAG_USIA_POHON));
                mKondisiPohon.setText(bundle.getString(TAG_KONDISI_POHON));
                mLatitude.setText(bundle.getString(TAG_LATITUDE));
                mLongitude.setText(bundle.getString(TAG_LONGTITUDE));
                mKeterangan.setText(bundle.getString(TAG_KETERANGAN));
                String statuspohon = bundle.getString(TAG_STATUS);
                if (statuspohon != null && statuspohon.equals(statusPohon)) {
                    mStatus.setText("Tertanam");
                } else {
                    mStatus.setText("Ditebang");
                }
                mQrcode.setText(bundle.getString(TAG_QRCODE));
                String url = URL_MONITOR + bundle.getString("foto_pohon");
                Picasso.with(DetailPohonActivity.this).load(url).error(R.mipmap.ic_launcher).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mId.setText(idx);
            mIdPegawai.setText(id_pegawai);
            mLastUpdate.setText(last_update);
            mJenisPohon.setText(jenis_pohon);
            mUsiaPohon.setText(usia_pohon);
            mKondisiPohon.setText(kondisi_pohon);
            mLatitude.setText(latitude);
            mLongitude.setText(longitude);
            mKeterangan.setText(ket);
            if (status.equals(statusPohon)) {
                mStatus.setText("Tertanam");
            } else {
                mStatus.setText("Ditebang");
            }
            mQrcode.setText(code);

            String url = "http://monitoringpohon.semarangvice.com/dist/img/pohon/" + foto_pohon;
            Picasso.with(DetailPohonActivity.this).load(url).error(R.mipmap.ic_launcher).into(imageView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
