package com.example.nghia.adapter;

/**
 * Created by Administrator on 18/07/2016.
 */
public class Dataprovider {
    private int image_id1;
    private int image_id2;
    private int image_id3;

    public Dataprovider(int image_id1, int image_id2, int image_id3) {
        this.image_id1 = image_id1;
        this.image_id2 = image_id2;
        this.image_id3 = image_id3;
    }

    public int getImage_id1() {
        return image_id1;
    }

    public void setImage_id1(int image_id1) {
        this.image_id1 = image_id1;
    }

    public int getImage_id2() {
        return image_id2;
    }

    public void setImage_id2(int image_id2) {
        this.image_id2 = image_id2;
    }

    public int getImage_id3() {
        return image_id3;
    }

    public void setImage_id3(int image_id3) {
        this.image_id3 = image_id3;
    }
}
