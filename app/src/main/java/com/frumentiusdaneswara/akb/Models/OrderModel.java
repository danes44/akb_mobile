package com.frumentiusdaneswara.akb.Models;

public class OrderModel {
    private String  nama_customer;
    private int id_reservasi, id_order;

    public OrderModel(int id_reservasi, int id_order) {
        this.id_reservasi = id_reservasi;
        this.id_order = id_order;
    }

//    public String getNama_customer() {
//        return nama_customer;
//    }
//
//    public void setNama_customer(String nama_customer) {
//        this.nama_customer = nama_customer;
//    }

    public int getId_reservasi() {
        return id_reservasi;
    }

    public void setId_reservasi(int id_reservasi) {
        this.id_reservasi = id_reservasi;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }
}
