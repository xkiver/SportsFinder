package cl.telematica.android.sportsfinder.Model;

/**
 * Created by cristian on 28-10-2016.
 */

import io.realm.RealmObject;

/**
 * Created by luxor on 27-10-16.
 */

public class Place extends RealmObject{

    private String name;
    private String descripction;
    private String imagen;
    private double latt;
    private double longi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripction() {
        return descripction;
    }

    public void setDescripction(String descripction) {
        this.descripction = descripction;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getLatt() {
        return latt;
    }

    public void setLatt(double latt) {
        this.latt = latt;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

}

