package id.sch.smktelkom_mlg.learn.recyclerview3.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Rokerecekecek on 26/01/2017.
 */
public class Hotel {
    public String judul;
    public String deskripsi;
    public String lokasi;
    public Drawable foto;

    public Hotel(String judul, String deskripsi,String lokasi, Drawable foto) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.lokasi=lokasi;
        this.foto = foto;
    }
}
