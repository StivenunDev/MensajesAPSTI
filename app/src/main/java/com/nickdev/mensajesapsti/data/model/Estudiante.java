package com.nickdev.mensajesapsti.data.model;


import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.List; // Importar List


public class Estudiante implements Parcelable {


    // --- MAPEO CON EL BACKEND ---
    // Mapea "id" del JSON de 'user' (visto en logcat)
    @SerializedName("id")
    private int id_estudiante;

    @SerializedName("nombres")
    private String nombres;

    @SerializedName("apellidos")
    private String apellidos;

    @SerializedName("correo")
    private String correoElectronico;

    // Mapea la lista de matriculas que viene del backend
    @SerializedName("matriculas")
    private List<Matricula> matriculas;

    // --- Campo interno para la UI ---
    private boolean seleccionado = false;

    // --- CONSTRUCTORES ---
    public Estudiante() {}

    // --- GETTERS INTELIGENTES ---

    public int getId_estudiante() {
        return id_estudiante;
    }

    public String getNombreCompleto() {
        if (nombres == null) return "Sin nombre";
        if (apellidos == null) return nombres;
        return nombres + " " + apellidos;
    }

    public String getCorreoElectronico() { return correoElectronico != null ? correoElectronico : "Sin correo"; }

    // Devuelve un string vacío, como solicitaste
    public String getTelefono() { return ""; }

    // Lógica para obtener la Abreviatura de la carrera activa
    public String getCarrera() {
        if (matriculas != null && !matriculas.isEmpty()) {
            // (Lógica futura: buscar la matrícula "activa")
            // Por ahora, tomamos la primera
            Carrera carrera = matriculas.get(0).getCarrera();
            if (carrera != null && carrera.getAbreviatura() != null) {
                return carrera.getAbreviatura();
            }
        }
        return "N/A"; // Valor por defecto si no tiene matrícula
    }

    // Lógica para obtener el nombre del Ciclo
    public String getPeriodo() {
        if (matriculas != null && !matriculas.isEmpty()) {
            Ciclo ciclo = matriculas.get(0).getCiclo();
            if (ciclo != null && ciclo.getNombre() != null) {
                // El backend devuelve "Ciclo III", la UI espera "III"
                // Reemplazamos "Ciclo " por "" para que coincida.
                return ciclo.getNombre().replace("Ciclo ", "");
            }
        }
        return "N/A"; // Valor por defecto
    }

    public boolean estaSeleccionado() { return seleccionado; }
    public void establecerSeleccionado(boolean seleccionado) { this.seleccionado = seleccionado; }

    // --- PARCELABLE (Actualizado) ---

    protected Estudiante(Parcel in) {
        id_estudiante = in.readInt();
        nombres = in.readString();
        apellidos = in.readString();
        correoElectronico = in.readString();
        matriculas = in.createTypedArrayList(Matricula.CREATOR); // Leer la lista
        seleccionado = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id_estudiante);
        dest.writeString(nombres);
        dest.writeString(apellidos);
        dest.writeString(correoElectronico);
        dest.writeTypedList(matriculas); // Escribir la lista
        dest.writeByte((byte) (seleccionado ? 1 : 0));
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<Estudiante> CREATOR = new Creator<Estudiante>() {
        @Override
        public Estudiante createFromParcel(Parcel in) { return new Estudiante(in); }
        @Override
        public Estudiante[] newArray(int size) { return new Estudiante[size]; }
    };
}