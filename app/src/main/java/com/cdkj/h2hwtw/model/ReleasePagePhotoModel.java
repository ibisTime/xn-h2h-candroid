package com.cdkj.h2hwtw.model;

/**
 * Created by 李先俊 on 2017/10/12.
 */

public class ReleasePagePhotoModel {

    private String imgPath;

    private boolean isHeart;//是否头部


    private int index;//用于记录下标

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isHeart() {
        return isHeart;
    }

    public void setHeart(boolean heart) {
        isHeart = heart;
    }
}
