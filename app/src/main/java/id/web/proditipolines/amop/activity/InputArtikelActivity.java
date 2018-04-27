package id.web.proditipolines.amop.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import id.web.proditipolines.amop.util.Helper;

import static id.web.proditipolines.amop.util.AppConstans.TAG_ID_PEGAWAI;
import static id.web.proditipolines.amop.util.AppConstans.TAG_MESSAGE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_NAMA_ARTIKEL;
import static id.web.proditipolines.amop.util.AppConstans.TAG_SUCCESS;
import static id.web.proditipolines.amop.util.AppConstans.TAG_TEKS_ARTIKEL;
import static id.web.proditipolines.amop.util.Server.URL_INSERT_ARTIKEL;

public class InputArtikelActivity extends AppCompatActivity {
    private static final String TAG = InputArtikelActivity.class.getSimpleName();
    public static int RESULT_CODE = 200;
    private EditText edtDesc, edtTitle;
    private String titleArtikel, isiArtikel, id_pegawai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_artikel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar menu = getSupportActionBar();
        if (menu != null) {
            menu.setDisplayShowHomeEnabled(true);
            menu.setDisplayHomeAsUpEnabled(true);
            menu.setTitle("Form Artikel");
        }
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Helper help = new Helper(getApplicationContext());
        HashMap<String, String> user = help.getUserDetail();
        id_pegawai = user.get(Helper.ID);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isiArtikel = edtDesc.getText().toString();
                titleArtikel = edtTitle.getText().toString();
                if (isiArtikel.isEmpty() || isiArtikel.equals("")) {
                    edtDesc.setError("Masih Kosong");
                } else if (titleArtikel.isEmpty() || titleArtikel.equals("")) {
                    edtTitle.setError("Masih Kosong");
                } else {
                    Simpan(isiArtikel, titleArtikel);
                }
            }
        });
    }

    private void Simpan(final String isiArtikel, final String titleArtikel) {
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_ARTIKEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        loading.dismiss();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.getInt(TAG_SUCCESS) == 1) {
                                Log.d("v Add", jObj.toString());
                                Toast.makeText(InputArtikelActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                Intent resultIntent = new Intent();
                                setResult(RESULT_CODE, resultIntent);
                                finish();
                            } else {
                                Toast.makeText(InputArtikelActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(InputArtikelActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(TAG_TEKS_ARTIKEL, isiArtikel);
                params.put(TAG_NAMA_ARTIKEL, titleArtikel);
                params.put(TAG_ID_PEGAWAI, id_pegawai);
                Log.d(TAG, "" + params);
                return params;
            }
        };
        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
}
