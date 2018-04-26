package id.web.proditipolines.amop.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.web.proditipolines.amop.Activity.DetailPohonActivity;
import id.web.proditipolines.amop.Activity.InputPohonActivity;
import id.web.proditipolines.amop.Activity.MainActivity;
import id.web.proditipolines.amop.Adapter.AdapterPohon;
import id.web.proditipolines.amop.App.AppController;
import id.web.proditipolines.amop.Data.DataPohon;
import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.Util.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataPohonFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView list;
    SwipeRefreshLayout swipe;
    List<DataPohon> itemList = new ArrayList<DataPohon>();
    AdapterPohon adapterPohon;
    AlertDialog dialog, alertDialog;
    FloatingActionButton fab;
    int success;

    private static final String TAG = DataPohonFragment.class.getSimpleName();

    private static String url_select     = Server.URL + "lihatdatapohon.php";
    private static String url_delete     = Server.URL + "deletepohon.php";

    public static final String TAG_ID           = "id";
    public static final String TAG_ID_PEGAWAI   = "id_pegawai";
    public static final String TAG_LAST_UPDATE  = "last_update";
    public static final String TAG_JENIS_POHON  = "jenis_pohon";
    public static final String TAG_USIA_POHON   = "usia_pohon";
    public static final String TAG_KONDISI_POHON= "kondisi_pohon";
    public static final String TAG_LATITUDE     = "latitude";
    public static final String TAG_LONGTITUDE   = "longtitude";
    public static final String TAG_FOTO_POHON   = "foto_pohon";
    public static final String TAG_KETERANGAN   = "keterangan";
    public static final String TAG_STATUS       = "status";
    public static final String TAG_QRCODE       = "qrcode";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    public DataPohonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data_pohon, container, false);

        // menghubungkan variablel pada layout dan pada java
        swipe   = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) v.findViewById(R.id.list);
        fab     = (FloatingActionButton) v.findViewById(R.id.fab);

        // untuk mengisi data dari JSON ke dalam adapter
        adapterPohon = new AdapterPohon(getActivity(), itemList);
        list.setAdapter(adapterPohon);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable()
                   {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapterPohon.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemList.get(position).getId();

                dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle("Peringatan");
                dialog.setMessage("Lakukan Aksi");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final DataPohon hasil = itemList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", hasil.getId());
                        Intent intent = new Intent(getActivity(), InputPohonActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Peringatan");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Anda yakin ingin menghapusnya?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                delete(idx);
                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "TIDAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });

                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "LIHAT DATA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();final
                        DataPohon hasil = itemList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", hasil.getId());
                        bundle.putString("id_pegawai", hasil.getId_pegawai());
                        bundle.putString("last_update", hasil.getLast_update());
                        bundle.putString("jenis_pohon", hasil.getJenis_pohon());
                        bundle.putString("usia_pohon", hasil.getUsia_pohon());
                        bundle.putString("kondisi_pohon", hasil.getKondisi_pohon());
                        bundle.putString("latitude", hasil.getLatitude());
                        bundle.putString("longtitude", hasil.getLongtitude());
                        bundle.putString("foto_pohon", hasil.getFoto_pohon());
                        bundle.putString("keterangan", hasil.getKeterangan());
                        bundle.putString("status", hasil.getStatus());
                        bundle.putString("qrcode", hasil.getQrcode());

                        Intent intent = new Intent(getActivity(), DetailPohonActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                });
                dialog.show();
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                startActivity(new
                        Intent(getActivity(), InputPohonActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterPohon.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapterPohon.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataPohon item = new DataPohon();

                        item.setId(obj.getString(TAG_ID));
                        item.setId_pegawai(obj.getString(TAG_ID_PEGAWAI));
                        item.setLast_update(obj.getString(TAG_LAST_UPDATE));
                        item.setJenis_pohon(obj.getString(TAG_JENIS_POHON));
                        item.setUsia_pohon(obj.getString(TAG_USIA_POHON));
                        item.setKondisi_pohon(obj.getString(TAG_KONDISI_POHON));
                        item.setLatitude(obj.getString(TAG_LATITUDE));
                        item.setLongtitude(obj.getString(TAG_LONGTITUDE));
                        item.setFoto_pohon(obj.getString(TAG_FOTO_POHON));
                        item.setKeterangan(obj.getString(TAG_KETERANGAN));
                        item.setStatus(obj.getString(TAG_STATUS));
                        item.setQrcode(obj.getString(TAG_QRCODE));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapterPohon.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menghapus
    private void delete(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapterPohon.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
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

}
