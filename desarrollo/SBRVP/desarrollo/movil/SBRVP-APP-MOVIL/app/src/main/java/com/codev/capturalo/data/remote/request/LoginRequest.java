package com.codev.capturalo.data.remote.request;


import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.data.model.UserId;
import com.codev.capturalo.data.model.UserRegister;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginRequest {

    @POST("ValidarCredenciales/")
    Call<UserId> login(@Query("usuario") String username,
                       @Query("contrasena") String password);

    @POST("InsertarUsuario/")
    Call<UserRegister> registerUserId(@Query("usuario") String username,
                                     @Query("contrasena") String password,
                                     @Query("correo") String correo,
                                     @Query("perfil") int idPerfil);

    @POST("InsertarCliente/")
    Call<UserEntity> registerUserData(@Query("nombre") String nombre,
                                    @Query("apellido") String password,
                                    @Query("idusuario") int idUser,
                                      @Query("dni") String dni,
                                      @Query("direccion") String direccion,
                                      @Query("rsocial") String rsocial,
                                      @Query("ruc") String ruc,
                                      @Query("estado") String estado,
                                      @Query("credito") int credito);
}
