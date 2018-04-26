package id.web.proditipolines.amop.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import id.web.proditipolines.amop.App.AppController;
import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.Util.Server;
import id.web.proditipolines.amop.Util.helper;

public class InputHistoryActivity extends AppCompatActivity {

    helper help;
    private Button tblQrScanner;
    public static TextView txtQrCode;
    Button tblSimpan;
    EditText txt_keterangan;
    TextView txt_No, txt_idPohon;
    Spinner sKegiatan;
    int success;

    private static final String TAG = InputHistoryActivity.class.getSimpleName();

    private String url_insert = Server.URL + "inserthistory.php";
    private String url_update = Server.URL + "updatehistory.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_history);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
        menu.setTitle("Form History Pohon");


//      sKegiatan.setOnItemSelectedListener(new ItemSelectedListener());

        tblSimpan = (Button) findViewById(R.id.tblSimpan);
        txtQrCode = (TextView) findViewById(R.id.txtQrCode);
        tblQrScanner = (Button) findViewById(R.id.tblQrScanner);
        sKegiatan = (Spinner) findViewById(R.id.kegiatan);
        txt_keterangan = (EditText) findViewById(R.id.keterangan);
        txt_No = (TextView) findViewById(R.id.txtNo);
        txt_idPohon = (TextView) findViewById(R.id.txtIdPohon);

        if(getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            txt_No.setText(bundle.getString("no"));
            txt_idPohon.setText(bundle.getString("id_pohon"));
            sKegiatan.setSelection(((ArrayAdapter<String>)sKegiatan.getAdapter()).getPosition(bundle.getString("kegiatan")));
            txt_keterangan.setText(bundle.getString("keterangan"));
            txtQrCode.setText(bundle.getString("qrcode"));
            tblSimpan.setText("Update");
        }else{
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

//    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {
//      get strings of first item
//      String firstItem = String.valueOf(sKegiatan.getSelectedItem());
//        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            if (firstItem.equals(String.valueOf(sKegiatan.getSelectedItem()))) {
//            } else {
//                Toast.makeText(parent.getContext(),
//                        "Anda telah memilih : " + parent.getItemAtPosition(pos).toString(),
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg) {
//
//        }
//    }


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
        if(getIntent().getExtras()!=null){
            url = url_update;
        } else {
            url = url_insert;
        }
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());

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
                        Toast.makeText(InputHistoryActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage().toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String,String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if(getIntent().getExtras()!=null){
                    params.put("no", txt_No.getText().toString().trim());
                    params.put("id_pohon", txt_idPohon.getText().toString().trim());
                    params.put("kegiatan", sKegiatan.getSelectedItem().toString().trim());
                    params.put("keterangan", txt_keterangan.getText().toString().trim());
                } else {
                    params.put("kegiatan", sKegiatan.getSelectedItem().toString().trim());
                    params.put("keterangan", txt_keterangan.getText().toString().trim());
                    params.put("qrcode", txtQrCode.getText().toString().trim());
                }

                //kembali ke parameters
                Log.d(TAG, ""+params);
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
