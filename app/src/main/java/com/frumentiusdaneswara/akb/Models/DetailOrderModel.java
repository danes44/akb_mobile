package com.frumentiusdaneswara.akb.Models;

public class DetailOrderModel {
    private String  status_order, waktu_order, nama_menu, deskripsi, unit, tipe_menu, str_gambar;
    private int id_detail, id_menu, id_order, jumlah_order;
    private double harga_jumlah, harga;



    public DetailOrderModel(String status_order, String waktu_order, String nama_menu, String deskripsi, String unit, String tipe_menu, String str_gambar, int id_detail, int id_menu, int id_order, int jumlah_order, double harga_jumlah, double harga) {
        this.status_order = status_order;
        this.waktu_order = waktu_order;
        this.nama_menu = nama_menu;
        this.deskripsi = deskripsi;
        this.unit = unit;
        this.tipe_menu = tipe_menu;
        this.str_gambar = str_gambar;
        this.id_detail = id_detail;
        this.id_menu = id_menu;
        this.id_order = id_order;
        this.harga_jumlah = harga_jumlah;
    }

    public String getStatus_order() {
        return status_order;
    }

    public void setStatus_order(String status_order) {
        this.status_order = status_order;
    }

    public String getWaktu_order() {
        return waktu_order;
    }

    public void setWaktu_order(String waktu_order) {
        this.waktu_order = waktu_order;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTipe_menu() {
        return tipe_menu;
    }

    public void setTipe_menu(String tipe_menu) {
        this.tipe_menu = tipe_menu;
    }

    public String getStr_gambar() {
        return str_gambar;
    }

    public void setStr_gambar(String str_gambar) {
        this.str_gambar = str_gambar;
    }

    public int getId_detail() {
        return id_detail;
    }

    public void setId_detail(int id_detail) {
        this.id_detail = id_detail;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public double getHarga_jumlah() {
        return harga_jumlah;
    }

    public void setHarga_jumlah(double harga_jumlah) {
        this.harga_jumlah = harga_jumlah;
    }

    public int getJumlah_order() {
        return jumlah_order;
    }

    public void setJumlah_order(int jumlah_order) {
        this.jumlah_order = jumlah_order;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
