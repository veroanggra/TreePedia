package id.web.proditipolines.amop.Data;

/**
 * Created by user on 01/06/2017.
 */

public class DataPohon {
    private String id, id_pegawai, last_update, jenis_pohon, usia_pohon, kondisi_pohon, latitude, longtitude, foto_pohon, keterangan, status, qrcode;

    public DataPohon(){

    }

    public DataPohon(String id, String id_pegawai, String last_update, String jenis_pohon, String usia_pohon, String kondisi_pohon, String latitude, String longtitude, String foto_pohon, String keterangan, String status, String qrcode) {
        this.id = id;
        this.id_pegawai = id_pegawai;
        this.last_update = last_update;
        this.jenis_pohon = jenis_pohon;
        this.usia_pohon = usia_pohon;
        this.kondisi_pohon = kondisi_pohon;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.foto_pohon = foto_pohon;
        this.keterangan = keterangan;
        this.status = status;
        this.qrcode = qrcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getJenis_pohon() {
        return jenis_pohon;
    }

    public void setJenis_pohon(String jenis_pohon) {
        this.jenis_pohon = jenis_pohon;
    }

    public String getUsia_pohon() {
        return usia_pohon;
    }

    public void setUsia_pohon(String usia_pohon) {
        this.usia_pohon = usia_pohon;
    }

    public String getKondisi_pohon() {
        return kondisi_pohon;
    }

    public void setKondisi_pohon(String kondisi_pohon) {
        this.kondisi_pohon = kondisi_pohon;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getFoto_pohon() {
        return foto_pohon;
    }

    public void setFoto_pohon(String foto_pohon) {
        this.foto_pohon = foto_pohon;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}

