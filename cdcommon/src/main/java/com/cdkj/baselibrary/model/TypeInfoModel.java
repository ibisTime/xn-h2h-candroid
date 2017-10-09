package com.cdkj.baselibrary.model;

/**
 * Created by 李先俊 on 2017/8/22.
 */

public class TypeInfoModel {

    /**
     * downloadUrl : https://itunes.apple.com/cn/app/健康e购/id1256905137?mt=8
     * forceUpdate : 1
     * note : 版本更新说明
     * version : 1.1.3
     */

    private String downloadUrl;
    private String forceUpdate;
    private String note;
    private String version;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
