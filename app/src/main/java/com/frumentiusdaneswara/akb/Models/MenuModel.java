package com.frumentiusdaneswara.akb.Models;

public class MenuModel {
    private String  nama_menu, deskripsi, unit, tipe_menu, str_gambar;
    private int id_menu;
    private double harga;
    private boolean ketersediaan;

    public MenuModel(int id_menu, String nama_menu, String deskripsi, String unit, String tipe_menu, String str_gambar, double harga, boolean ketersediaan) {
        this.nama_menu = nama_menu;
        this.deskripsi = deskripsi;
        this.unit = unit;
        this.tipe_menu = tipe_menu;
        this.str_gambar = str_gambar;
        this.id_menu = id_menu;
        this.harga = harga;
        this.ketersediaan = ketersediaan;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getUnit() {
        return unit;
    }

    public String getTipe_menu() {
        return tipe_menu;
    }

    public String getStr_gambar() {
        return str_gambar;
    }

    public int getId_menu() {
        return id_menu;
    }

    public double getHarga() {
        return harga;
    }

    public boolean isKetersediaan() {
        return ketersediaan;
    }
}
