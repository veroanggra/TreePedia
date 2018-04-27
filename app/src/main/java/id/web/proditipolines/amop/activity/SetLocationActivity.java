package id.web.proditipolines.amop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.util.LocationFinder;

import static id.web.proditipolines.amop.activity.DetailPohonActivity.idx;

public class SetLocationActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    SupportMapFragment mapFrag;
    Marker mCurrLocationMarker;
    double lat, longi;
    boolean gpsStatus = false;
    boolean networkStatus = false;
    TextView txtLatitude, txtLongitude;
    Button btnOK;
    String[] id, id_pegawai, last_update, jenis_pohon, usia_pohon, kondisi_pohon, foto_pohon, keterangan, status, qrcode;
    int i, numData;
    LatLng latLng[];
    Boolean markerD[];
    private Double[] latitude, longtitude;

    private static final String TAG = SetLocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        getSupportActionBar().setTitle("Lokasi");

        txtLatitude = (TextView) findViewById(R.id.latitude);
        txtLongitude = (TextView) findViewById(R.id.longitude);
        btnOK = (Button) findViewById(R.id.btnOk);

        gpsSwitch();
        LocationFinder gps = new LocationFinder(SetLocationActivity.this);
        if(gps.locationavailable()){
            lat = gps.getLatitude();
            longi = gps.getLongitude();
        } else {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
            lat = gps.getLatitude();
            longi = gps.getLongitude();
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputPohonActivity.mTextViewLat.setText(txtLatitude.getText());
                InputPohonActivity.mTextViewLon.setText(txtLongitude.getText());
                onBackPressed();
            }
        });


        txtLatitude.setText(String.valueOf(lat));
        txtLongitude.setText(String.valueOf(longi));

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lokasi);
        mapFrag.onCreate(savedInstanceState);
        mapFrag.onResume();
        mapFrag.getMapAsync(SetLocationActivity.this);

        getLokasi();

//        setupLocationRequest();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng latLng = new LatLng(lat, longi);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,19.5f));
        mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Lokasi Sekarang")
                .snippet("Tekan Lama Dan Pindahkan Marker Jika Dibutuhkan")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        mCurrLocationMarker.showInfoWindow();

        //move map camera
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("Marker", "Dragging");
            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                LatLng markerLocation = mCurrLocationMarker.getPosition();
                txtLatitude.setText(String.valueOf(markerLocation.latitude));
                txtLongitude.setText(String.valueOf(markerLocation.longitude));
//                Toast.makeText(SetLocationActivity.this,"latitude"+ markerLocation.latitude + "", Toast.LENGTH_LONG).show();
                Log.d("Marker", "finished");
            }

            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("Marker", "Started");

            }
        });
//        mMap.setMyLocationEnabled(true);
    }

    private void getLokasi() {
        String url = "http://monitoringpohon.semarangvice.com/AppAndroid/datapetapohon.php";
        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                numData = response.length();
                Log.d(TAG, response.toString());
                latLng = new LatLng[numData];
                markerD = new Boolean[numData];
                id = new String[numData];
                id_pegawai = new String[numData];
                last_update = new String[numData];
                jenis_pohon = new String[numData];
                usia_pohon = new String[numData];
                kondisi_pohon = new String[numData];
                foto_pohon = new String[numData];
                keterangan = new String[numData];
                status = new String[numData];
                qrcode = new String[numData];
                latitude = new Double[numData];
                longtitude = new Double[numData];

                for (i = 0; i < numData; i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        id[i] = data.getString("id");
                        latLng[i] = new LatLng(data.getDouble("latitude"),
                                data.getDouble("longtitude"));
                        id_pegawai[i] = data.getString("id_pegawai");
                        last_update[i] = data.getString("last_update");
                        jenis_pohon[i] = data.getString("jenis_pohon");
                        usia_pohon[i] = data.getString("usia_pohon");
                        kondisi_pohon[i] = data.getString("kondisi_pohon");
                        foto_pohon[i] = data.getString("foto_pohon");
                        keterangan[i] = data.getString("keterangan");
                        status[i] = data.getString("status");
                        qrcode[i] = data.getString("qrcode");
                        latitude[i] = data.getDouble("latitude");
                        longtitude[i] = data.getDouble("longtitude");

                        markerD[i] = false;
                        switch (kondisi_pohon[i]){
                            case "Sehat":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(qrcode[i])
                                        .snippet(jenis_pohon[i] + " - update : " +last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.sehat)));
                                break;
                            case "Cukup":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(qrcode[i])
                                        .snippet(jenis_pohon[i] + " - update : " +last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cukup)));
                                break;
                            case "Keropos":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(qrcode[i])
                                        .snippet(jenis_pohon[i] + " - update : " +last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.keropos)));
                                break;
                            case "Mati":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(qrcode[i])
                                        .snippet(jenis_pohon[i] + " - update : " +last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mati)));
                                break;
                            default:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(qrcode[i])
                                        .snippet(jenis_pohon[i] + " - update : " +last_update[i]));
                                break;

                        }
                    } catch (JSONException ignored) {
                        ignored.printStackTrace();
                    }
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Log.d(TAG, "Marker Clicked");
                        for (int i = 0; i < numData; i++) {

                            if (marker.getTitle().equals(foto_pohon[i])) {
                                if (markerD[i]) {
                                    Log.d(TAG, "panggil activity");
                                    idx = id[i];
                                    DetailPohonActivity.id_pegawai = id_pegawai[i];
                                    DetailPohonActivity.last_update = last_update[i];
                                    DetailPohonActivity.jenis_pohon = jenis_pohon[i];
                                    DetailPohonActivity.kondisi_pohon = kondisi_pohon[i];
                                    DetailPohonActivity.usia_pohon = usia_pohon[i];
                                    DetailPohonActivity.latitude = latitude[i].toString();
                                    DetailPohonActivity.longitude = longtitude[i].toString();
                                    DetailPohonActivity.ket = keterangan[i];
                                    DetailPohonActivity.status = status[i];
                                    DetailPohonActivity.code = qrcode[i];
                                    DetailPohonActivity.foto_pohon = foto_pohon[i];

                                    Intent intent = new Intent(SetLocationActivity.this, DetailPohonActivity.class);
                                    startActivity(intent);

                                    markerD[i] = false;
                                } else {
                                    Log.d(TAG, "show info");
                                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15.5f));
                                    markerD[i] = true;
                                    marker.showInfoWindow();
                                    Toast ts = Toast.makeText(SetLocationActivity.this,"Tap once again on marker for detail",Toast.LENGTH_LONG);
                                    TextView v = (TextView) ts.getView().findViewById(android.R.id.message);
                                    if( v != null)
                                        v.setGravity(Gravity.CENTER);
                                    ts.show();
                                }
                            } else {
                                markerD[i] = false;
                            }
                        }
                        return false;
                    }
                });
            }

        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetLocationActivity.this);
                builder.setTitle("Error!");
                builder.setMessage("No Internet Connection");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLokasi();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        Volley.newRequestQueue(SetLocationActivity.this).add(jArr);
    }

    private void gpsSwitch() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        if (!gpsStatus && !networkStatus) {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);

        }
    }
}
