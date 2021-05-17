package com.frumentiusdaneswara.akb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frumentiusdaneswara.akb.API.DetailOrderAPI;
import com.frumentiusdaneswara.akb.API.MenuAPI;
import com.frumentiusdaneswara.akb.Adapters.AdapterDetailOrder;
import com.frumentiusdaneswara.akb.Adapters.AdapterMenu;
import com.frumentiusdaneswara.akb.Models.DetailOrderModel;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;


public class CartFragment extends Fragment {

    private AdapterDetailOrder adapter;
    private RecyclerView recyclerView;
    private View view;
    private List<DetailOrderModel> list = new ArrayList<>();
    private TextInputEditText searchInputText;
    private SwipeRefreshLayout swipeRefresh;
    private int id_reservasi, id_pesanan =0;
    private double subtotal = 0;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.cart_rv);
        adapter = new AdapterDetailOrder(view.getContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        System.out.println("=============================HABIS INI====================");
        loadPreferences();

        MaterialToolbar materialToolbar = (MaterialToolbar) view.findViewById(R.id.profileToolbar);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nanti disini balik ke home
                getActivity().onBackPressed();
            }
        });


        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(false);
        getDetailOrder();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                subtotal = 0; //subtotal di inisialisasi lagi biar gak nambah terus kalo direfresh
                getDetailOrder();
            }
        });

        MaterialButton btnCheckoutPesanan = (MaterialButton) view.findViewById(R.id.btnCheckoutPesanan);
        btnCheckoutPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_pesanan!=0) {
                    submit(id_pesanan);
                    swipeRefresh.setRefreshing(true);
                }
                else {
                    Toast.makeText(view.getContext(), "Belum ada pesanan, silakan pilih pesanan Anda!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void getDetailOrder() {

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(view.getContext());
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menampilkan data menu");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, DetailOrderAPI.URL_SELECT + id_reservasi
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!list.isEmpty())
                        list.clear();
                    if(jsonArray.length()>0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                            Integer id_menu = jsonObject.optInt("id_menu");
                            Integer id_detail = jsonObject.optInt("id_detail");
                            Integer id_order = jsonObject.optInt("id_order");
                            Integer jumlah_order = jsonObject.optInt("jumlah_order");
                            String tipe_menu = jsonObject.optString("tipe_menu");
                            String nama_menu = jsonObject.optString("nama_menu");
                            Double harga_jumlah = jsonObject.optDouble("harga_jumlah");
                            Double harga = jsonObject.optDouble("harga");
                            String str_gambar = MenuAPI.ROOT_URL+jsonObject.optString("str_gambar");
                            String deskripsi = jsonObject.optString("deskripsi");
                            String unit = jsonObject.optString("unit");
                            String status_order = jsonObject.optString("status_order");
                            String waktu_order = jsonObject.optString("waktu_order");

                            DetailOrderModel detailOrderModel = new DetailOrderModel(status_order, waktu_order, nama_menu, deskripsi, unit, tipe_menu, str_gambar, id_detail, id_menu, id_order, jumlah_order, harga_jumlah, harga);

                            subtotal += harga_jumlah;
                            id_pesanan = id_order;
                            list.add(detailOrderModel);
                        }
                        swipeRefresh.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                        TextView txtSubtotal = (TextView) view.findViewById(R.id.total_harga);
                        NumberFormat formatter = new DecimalFormat("#,###");
                        txtSubtotal.setText("Rp. " + formatter.format((int) Math.round(subtotal)));

                    }
                    else{
                        Toast.makeText(view.getContext(), "Belum ada pesanan, silakan pilih pesanan",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, error -> {
//            progressDialog.dismiss();
            swipeRefresh.setRefreshing(false);
            if (id_pesanan==0) {
                Toast.makeText(view.getContext(), "Belum ada pesanan, silakan pilih pesanan Anda!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    public void submit(final int id_order){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Melakukan checkout order");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, DetailOrderAPI.URL_UPDATE_STATUS + id_order, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if(obj.getString("status").equals("Success"))
                    {
                        loadFragment(new SearchMenuFragment());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                System.out.println("INI RESPONSE: "+response);
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        System.out.println("INI OBJ: "+obj);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    private void loadPreferences() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("hasilScan", Context.MODE_PRIVATE);
        String nama_customer = preferences.getString("nama_customer","-");
        if(preferences!=null && !nama_customer.isEmpty() && !nama_customer.equals("-")) {
            id_reservasi = preferences.getInt("id_reservasi", 0);
        }
        else
            id_reservasi = 0;
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) this.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_detail_menu,fragment)
                .addToBackStack(null)
                .commit();
    }
}
