package com.frumentiusdaneswara.akb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.frumentiusdaneswara.akb.API.MenuAPI;
import com.frumentiusdaneswara.akb.Adapters.AdapterMenu;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchMenuFragment extends Fragment {

    private AdapterMenu adapter;
    private RecyclerView recyclerView;
    private View view;
    private List<MenuModel> list = new ArrayList<>();
    private TextInputEditText searchInputText;
    private SwipeRefreshLayout swipeRefresh;

    public SearchMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_menu, container, false);

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.menu_rv);
        adapter = new AdapterMenu(view.getContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        searchInputText = view.findViewById(R.id.search_menu);

        loadPreferences();

        MaterialToolbar materialToolbar = (MaterialToolbar) view.findViewById(R.id.profileToolbar);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nanti disini balik ke home
                getActivity().onBackPressed();
            }
        });

        searchInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter.getFilter().filter(s);
                System.out.println("MASUK SINI LHOOOOO ASDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);
        getMenu();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMenu();
            }
        });

        return view;
    }

    private void getMenu() {

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(view.getContext());
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menampilkan data menu");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, MenuAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!list.isEmpty())
                        list.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id_menu = jsonObject.optInt("id_menu");
                        String tipe_menu = jsonObject.optString("tipe_menu");
                        String nama_menu = jsonObject.optString("nama_menu");
                        Double harga = jsonObject.optDouble("harga");
                        String str_gambar = MenuAPI.ROOT_URL+jsonObject.optString("str_gambar");
                        Integer ketersediaan = jsonObject.optInt("ketersediaan");
                        String deskripsi = jsonObject.optString("deskripsi");
                        String unit = jsonObject.optString("unit");

                        MenuModel menuModel = new MenuModel(id_menu, nama_menu, deskripsi, unit, tipe_menu, str_gambar, harga, ketersediaan);

                        list.add(menuModel);
                    }
                    swipeRefresh.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, error -> {
//            progressDialog.dismiss();
            swipeRefresh.setRefreshing(false);
            Toast.makeText(view.getContext(), error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        });

        queue.add(stringRequest);
    }

    private void loadPreferences() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("myKey", Context.MODE_PRIVATE);
        String kategori = preferences.getString("searchKategori","-");
        if (kategori.equals("utama")){
            searchInputText.setText("utama");
        }
        else if (kategori.equals("sideDish")){
            searchInputText.setText("side dish");
        }
        else if (kategori.equals("minuman")){
            searchInputText.setText("minuman");
        }

    }
}