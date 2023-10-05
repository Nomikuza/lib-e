package com.codingstuff.loginandsignup.Adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseRVModal implements Parcelable {
    private String kdBuku;
    private String nmBuku;
    private String nmPnls;
    private String nmPenerbit;
    private String jumlah;

    public CourseRVModal(){

    }

    protected CourseRVModal(Parcel in) {
        kdBuku = in.readString();
        nmBuku = in.readString();
        nmPnls = in.readString();
        nmPenerbit = in.readString();
        jumlah = in.readString();
    }

    public static final Creator<CourseRVModal> CREATOR = new Creator<CourseRVModal>() {
        @Override
        public CourseRVModal createFromParcel(Parcel in) {
            return new CourseRVModal(in);
        }

        @Override
        public CourseRVModal[] newArray(int size) {
            return new CourseRVModal[size];
        }
    };

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

    public CourseRVModal(String kdBuku, String nmBuku, String nmPnls, String nmPenerbit, String jumlah) {
        this.kdBuku = kdBuku;
        this.nmBuku = nmBuku;
        this.nmPnls = nmPnls;
        this.nmPenerbit = nmPenerbit;
        this.jumlah = jumlah;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(kdBuku);
        parcel.writeString(nmBuku);
        parcel.writeString(nmPnls);
        parcel.writeString(nmPenerbit);
        parcel.writeString(jumlah);
    }
}
