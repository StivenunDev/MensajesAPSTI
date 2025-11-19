package com.nickdev.mensajesapsti.data.model;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import android.os.Parcelable;

public class Carrera  implements Parcelable {

    @SerializedName("id_carreras")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("abreviatura")
    private String abreviatura;

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getAbreviatura() { return abreviatura; }

    // --- Parcelable ---
    protected Carrera(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        abreviatura = in.readString();
    }

    public static final Creator<Carrera> CREATOR = new Creator<Carrera>() {
        @Override
        public Carrera createFromParcel(Parcel in) { return new Carrera(in); }
        @Override
        public Carrera[] newArray(int size) { return new Carrera[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(abreviatura);
    }
}
