package com.nickdev.mensajesapsti.data.network;

import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.data.model.Mensaje;
import com.nickdev.mensajesapsti.data.network.models.LoginRequest;
import com.nickdev.mensajesapsti.data.network.models.LoginResponse;
import com.nickdev.mensajesapsti.data.model.api.MensajeRequest;
import java.util.List;
import okhttp3.MultipartBody; // Importante
import okhttp3.ResponseBody;  // Importante
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import java.util.Map;

public interface ApiService {

    @POST("/api/v1/auth/login/admin")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/v1/estudiante")
    Call<List<Estudiante>> getEstudiantes();


    @GET("/api/v1/mensaje")
    Call<List<Mensaje>> obtenerMensajesEnviados();

    @Multipart
    @POST("/api/v1/files/upload")
    Call<Map<String, String>> uploadFile(@Part MultipartBody.Part file);

    @POST("/api/v1/mensaje")
    Call<Void> crearMensaje(@Body MensajeRequest request);
}