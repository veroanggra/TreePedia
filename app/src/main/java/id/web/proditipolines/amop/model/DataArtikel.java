package id.web.proditipolines.amop.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataArtikel implements Parcelable{
    private String id_artikel, nama_artikel, teks_artikel, waktu_artikel, id_pegawai, nama_pegawai;

    public DataArtikel() {
    }

    private DataArtikel(Parcel in) {
        id_artikel = in.readString();
        nama_artikel = in.readString();
        teks_artikel = in.readString();
        waktu_artikel = in.readString();
        id_pegawai = in.readString();
        nama_pegawai = in.readString();
    }

    public static final Creator<DataArtikel> CREATOR = new Creator<DataArtikel>() {
        @Override
        public DataArtikel createFromParcel(Parcel in) {
            return new DataArtikel(in);
        }

        @Override
        public DataArtikel[] newArray(int size) {
            return new DataArtikel[size];
        }
    };

    public String getId_artikel() {
        return id_artikel;
    }

    public void setId_artikel(String id_artikel) {
        this.id_artikel = id_artikel;
    }

    public String getNama_artikel() {
        return nama_artikel;
    }

    public void setNama_artikel(String nama_artikel) {
        this.nama_artikel = nama_artikel;
    }

    public String getTeks_artikel() {
        return teks_artikel;
    }

    public void setTeks_artikel(String teks_artikel) {
        this.teks_artikel = teks_artikel;
    }

    public String getWaktu_artikel() {
        return waktu_artikel;
    }

    public void setWaktu_artikel(String waktu_artikel) {
        this.waktu_artikel = waktu_artikel;
    }

    public String getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_artikel);
        parcel.writeString(nama_artikel);
        parcel.writeString(teks_artikel);
        parcel.writeString(waktu_artikel);
        parcel.writeString(id_pegawai);
        parcel.writeString(nama_pegawai);
    }
}
