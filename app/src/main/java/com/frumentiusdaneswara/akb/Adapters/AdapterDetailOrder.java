package com.frumentiusdaneswara.akb.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frumentiusdaneswara.akb.DetailMenuFragment;
import com.frumentiusdaneswara.akb.Models.DetailOrderModel;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.frumentiusdaneswara.akb.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterDetailOrder extends RecyclerView.Adapter<AdapterDetailOrder.adapterUserViewHolder>{

    private List<DetailOrderModel> detailOrderList;
    private List<DetailOrderModel> detailOrderListFiltered;
    private Context context;
    private View view;
    private String userInput;

    public AdapterDetailOrder(Context context, List<DetailOrderModel> detailOrderList) {
        this.context                    = context;
        this.detailOrderList            = detailOrderList;
        this.detailOrderListFiltered    = detailOrderList;
    }

    @NonNull
    @Override
    public AdapterDetailOrder.adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_detail_order, parent, false);
        return new AdapterDetailOrder.adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailOrder.adapterUserViewHolder holder, int position) {
        final DetailOrderModel detailOrderModel = detailOrderListFiltered.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");

        holder.txtNamaMenu.setText(detailOrderModel.getNama_menu());
        System.out.println(detailOrderModel.getJumlah_order());
//        holder.txtHarga.setText(detailOrderModel.getJumlah_order()+" @ "+"Rp. "
//                        +formatter.format((int) Math.round(detailOrderModel.getHarga())));
        holder.txtHarga.setText(formatter.format((int) Math.round(detailOrderModel.getHarga_jumlah())));
        holder.txtDeskripsi.setText(detailOrderModel.getDeskripsi());
        Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(detailOrderModel.getStr_gambar())
                .centerCrop()
                .override(300, 300)
                .placeholder(R.drawable.dummy)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
//                DetailMenuFragment dialog = new DetailMenuFragment();
//                dialog.show(manager, "dialog");
//
//                Gson gson = new Gson();
//                String requestItemJson = gson.toJson(detailOrderModel);
//
//                Bundle args = new Bundle();
//                args.putString("item", requestItemJson);
//                dialog.setArguments(args);
                DetailMenuFragment fragment = new DetailMenuFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", new Gson().toJson(detailOrderModel));
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (detailOrderListFiltered != null) ? detailOrderListFiltered.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtNamaMenu, txtHarga, txtDeskripsi;
        private final ImageView ivGambar;
        private final LinearLayout mParent;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaMenu         = (TextView) itemView.findViewById(R.id.nama_menu);
            txtHarga            = (TextView) itemView.findViewById(R.id.harga);
            txtDeskripsi        = (TextView) itemView.findViewById(R.id.deskripsi);
            ivGambar            = (ImageView) itemView.findViewById(R.id.gambar);
            mParent             = (LinearLayout) itemView.findViewById(R.id.parentAdapter);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                userInput = charSequence.toString().toLowerCase();
//                loadPreferences();
                if (userInput.isEmpty()) {
                    detailOrderListFiltered = detailOrderList;
                }
                else {
                    List<DetailOrderModel> filteredList = new ArrayList<>();
                    for(DetailOrderModel detailOrderModel : detailOrderList) {
                        if(detailOrderModel.getNama_menu().toLowerCase().contains(userInput) || detailOrderModel.getTipe_menu().toLowerCase().contains(userInput)) {
                            filteredList.add(detailOrderModel);
                        }
                    }
                    detailOrderListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = detailOrderListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                detailOrderListFiltered = (ArrayList<DetailOrderModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void loadPreferences() {
        SharedPreferences preferences = this.context.getSharedPreferences("myKey", Context.MODE_PRIVATE);
        String kategori = preferences.getString("searchKategori","-");
        if (kategori.equals("utama")){
            userInput = "utama";
        }
        else if (kategori.equals("sideDish")){
            userInput = "side dish";
        }
        else if (kategori.equals("minuman")){
            userInput = "minuman";
        }

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

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_search_menu,fragment)
                .addToBackStack(null)
                .commit();
    }
}