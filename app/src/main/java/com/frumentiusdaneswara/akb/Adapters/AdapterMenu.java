package com.frumentiusdaneswara.akb.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.frumentiusdaneswara.akb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.adapterUserViewHolder> {

    private List<MenuModel> menuList;
    private List<MenuModel> menuListFiltered;
    private Context context;
    private View view;

    public AdapterMenu(Context context, List<MenuModel> menuList) {
        this.context                = context;
        this.menuList               = menuList;
        this.menuListFiltered       = menuList;
    }

    @NonNull
    @Override
    public AdapterMenu.adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_menu, parent, false);
        return new AdapterMenu.adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMenu.adapterUserViewHolder holder, int position) {
        final MenuModel menuModel = menuListFiltered.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");

        holder.txtNamaMenu.setText(menuModel.getNama_menu());
        holder.txtHarga.setText("Rp." + formatter.format((int) Math.round(menuModel.getHarga())));
        holder.txtDeskripsi.setText(menuModel.getDeskripsi());
        Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(menuModel.getStr_gambar())
                .centerCrop()
                .override(300, 300)
                .placeholder(R.drawable.dummy)
                .skipMemoryCache(true)
                .into(holder.ivGambar);
    }

    @Override
    public int getItemCount() {
        return (menuListFiltered != null) ? menuListFiltered.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaMenu, txtHarga, txtDeskripsi;
        private ImageView ivGambar;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaMenu         = (TextView) itemView.findViewById(R.id.nama_menu);
            txtHarga            = (TextView) itemView.findViewById(R.id.harga);
            txtDeskripsi        = (TextView) itemView.findViewById(R.id.deskripsi);
            ivGambar            = (ImageView) itemView.findViewById(R.id.gambar);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    menuListFiltered = menuList;
                }
                else {
                    List<MenuModel> filteredList = new ArrayList<>();
                    for(MenuModel menuModel : menuList) {
                        if(menuModel.getNama_menu().toLowerCase().contains(userInput) ||
                                menuModel.getTipe_menu().toLowerCase().contains(userInput)) {
                            filteredList.add(menuModel);
                        }
                    }
                    menuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = menuListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menuListFiltered = (ArrayList<MenuModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //Fungsi menghapus data mahasiswa

//    public void deleteMahasiswa(String npm){
//        //Pendeklarasian queue
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menghapus data mahasiswa");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
//
//        //Memulai membuat permintaan request menghapus data ke jaringan
//        StringRequest stringRequest = new StringRequest(DELETE, MahasiswaAPI.URL_DELETE + npm, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
//                progressDialog.dismiss();
//                try {
//                    //Mengubah response string menjadi object
//                    JSONObject obj = new JSONObject(response);
//                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
//                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
//                    notifyDataSetChanged();
//                    mListener.deleteItem(true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Disini bagian jika response jaringan terdapat ganguan/error
//                progressDialog.dismiss();
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
//        queue.add(stringRequest);
//    }

//    public void loadFragment(Fragment fragment) {
//        AppCompatActivity activity = (AppCompatActivity) view.getContext();
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_view_mahasiswa,fragment)
//                .addToBackStack(null)
//                .commit();
//    }
}