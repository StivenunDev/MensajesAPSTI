package com.nickdev.mensajesapsti.data.model;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import android.os.Parcelable;

public class Matricula implements Parcelable {

    @SerializedName("id_matriculas")
    private int id;

    @SerializedName("carrera")
    private Carrera carrera;

    @SerializedName("ciclo")
    private Ciclo ciclo;

    @SerializedName("estado")
    private String estado;

    // Getters
    public int getId() { return id; }
    public Carrera getCarrera() { return carrera; }
    public Ciclo getCiclo() { return ciclo; }
    public String getEstado() { return estado; }

    // --- Parcelable ---
    protected Matricula(Parcel in) {
        id = in.readInt();
        carrera = in.readParcelable(Carrera.class.getClassLoader());
        ciclo = in.readParcelable(Ciclo.class.getClassLoader());
        estado = in.readString();
    }

    public static final Creator<Matricula> CREATOR = new Creator<Matricula>() {
        @Override
        public Matricula createFromParcel(Parcel in) { return new Matricula(in); }
        @Override
        public Matricula[] newArray(int size) { return new Matricula[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(carrera, flags);
        dest.writeParcelable(ciclo, flags);
        dest.writeString(estado);
    }
}