package com.nickdev.mensajesapsti.data.model;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import android.os.Parcelable;

public class Ciclo implements Parcelable {

    @SerializedName("id_ciclos")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }

    // --- Parcelable ---
    protected Ciclo(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public static final Creator<Ciclo> CREATOR = new Creator<Ciclo>() {
        @Override
        public Ciclo createFromParcel(Parcel in) { return new Ciclo(in); }
        @Override
        public Ciclo[] newArray(int size) { return new Ciclo[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
    }
}

