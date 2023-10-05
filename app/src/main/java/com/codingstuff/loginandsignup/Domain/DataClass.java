package com.codingstuff.loginandsignup.Domain;

public class DataClass {
    private String kdBuku;
    private String nmBuku;
    private String nmPnls;
    private String nmPenerbit;
    private String jumlah;
    private String dataImage;

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

    public DataClass(String kdBuku, String nmBuku, String nmPnls, String nmPenerbit, String jumlah, String dataImage) {
        this.kdBuku = kdBuku;
        this.nmBuku = nmBuku;
        this.nmPnls = nmPnls;
        this.nmPenerbit = nmPenerbit;
        this.jumlah = jumlah;
        this.dataImage = dataImage;
    }

    public DataClass() {

    }
}