package id.web.proditipolines.amop.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.base.AppController;

import static id.web.proditipolines.amop.util.AppConstans.TAG_ID_POHON;
import static id.web.proditipolines.amop.util.AppConstans.TAG_KEGIATAN;
import static id.web.proditipolines.amop.util.AppConstans.TAG_KETERANGAN;
import static id.web.proditipolines.amop.util.AppConstans.TAG_NO;
import static id.web.proditipolines.amop.util.AppConstans.TAG_QRCODE;
import static id.web.proditipolines.amop.util.Server.URL_INSERT_HISTORY;
import static id.web.proditipolines.amop.util.Server.URL_UPDATE_HISTORY;

public class InputHistoryActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static TextView txtQrCode;
    Button tblSimpan;
    EditText txt_keterangan;
    TextView txt_No, txt_idPohon;
    Spinner sKegiatan;
    int success;

    private static final String TAG = InputHistoryActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_history);

        ActionBar menu = getSupportActionBar();
        if (menu != null) {
            menu.setDisplayShowHomeEnabled(true);
            menu.setDisplayHomeAsUpEnabled(true);
            menu.setTitle("Form History Pohon");
        }

        tblSimpan = (Button) findViewById(R.id.tblSimpan);
        txtQrCode = (TextView) findViewById(R.id.txtQrCode);
        Button tblQrScanner = (Button) findViewById(R.id.tblQrScanner);
        sKegiatan = (Spinner) findViewById(R.id.kegiatan);
        txt_keterangan = (EditText) findViewById(R.id.keterangan);
        txt_No = (TextView) findViewById(R.id.txtNo);
        txt_idPohon = (TextView) findViewById(R.id.txtIdPohon);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            txt_No.setText(bundle.getString(TAG_NO));
            txt_idPohon.setText(bundle.getString(TAG_ID_POHON));
            sKegiatan.setSelection(((ArrayAdapter<String>) sKegiatan.getAdapter()).getPosition(bundle.getString(TAG_KEGIATAN)));
            txt_keterangan.setText(bundle.getString(TAG_KETERANGAN));
            txtQrCode.setText(bundle.getString(TAG_QRCODE));
            tblSimpan.setText("Update");
        } else {
            tblSimpan.setText("Simpan");
        }

        tblQrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputHistoryActivity.this, Scanner2Activity.class);
                startActivity(intent);
            }
        });

        tblSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simppanUpdate();
            }
        });

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

    private void simppanUpdate() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (getIntent().getExtras() != null) {
            url = URL_UPDATE_HISTORY;
        } else {
            url = URL_INSERT_HISTORY;
        }
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("v Add", jObj.toString());

                                Toast.makeText(InputHistoryActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                kosong();

                            } else {
                                Toast.makeText(InputHistoryActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(InputHistoryActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<>();

                //menambah parameter yang di kirim ke web servis
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (getIntent().getExtras() != null) {
                    params.put(TAG_NO, txt_No.getText().toString().trim());
                    params.put(TAG_ID_POHON, txt_idPohon.getText().toString().trim());
                    params.put(TAG_KEGIATAN, sKegiatan.getSelectedItem().toString().trim());
                    params.put(TAG_KETERANGAN, txt_keterangan.getText().toString().trim());
                } else {
                    params.put(TAG_KEGIATAN, sKegiatan.getSelectedItem().toString().trim());
                    params.put(TAG_KETERANGAN, txt_keterangan.getText().toString().trim());
                    params.put(TAG_QRCODE, txtQrCode.getText().toString().trim());
                }

                //kembali ke parameters
                Log.d(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void kosong() {
        txt_No.setText(null);
        txt_idPohon.setText(null);
        txt_keterangan.setText(null);
        txtQrCode.setText(null);
    }
}
