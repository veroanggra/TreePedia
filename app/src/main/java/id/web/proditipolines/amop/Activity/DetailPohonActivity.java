package id.web.proditipolines.amop.Activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.web.proditipolines.amop.R;

public class DetailPohonActivity extends AppCompatActivity {

    ImageView imageView;
    TextView mId, mIdPegawai, mLastUpdate, mJenisPohon, mUsiaPohon, mKondisiPohon, mLatitude, mLongitude, mKeterangan, mStatus, mQrcode;
    String id;
    public static String idx, id_pegawai, last_update, jenis_pohon, kondisi_pohon, latitude, longitude, usia_pohon, ket, status, code, foto_pohon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pohon);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
        menu.setTitle("Detail Pohon");

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

        try{
            id = bundle.getString("id");
        }catch (Exception e){
        }

        if(id != null) {

            mId.setText(bundle.getString("id"));
            mIdPegawai.setText(bundle.getString("id_pegawai"));
            mLastUpdate.setText(bundle.getString("last_update"));
            mJenisPohon.setText(bundle.getString("jenis_pohon"));
            mUsiaPohon.setText(bundle.getString("usia_pohon"));
            mKondisiPohon.setText(bundle.getString("kondisi_pohon"));
            mLatitude.setText(bundle.getString("latitude"));
            mLongitude.setText(bundle.getString("longtitude"));
            mKeterangan.setText(bundle.getString("keterangan"));
            String statuspohon = bundle.getString("status");
            if(statuspohon.equals("1")){
                mStatus.setText("Tertanam");
            }else{
                mStatus.setText("Ditebang");
            }
            mQrcode.setText(bundle.getString("qrcode"));

            String url = "http://monitoringpohon.semarangvice.com/dist/img/pohon/" + bundle.getString("foto_pohon");
            Picasso.with(DetailPohonActivity.this).load(url).error(R.mipmap.ic_launcher).into(imageView);
        }else {
            mId.setText(idx);
            mIdPegawai.setText(id_pegawai);
            mLastUpdate.setText(last_update);
            mJenisPohon.setText(jenis_pohon);
            mUsiaPohon.setText(usia_pohon);
            mKondisiPohon.setText(kondisi_pohon);
            mLatitude.setText(latitude);
            mLongitude.setText(longitude);
            mKeterangan.setText(ket);
            if (status.equals("1")){
                mStatus.setText("Tertanam");
            }else{
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
