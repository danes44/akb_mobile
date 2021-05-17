package com.frumentiusdaneswara.akb.API;

public class DetailOrderAPI {
    public static final String ROOT_URL             = "https://4c18e9bfa0ab.ngrok.io";
    public static final String ROOT_API             = ROOT_URL+ "/api";
    public static final String URL_SELECT           = ROOT_API+"/detailOrder/showCart/";
    public static final String URL_ADD              = ROOT_API+"/detailOrder";
    public static final String URL_UPDATE_STATUS    = ROOT_API+"/detailOrder/status/"; //ketambahan id
    public static final String URL_DELETE           = ROOT_API+"/detailOrder/"; //ketambahan id
}
