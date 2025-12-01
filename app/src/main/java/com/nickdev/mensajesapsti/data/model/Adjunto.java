package com.nickdev.mensajesapsti.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Adjunto implements Parcelable{
    @SerializedName("id")
    private int id;

    @SerializedName("nombreOriginal")
    private String nombreOriginal;

    @SerializedName("url")
    private String url;

    public Adjunto() {}

    // Getters
    public String getUrl() {
        return url;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }


    protected Adjunto(Parcel in) {
        id = in.readInt();
        nombreOriginal = in.readString();
        url = in.readString();
    }

    public static final Creator<Adjunto> CREATOR = new Creator<Adjunto>() {
        @Override
        public Adjunto createFromParcel(Parcel in) { return new Adjunto(in); }

        @Override
        public Adjunto[] newArray(int size) { return new Adjunto[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombreOriginal);
        dest.writeString(url);
    }
}