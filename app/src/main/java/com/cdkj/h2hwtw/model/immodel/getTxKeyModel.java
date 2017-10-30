package com.cdkj.h2hwtw.model.immodel;

/**
 * 用于获取腾讯签名等
 * Created by cdkj on 2017/10/27.
 */

public class getTxKeyModel {

    private String accessKey;//公钥
    private String accountType;
    private String secretKey;//密钥
    private String sig;//签名
    private String txAppAdmin;//应用管理员;
    private int txAppCode;//应用编号

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getTxAppAdmin() {
        return txAppAdmin;
    }

    public void setTxAppAdmin(String txAppAdmin) {
        this.txAppAdmin = txAppAdmin;
    }

    public int getTxAppCode() {
        return txAppCode;
    }

    public void setTxAppCode(int txAppCode) {
        this.txAppCode = txAppCode;
    }
}
