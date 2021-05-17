package com.frumentiusdaneswara.akb.API;

public class OrderAPI {
    public static final String ROOT_URL   = "https://4c18e9bfa0ab.ngrok.io";
    public static final String ROOT_API   = ROOT_URL+ "/api";
    public static final String URL_SELECT = ROOT_API+"/order";
    public static final String URL_SELECT_ID = ROOT_API+"/order/byReservasi/";
    public static final String URL_ADD    = ROOT_API+"/order";
    public static final String URL_UPDATE = ROOT_API+"/order/"; //ketambahan id
    public static final String URL_DELETE = ROOT_API+"/order/"; //ketambahan id
}
