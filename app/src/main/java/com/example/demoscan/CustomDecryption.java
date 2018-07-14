package com.example.demoscan;

/**
 * Created by Rats on 12/6/2017.
 */

public class CustomDecryption {
    String iv = "";
    String value = "";
    String mac = "";

    public CustomDecryption() {
    }

    public CustomDecryption(String iv, String value, String mac) {
        this.iv = iv;
        this.value = value;
        this.mac = mac;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
