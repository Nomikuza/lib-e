package com.codingstuff.loginandsignup.Domain;

public class PeminjamanModel {
    private String kdPeminjaman;
    private String kdBuku;
    private String nim;
    private String tglPeminjaman;
    private String tglPengembalian;
    private String key;
    private String qr;
    private String status;

    public PeminjamanModel(String kdPeminjaman, String kdBuku, String nim, String tglPeminjaman, String tglPengembalian, String status) {
        this.kdPeminjaman = kdPeminjaman;
        this.kdBuku = kdBuku;
        this.nim = nim;
        this.tglPeminjaman = tglPeminjaman;
        this.tglPengembalian = tglPengembalian;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
    public String getKdPeminjaman() {
        return kdPeminjaman;
    }

    public void setKdPeminjaman(String kdPeminjaman) {
        this.kdPeminjaman = kdPeminjaman;
    }

    public String getKdBuku() {
        return kdBuku;
    }

    public void setKdBuku(String kdBuku) {
        this.kdBuku = kdBuku;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getTglPeminjaman() {
        return tglPeminjaman;
    }

    public void setTglPeminjaman(String tglPeminjaman) {
        this.tglPeminjaman = tglPeminjaman;
    }

    public String getTglPengembalian() {
        return tglPengembalian;
    }

    public void setTglPengembalian(String tglPengembalian) {
        this.tglPengembalian = tglPengembalian;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PeminjamanModel() {

    }
}
