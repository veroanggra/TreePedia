package id.web.proditipolines.amop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.web.proditipolines.amop.base.AppController;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static id.web.proditipolines.amop.util.AppConstans.TAG_SUCCESS;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    String id, qrcode;
    int success;
    String tag_json_obj = "json_obj_req";
    private ZXingScannerView mScannerView;

    private static final String TAG = ScannerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        qrcode = rawResult.getText();
        String url = "http://monitoringpohon.semarangvice.com/AppAndroid/cariid.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        id = jObj.getString("id");
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        Intent intent = new Intent(ScannerActivity.this, InputPohonActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                    } else {
                        InputPohonActivity.txtQrCode.setText(qrcode);
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(ScannerActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<>();
                params.put("qrcode", qrcode);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
