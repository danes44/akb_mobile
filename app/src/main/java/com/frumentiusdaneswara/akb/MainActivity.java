package com.frumentiusdaneswara.akb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.frumentiusdaneswara.akb.Adapters.AdapterMenu;
import com.frumentiusdaneswara.akb.Models.MenuModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialButton buttonScan, buttonSearch, buttonCart;
    ImageView menuUtama, sideDish, minuman;
    TextView txtHello;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AdapterMenu adapterMenu;
    private List<MenuModel> list = new ArrayList<>();
    SearchMenuFragment searchMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = MainActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        buttonScan = (MaterialButton) findViewById(R.id.btnScan);
        buttonCart = (MaterialButton) findViewById(R.id.btnCart);
        buttonSearch = (MaterialButton) findViewById(R.id.btnSearch);
        menuUtama = (ImageView) findViewById(R.id.imageMenuUtama);
        sideDish = (ImageView) findViewById(R.id.imageSideDish);
        minuman = (ImageView) findViewById(R.id.imageMinuman);
        txtHello = (TextView) findViewById(R.id.hello);
        adapterMenu = new AdapterMenu(this, list);

        loadPreferences();

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this , ScanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchMenuFragment());
            }
        });

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CartFragment());
            }
        });

        menuUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchMenuFragment());
//                savePreferences("utama");
            }
        });

        sideDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchMenuFragment());
//                savePreferences("utama");
            }
        });

        minuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchMenuFragment());
//                savePreferences("utama");
            }
        });

    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private void savePreferences(String kategori) {
        SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("searchKategori", kategori);
        editor.apply();
    }

    private void loadPreferences() {
        SharedPreferences preferences = this.getSharedPreferences("hasilScan", Context.MODE_PRIVATE);
        String nama_customer = preferences.getString("nama_customer", "-");
        if(preferences!=null && !nama_customer.isEmpty() && !nama_customer.equals("-")) {
            txtHello.setText("Selamat datang, " + nama_customer);
        }

    }
}