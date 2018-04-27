package id.web.proditipolines.amop.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import id.web.proditipolines.amop.activity.InputHistoryActivity;
import id.web.proditipolines.amop.adapter.AdapterHistory;
import id.web.proditipolines.amop.base.AppController;
import id.web.proditipolines.amop.model.DataHistory;
import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.util.Server;

import static id.web.proditipolines.amop.util.AppConstans.TAG_ID_POHON;
import static id.web.proditipolines.amop.util.AppConstans.TAG_KEGIATAN;
import static id.web.proditipolines.amop.util.AppConstans.TAG_KETERANGAN;
import static id.web.proditipolines.amop.util.AppConstans.TAG_MESSAGE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_NO;
import static id.web.proditipolines.amop.util.AppConstans.TAG_QRCODE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_SUCCESS;
import static id.web.proditipolines.amop.util.AppConstans.TAG_TANGGAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPohonFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    SwipeRefreshLayout swipe;
    List<DataHistory> itemList = new ArrayList<>();
    AdapterHistory adapterHistory;
    int success;
    AlertDialog dialog, alertDialog;
    FloatingActionButton fab2;
    private static final String TAG = HistoryPohonFragment.class.getSimpleName();

    String tag_json_obj = "json_obj_req";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_pohon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // menghubungkan variablel pada layout dan pada java
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        list = (ListView) view.findViewById(R.id.list);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);

        // untuk mengisi data dari JSON ke dalam adapter
        adapterHistory = new AdapterHistory(getActivity(), itemList);
        list.setAdapter(adapterHistory);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapterHistory.notifyDataSetChanged();
                callVolley();
            }
        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long no) {
                // TODO Auto-generated method stub
                final String nox = itemList.get(position).getNo();

//                final CharSequence[] dialogitem = {"Delete"};
                dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle("Peringatan");
                dialog.setMessage("Lakukan Aksi");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final DataHistory hasil = itemList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("no", hasil.getNo());
                        bundle.putString("id_pohon", hasil.getId_pohon());
                        bundle.putString("tanggal", hasil.getTanggal());
                        bundle.putString("kegiatan", hasil.getKegiatan());
                        bundle.putString("keterangan", hasil.getKeterangan());
                        bundle.putString("qrcode", hasil.getQrcode());
                        Intent intent = new Intent(getActivity(), InputHistoryActivity.class);
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
                                delete(nox);
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

                dialog.show();
                return false;
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InputHistoryActivity.class));
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterHistory.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapterHistory.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        String url_select = Server.URL + "lihatdatahistory.php";
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataHistory item = new DataHistory();

                        item.setNo(obj.getString(TAG_NO));
                        item.setId_pohon(obj.getString(TAG_ID_POHON));
                        item.setTanggal(obj.getString(TAG_TANGGAL));
                        item.setKegiatan(obj.getString(TAG_KEGIATAN));
                        item.setKeterangan(obj.getString(TAG_KETERANGAN));
                        item.setQrcode(obj.getString(TAG_QRCODE));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapterHistory.notifyDataSetChanged();

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
    private void delete(final String nox) {
        String url_delete = Server.URL + "deletehistory.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapterHistory.notifyDataSetChanged();

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
                Map<String, String> params = new HashMap<>();
                params.put("no", nox);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

}
