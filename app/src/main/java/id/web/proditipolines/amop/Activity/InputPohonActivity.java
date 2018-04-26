package id.web.proditipolines.amop.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.web.proditipolines.amop.Adapter.AdapterPohon;
import id.web.proditipolines.amop.App.AppController;
import id.web.proditipolines.amop.Fragment.DataPohonFragment;
import id.web.proditipolines.amop.Fragment.GmapFragment;
import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.Util.Server;
import id.web.proditipolines.amop.Util.helper;


public class InputPohonActivity extends AppCompatActivity {
    helper help;
    private Button btCamera, tblQrScanner;
    public static TextView txtQrCode, mTextViewLat, mTextViewLon;
    static final int REQUEST_TAKE_PHOTO = 1;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    TextView mId, mIdpegawai;
    Button btnSimpan, btnUpdate, btnLokasi;
    ImageView imageView;
    EditText  txt_jenis, txt_usia, txt_keterangan;
    Spinner sKondisi;
    Bitmap bitmap;
    String idx, mCurrentPhotoPath, imageFileName, timeStamp;
    int success;

    private static final String TAG = InputPohonActivity.class.getSimpleName();

    private String url_insert = Server.URL + "insertpohon.php";
    private String url_edit   = Server.URL + "editpohon.php";
    private String url_update = Server.URL + "updatepohon.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pohon);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
        menu.setTitle("Form Data Pohon");

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnLokasi = (Button) findViewById(R.id.btnLokasi);
        txtQrCode = (TextView) findViewById(R.id.txtQrCode);
        tblQrScanner = (Button) findViewById(R.id.tblQrScanner);
        btCamera = (Button)findViewById(R.id.tblFoto);
        mTextViewLat = (TextView)findViewById(R.id.txtLatitude);
        mTextViewLon = (TextView)findViewById(R.id.txtLontitude);
        mId = (TextView) findViewById(R.id.txtId);
        mIdpegawai = (TextView) findViewById(R.id.txtIdPegwai);
        txt_jenis = (EditText) findViewById(R.id.jenispohon);
        txt_usia = (EditText) findViewById(R.id.usia);
        sKondisi = (Spinner) findViewById(R.id.kondisi);
        txt_keterangan = (EditText) findViewById(R.id.keterangan);
        imageView = (ImageView) findViewById(R.id.camera);

        help = new helper(getApplicationContext());
        HashMap<String, String> user = help.getUserDetail();
        String id_pegawai = user.get(helper.ID);
        mIdpegawai.setText(id_pegawai);

