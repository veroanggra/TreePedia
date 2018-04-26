package id.web.proditipolines.amop.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.web.proditipolines.amop.R;

import static id.web.proditipolines.amop.Activity.MainActivity.navItemIndex;

public class HomeFragment extends Fragment{

    String[] kondisi_pohon;
    int[] jumlah;
    int i, numData;
    BarChart barChart;

    private static final String TAG = HomeFragment.class.getSimpleName();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        barChart = (BarChart) v.findViewById(R.id.chart);

        getData();

        return v;
    }

    private void getData() {
        String url = "http://monitoringpohon.semarangvice.com/AppAndroid/datajumlahpohon.php";
        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                numData = response.length();
                Log.d(TAG, response.toString());
                kondisi_pohon = new String[numData];
                jumlah = new int[numData];

                ArrayList<BarEntry> entries = new ArrayList<>();
                for (i = 0; i < numData; i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        jumlah[i] = data.getInt("jumlah");

                        entries.add(new BarEntry(jumlah[i], i));
                    } catch (JSONException je) {
                    }
                }

                BarDataSet dataset = new BarDataSet(entries, "");

                ArrayList<String> labels = new ArrayList<String>();
                for (i = 0; i < numData; i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        kondisi_pohon[i] = data.getString("kondisi_pohon");

                        labels.add(kondisi_pohon[i]);
                    } catch (JSONException je) {
                    }
                }

                BarData data = new BarData(labels, dataset);
                int color1 = getResources().getColor(R.color.kondisiSehat);
                int color2 = getResources().getColor(R.color.kondisiCukup);
                int color3 = getResources().getColor(R.color.kondisiKeropos);
                int color4 = getResources().getColor(R.color.kondisiMati);
                dataset.setColors(new int[]{color1, color2, color3, color4});
                barChart.setDescription("Kondisi Pohon");
                barChart.setData(data);

                barChart.animateY(5000);

            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error!");
                builder.setMessage("No Internet Connection");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getData();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(jArr);
    }

}
