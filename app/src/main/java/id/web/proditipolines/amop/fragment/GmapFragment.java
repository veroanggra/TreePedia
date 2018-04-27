package id.web.proditipolines.amop.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.web.proditipolines.amop.activity.DetailPohonActivity;
import id.web.proditipolines.amop.R;

import static id.web.proditipolines.amop.activity.DetailPohonActivity.idx;

/**
 * A simple {@link Fragment} subclass.
 */
public class GmapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mapView;
    String[] id, id_pegawai, last_update, jenis_pohon, usia_pohon, kondisi_pohon, foto_pohon, keterangan, status, qrcode;
    int i, numData;
    LatLng latLng[];
    Boolean markerD[];
    private Double[] latitude, longtitude;

    private static final String TAG = GmapFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        getLokasi();
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
                        latLng[i] = new LatLng(data.getDouble("latitude"), data.getDouble("longtitude"));
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
                        switch (kondisi_pohon[i]) {
                            case "Sehat":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(foto_pohon[i])
                                        .snippet(jenis_pohon[i] + "\n\n" + last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.sehat)));
                                break;
                            case "Cukup":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(foto_pohon[i])
                                        .snippet(jenis_pohon[i] + "\n\n" + last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cukup)));
                                break;
                            case "Keropos":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(foto_pohon[i])
                                        .snippet(jenis_pohon[i] + "\n\n" + last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.keropos)));
                                break;
                            case "Mati":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(foto_pohon[i])
                                        .snippet(jenis_pohon[i] + "\n\n" + last_update[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mati)));
                                break;
                            default:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng[i])
                                        .title(foto_pohon[i])
                                        .snippet(jenis_pohon[i] + "\n\n" + last_update[i]));
                                break;

                        }
                    } catch (JSONException ignored) {
                        Log.e(TAG, ignored.getMessage());
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng[i], 15.5f));
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

                                    Intent intent = new Intent(getActivity(), DetailPohonActivity.class);
                                    startActivity(intent);

                                    markerD[i] = false;
                                } else {
                                    Log.d(TAG, "show info");
                                    markerD[i] = true;
                                    marker.showInfoWindow();
                                    Toast ts = Toast.makeText(getActivity(), "Ketuk sekali lagi pada marker untuk melihat detail", Toast.LENGTH_LONG);
                                    TextView v = (TextView) ts.getView().findViewById(android.R.id.message);
                                    if (v != null)
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

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        Volley.newRequestQueue(getActivity()).add(jArr);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @SuppressLint("InflateParams")
            @Override
            public View getInfoContents(Marker marker) {
                View v = null;
                try {

                    // Getting view from the layout file info_window_layout
                    v = getActivity().getLayoutInflater().inflate(R.layout.infowindow, null);

                    // Getting reference to the TextView to set latitude
                    TextView infoTxt = (TextView) v.findViewById(R.id.infoTxt);
                    ImageView foto_pohon = (ImageView) v.findViewById(R.id.imgPohon);

                    infoTxt.setText(marker.getSnippet());
                    String url = "http://monitoringpohon.semarangvice.com/dist/img/pohon/" + marker.getTitle();
                    Picasso.with(getActivity()).load(url).error(R.mipmap.ic_launcher).into(foto_pohon);

                } catch (Exception ev) {
                    System.out.print(ev.getMessage());
                }
                return v;
            }
        });
    }
}
