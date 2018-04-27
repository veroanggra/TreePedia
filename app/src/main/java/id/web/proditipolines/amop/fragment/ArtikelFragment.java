package id.web.proditipolines.amop.fragment;


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
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.activity.InputArtikelActivity;
import id.web.proditipolines.amop.adapter.AdapterArtikel;
import id.web.proditipolines.amop.base.AppController;
import id.web.proditipolines.amop.model.DataArtikel;

import static id.web.proditipolines.amop.activity.InputArtikelActivity.RESULT_CODE;
import static id.web.proditipolines.amop.util.AppConstans.TAG_ID_ARTIKEL;
import static id.web.proditipolines.amop.util.AppConstans.TAG_ID_PEGAWAI;
import static id.web.proditipolines.amop.util.AppConstans.TAG_NAMA_ARTIKEL;
import static id.web.proditipolines.amop.util.AppConstans.TAG_NAMA_PEGAWAI;
import static id.web.proditipolines.amop.util.AppConstans.TAG_TEKS_ARTIKEL;
import static id.web.proditipolines.amop.util.AppConstans.TAG_WAKTU_ARTIKEL;
import static id.web.proditipolines.amop.util.Server.URL_ARTIKEL;


public class ArtikelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ArtikelFragment.class.getSimpleName();
    private SwipeRefreshLayout swipe;
    private List<DataArtikel> itemList = new ArrayList<>();
    private AdapterArtikel adapterArtikel;
    private int ACTION = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artikel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        ListView list = (ListView) view.findViewById(R.id.list);
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addArtikel);
        adapterArtikel = new AdapterArtikel(getActivity(), itemList);
        list.setAdapter(adapterArtikel);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapterArtikel.notifyDataSetChanged();
                callVolley();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), InputArtikelActivity.class), ACTION);
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterArtikel.notifyDataSetChanged();
        callVolley();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION && resultCode == RESULT_CODE){
            itemList.clear();
            adapterArtikel.notifyDataSetChanged();
            callVolley();
        }
    }

    private void callVolley() {
        itemList.clear();
        adapterArtikel.notifyDataSetChanged();
        swipe.setRefreshing(true);
        JsonArrayRequest jArr = new JsonArrayRequest(URL_ARTIKEL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        DataArtikel item = new DataArtikel();
                        item.setId_artikel(obj.getString(TAG_ID_ARTIKEL));
                        item.setNama_artikel(obj.getString(TAG_NAMA_ARTIKEL));
                        item.setNama_pegawai(obj.getString(TAG_NAMA_PEGAWAI));
                        item.setTeks_artikel(obj.getString(TAG_TEKS_ARTIKEL));
                        item.setWaktu_artikel(obj.getString(TAG_WAKTU_ARTIKEL));
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterArtikel.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(jArr);
    }
}
