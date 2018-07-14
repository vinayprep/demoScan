package com.example.demoscan;

public class CheckSerial {
    private String message;

    private String code;

    private String serialId;

    private String companySerialId;

    private String productId;

    private String companyProductId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCompanySerialId() {
        return companySerialId;
    }

    public void setCompanySerialId(String companySerialId) {
        this.companySerialId = companySerialId;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyProductId() {
        return companyProductId;
    }

    public void setCompanyProductId(String companyProductId) {
        this.companyProductId = companyProductId;
    }

    @Override
    public String toString() {
        return "ClassPojo [code = " + code +
                ",serialId = " + serialId +
                ",message = " + message +
                ",companySerialId = " + companySerialId +
                ",productId = " + productId +
                ",companyProductId = " + companyProductId + "]";
    }
}