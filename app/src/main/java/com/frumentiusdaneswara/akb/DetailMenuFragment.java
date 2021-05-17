package com.frumentiusdaneswara.akb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frumentiusdaneswara.akb.API.DetailOrderAPI;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class DetailMenuFragment extends Fragment {

    private TextView txtNamaMenu, txtHarga, txtDeskripsi, txtJumlahPesanan;
    private Chip txtKetersediaan;
    private ImageView ivGambar;
    private MenuModel menuModel;
    private String requestBarangJson;
    private boolean isScan;
    private int id_order;

    public static DetailMenuFragment newInstance() {
        DetailMenuFragment fragment = new DetailMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_menu, container, false);

        loadPreferences();

        txtNamaMenu         = (TextView) v.findViewById(R.id.nama_menu);
        txtHarga            = (TextView) v.findViewById(R.id.harga);
        txtDeskripsi        = (TextView) v.findViewById(R.id.deskripsi);
        txtKetersediaan     = (Chip) v.findViewById(R.id.ketersediaan);
        ivGambar            = (ImageView) v.findViewById(R.id.imageMenu);
        txtJumlahPesanan    = (TextView) v.findViewById(R.id.jumlah_pesanan);

        MaterialButton btnAddCart = (MaterialButton) v.findViewById(R.id.btnTambahPesanan);

        MaterialToolbar materialToolbar = (MaterialToolbar) v.findViewById(R.id.profileToolbar);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nanti disini balik ke home
//                dismiss();
                getActivity().onBackPressed();
            }
        });

//        Bundle bundle = this.getArguments();
//        System.out.println(bundle+"ASDAS!@#!@%!@#@!#!@#!@#");
//        if(bundle != null)
//            requestBarangJson = bundle.getString("item");
//        Gson gson = new Gson();
//        menuModel = gson.fromJson(requestBarangJson, MenuModel.class);

        Bundle bundle = this.getArguments();
        System.out.println(bundle);
        if(bundle != null) {
            menuModel = new Gson().fromJson((String) bundle.getSerializable("item"), MenuModel.class);


            NumberFormat formatter = new DecimalFormat("#,###");

            txtNamaMenu.setText(menuModel.getNama_menu());
            txtDeskripsi.setText(menuModel.getDeskripsi());
            if (menuModel.getKetersediaan() == 1) {
                txtKetersediaan.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#43A047")));
                txtKetersediaan.setTextColor(Color.parseColor("#43A047"));
                txtKetersediaan.setText("Tersedia");
            } else {
                txtKetersediaan.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#D24848")));
                txtKetersediaan.setTextColor(Color.parseColor("#D24848"));
                txtKetersediaan.setText("Tidak Tersedia");
            }
            txtHarga.setText("Rp. " + formatter.format((int) Math.round(menuModel.getHarga())));

            Glide.with(v.getContext())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(menuModel.getStr_gambar())
                    .centerCrop()
                    .override(300, 300)
                    .placeholder(R.drawable.dummy)
                    .skipMemoryCache(true)
                    .into(ivGambar);
        }

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScan){
                    if(txtJumlahPesanan.getText().length()<1 )
                    {
                        txtJumlahPesanan.setError("Data Tidak Boleh Kosong");
                    }
                    else {
                        double jumlah_harga = menuModel.getHarga() * Integer.parseInt(txtJumlahPesanan.getText().toString());
                        System.out.println(id_order +"-ASDDASDQWEQWDAS---"+ menuModel.getId_menu() +"--"+ Integer.parseInt(txtJumlahPesanan.getText().toString()) +"--"+ jumlah_harga);
                        tambahOrder(id_order, menuModel.getId_menu(), Integer.parseInt(txtJumlahPesanan.getText().toString()), jumlah_harga);
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        loadFragment(new CartFragment());
                                    }
                                }, 3000);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Scan Barcode Dahulu", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity() , ScanActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });
        return v;
    }

    private void loadPreferences() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("hasilScan", Context.MODE_PRIVATE);
        String nama_customer = preferences.getString("nama_customer","-");
        if(preferences!=null && !nama_customer.isEmpty() && !nama_customer.equals("-")) {
            isScan = true;
        }
        else
            isScan = false;

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("id_order", Context.MODE_PRIVATE);
        int id = sharedPref.getInt("id_order",0);
        if(sharedPref!=null && id!=0) {
            id_order = id;
            System.out.println(id_order);
        }
        else
            id_order = 0;

    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) this.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_detail_menu,fragment)
                .addToBackStack(null)
                .commit();
    }

    public void tambahOrder(final int id_order, final int id_menu, final int jumlah_order, final Double harga_jumlah){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data order");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, DetailOrderAPI.URL_ADD, new Response.Listener<String>() {
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
                System.out.println("======================MASUK SINI==================");
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_order", String.valueOf(id_order));
                params.put("id_menu", String.valueOf(id_menu));
                params.put("jumlah_order", String.valueOf(jumlah_order));
                params.put("harga_jumlah", String.valueOf(harga_jumlah));
                params.put("status_order", "dalam keranjang");

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}