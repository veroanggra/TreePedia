package id.web.proditipolines.amop.Data;

/**
 * Created by user on 02/06/2017.
 */

public class DataHistory {
    private String no, id_pohon, tanggal, kegiatan, keterangan, qrcode;

    public DataHistory() {

    }

    public DataHistory(String no, String id_pohon, String tanggal, String kegiatan, String keterangan, String qrcode) {
        this.no = no;
        this.id_pohon = id_pohon;
        this.tanggal = tanggal;
        this.kegiatan = kegiatan;
        this.keterangan = keterangan;
        this.qrcode = qrcode;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getId_pohon() {
        return id_pohon;
    }

    public void setId_pohon(String id_pohon) {
        this.id_pohon = id_pohon;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKegiatan() { return kegiatan; }

    public void setKegiatan(String kegiatan) { this.kegiatan = kegiatan; }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getQrcode() { return qrcode; }

    public void setQrcode(String qrcode) { this.qrcode = qrcode; }
}