        if(getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            idx = bundle.getString("id");
            edit();
            btnSimpan.setVisibility(View.GONE);
            btnSimpan.setEnabled(false);
        }else{
//            setupLocationRequest();
            btnUpdate.setVisibility(View.GONE);
            btnUpdate.setEnabled(false);
        }

        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InputPohonActivity.this, SetLocationActivity.class);
                startActivity(intent);
            }
        });
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        tblQrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputPohonActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });
        
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Simpan();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                DetailPohonActivity detailPohonFragment = new DetailPohonActivity();
//                transaction.replace(R.id.frame, detailPohonFragment);
//                transaction.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void edit(){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        mId.setText(jObj.getString("id"));
                        txt_jenis.setText(jObj.getString("jenis_pohon"));
                        txt_usia.setText(jObj.getString("usia_pohon"));
                        sKondisi.setSelection(((ArrayAdapter<String>)sKondisi.getAdapter()).getPosition(jObj.getString("kondisi_pohon")));
                        mTextViewLat.setText(jObj.getString("latitude"));
                        mTextViewLon.setText(jObj.getString("longtitude"));
                        txt_keterangan.setText(jObj.getString("keterangan"));
                        txtQrCode.setText(jObj.getString("qrcode"));
                        String url = "http://monitoringpohon.semarangvice.com/dist/img/pohon/" + jObj.getString("foto_pohon");
                        Picasso.with(InputPohonActivity.this).load(url).error(R.mipmap.ic_launcher).into(imageView);

                    } else {
                        Toast.makeText(InputPohonActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(InputPohonActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void Simpan() {

        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_insert,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("v Add", jObj.toString());

                                Toast.makeText(InputPohonActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                kosong();
//                                setupLocationRequest();

                            } else {
                                Toast.makeText(InputPohonActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(InputPohonActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage().toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String,String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis

                params.put("filename", imageFileName);
                params.put("foto_pohon", getStringImage(bitmap));
                params.put("id_pegawai", mIdpegawai.getText().toString().trim());
                params.put("jenis_pohon", txt_jenis.getText().toString().trim());
                params.put("usia_pohon", txt_usia.getText().toString().trim());
                params.put("kondisi_pohon", sKondisi.getSelectedItem().toString().trim());
                params.put("latitude", mTextViewLat.getText().toString().trim());
                params.put("longtitude",mTextViewLon.getText().toString().trim());
                params.put("keterangan", txt_keterangan.getText().toString().trim());
                params.put("qrcode", txtQrCode.getText().toString().trim());


                //kembali ke parameters
                Log.d(TAG, ""+params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void Update() {

        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("v Add", jObj.toString());

                                Toast.makeText(InputPohonActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                                Toast.makeText(InputPohonActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(InputPohonActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage().toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String,String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if((getIntent().getExtras()!=null) && (bitmap != null)){
                    params.put("id", mId.getText().toString().trim());
                    params.put("filename", imageFileName);
                    params.put("foto_pohon", getStringImage(bitmap));
                    params.put("id_pegawai", mIdpegawai.getText().toString().trim());
                    params.put("jenis_pohon", txt_jenis.getText().toString().trim());
                    params.put("usia_pohon", txt_usia.getText().toString().trim());
                    params.put("kondisi_pohon", sKondisi.getSelectedItem().toString().trim());
                    params.put("latitude", mTextViewLat.getText().toString().trim());
                    params.put("longtitude",mTextViewLon.getText().toString().trim());
                    params.put("keterangan", txt_keterangan.getText().toString().trim());
                    params.put("qrcode", txtQrCode.getText().toString().trim());
                } else if((getIntent().getExtras()!=null) && (bitmap == null)){
                    params.put("id", mId.getText().toString().trim());
                    params.put("filename", "0");
                    params.put("foto_pohon", "0");
                    params.put("id_pegawai", mIdpegawai.getText().toString().trim());
                    params.put("jenis_pohon", txt_jenis.getText().toString().trim());
                    params.put("usia_pohon", txt_usia.getText().toString().trim());
                    params.put("kondisi_pohon", sKondisi.getSelectedItem().toString().trim());
                    params.put("latitude", mTextViewLat.getText().toString().trim());
                    params.put("longtitude",mTextViewLon.getText().toString().trim());
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
        mId.setText(null);
        imageView.setImageResource(0);
        txt_jenis.setText(null);
        txt_usia.setText(null);
//        txt_kondisi.setText(null);
        mTextViewLat.setText(null);
        mTextViewLon.setText(null);
        txt_keterangan.setText(null);
        txtQrCode.setText(null);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(

                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Get the dimensions of the View
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }

//    LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//                mTextViewLat.setText(""+location.getLatitude());
//                mTextViewLon.setText(""+location.getLongitude());
//            }
//        }
//    };
//
//    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
//        @Override
//        public void onConnected(Bundle bundle) {
//            Log.i("onConnected()", "start");
//            try {
//                LocationServices.FusedLocationApi.requestLocationUpdates(
//                        mGoogleApiClient, mLocationRequest, mLocationListener);
//            } catch (SecurityException e) {
//                Log.i("onConnected()","SecurityException: "+e.getMessage());
//            }
//        }
//        @Override
//        public void onConnectionSuspended(int i) {}
//    };
//
//    GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
//        @Override
//        public void onConnectionFailed(ConnectionResult connectionResult) {
//            Toast.makeText(InputPohonActivity.this, connectionResult.toString(), Toast.LENGTH_LONG).show();
//            Log.i("onConnected()", "SecurityException: " +connectionResult.toString());
//        }
//    };
//
//    protected synchronized void setupLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(10000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(mConnectionCallbacks)
//                .addOnConnectionFailedListener(mOnConnectionFailedListener)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

}
