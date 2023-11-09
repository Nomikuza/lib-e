package com.codingstuff.loginandsignup.Domain;

public class DataClass {
    private String kdBuku;
    private String nmBuku;
    private String nmPnls;
    private String nmPenerbit;
    private String jumlah;
    private String dataImage;
    private String hal;
    private String key;


    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKdBuku() {
        return kdBuku;
    }

    public void setKdBuku(String kdBuku) {
        this.kdBuku = kdBuku;
    }

    public String getNmBuku() {
        return nmBuku;
    }

    public void setNmBuku(String nmBuku) {
        this.nmBuku = nmBuku;
    }

    public String getNmPnls() {
        return nmPnls;
    }

    public void setNmPnls(String nmPnls) {
        this.nmPnls = nmPnls;
    }

    public String getNmPenerbit() {
        return nmPenerbit;
    }

    public void setNmPenerbit(String nmPenerbit) {
        this.nmPenerbit = nmPenerbit;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }


    public DataClass(String dataImage, String desc, String hal, String jumlah, String kdBuku, String nmBuku, String nmPenerbit, String nmPnls) {
        this.kdBuku = kdBuku;
        this.nmBuku = nmBuku;
        this.nmPnls = nmPnls;
        this.nmPenerbit = nmPenerbit;
        this.jumlah = jumlah;
        this.dataImage = dataImage;
        this.hal = hal;

        this.desc = desc;
    }

    public DataClass() {

    }
}