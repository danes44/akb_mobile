package com.frumentiusdaneswara.akb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frumentiusdaneswara.akb.API.DetailOrderAPI;
import com.frumentiusdaneswara.akb.API.MenuAPI;
import com.frumentiusdaneswara.akb.API.OrderAPI;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.frumentiusdaneswara.akb.Models.OrderModel;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.android.volley.Request.Method.POST;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private List<OrderModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());

        String hasilScan = rawResult.getText();
        String[] hasilScanArray = hasilScan.split(";");
        System.out.println(hasilScanArray[0]);
        System.out.println(hasilScanArray[1]);
        System.out.println(hasilScanArray[2]);
        System.out.println(hasilScanArray[3]);

        createOrder(Integer.parseInt(hasilScanArray[0]));
        getOrder(Integer.parseInt(hasilScanArray[0]));
        savePreferences(hasilScanArray);

        Toast.makeText(this, "Selamat Datang, "+hasilScanArray[1],Toast.LENGTH_SHORT).show();

        mScannerView.resumeCameraPreview(this);

        Intent intent=new Intent(ScanActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void savePreferences(String[] hasilScanArray) {
        SharedPreferences sharedPref = getSharedPreferences("hasilScan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("id_reservasi", Integer.parseInt(hasilScanArray[0]));
        editor.putString("nama_customer", hasilScanArray[1]);
        editor.putString("no_meja", hasilScanArray[2]);
        editor.putString("tanggalWaktu", hasilScanArray[3]);
        editor.apply();
    }

    public void createOrder(final int id_reservasi){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menambahkan data order");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, OrderAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error

                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
//                    if(obj.getString("status").equals("Success"))
//                    {
//                        loadFragment(new SearchMenuFragment());
//                    }

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(mScannerView.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
//                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(mScannerView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
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
                params.put("id_reservasi", String.valueOf(id_reservasi));

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    private void getOrder(int id_reservasi) {

        RequestQueue queue = Volley.newRequestQueue(this);

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(view.getContext());
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menampilkan data menu");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
        System.out.println(id_reservasi);
        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, OrderAPI.URL_SELECT_ID + id_reservasi
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!list.isEmpty())
                        list.clear();

                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                    Integer id_order = jsonObject.optInt("id_order");
                    Integer id_reservasi = jsonObject.optInt("id_reservasi");
                    String tgl_order = jsonObject.optString("tgl_order");

                    OrderModel orderModel = new OrderModel(id_reservasi, id_order);


                    SharedPreferences sharedPref = getSharedPreferences("id_order", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("id_order", id_order);
                    editor.apply();

                    System.out.println(id_order + "asdadasdasdasda!@#!#!@#sdasda");

                    list.add(orderModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(mScannerView.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, error -> {
//            progressDialog.dismiss();
            Toast.makeText(mScannerView.getContext(), error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        });

        queue.add(stringRequest);
    }
}