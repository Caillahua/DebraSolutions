package com.codev.capturalo.data.remote.request;

import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;
import com.codev.capturalo.data.model.UserEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetRequest {
    @GET("BuscarArticulo")
    Call<ArrayList<ArticuloEntity>> getArticulos(@Query("nombreArticulo") String articleName,
                                                 @Query("idCategoria") int idCategoria,
                                                 @Query("caracteristicas") String caracteristica,
                                                 @Query("idTienda") int idTienda);

    @GET("buscarTienda")
    Call<ArrayList<TiendaEntity>> getTienda(@Query("idTienda") int idTienda,
                                 @Query("rsocial") String rsocial,
                                 @Query("delivery") String delivery,
                                 @Query("ventaOnline") String ventaOnline,
                                 @Query("recojoTienda") String recojoTienda);

    @GET("registraVenta")
    Call<Integer> setVenta(@Query("idCliente") int idCliente,
                                            @Query("idMedioPago") int idMedioPago,
                                            @Query("idArticulo") int idArticulo,
                                            @Query("idTienda") int idTienda,
                                            @Query("cantidad") int cantidad);


    @FormUrlEncoded
    @POST("accounts/user/recovery/")
    Call<UserEntity> recovery(@Field("email") String email);

  /*  @FormUrlEncoded
    @POST("accounts/register/")
    Call<AccessTokenEntity> registerUser(@Field("email") String email,
                                         @Field("password") String password,
                                         @Field("first_name") String first_name,
                                         @Field("last_name") String last_name,
                                         @Field("phone") String phone);
*/

   /* @PUT("accounts/user/update/")
    Call<UserEntity> editUser(@Header("Content-Type") String type, @Header("Authorization") String token, @Body UserResponse requestBody);
*/
    @FormUrlEncoded
    @PUT("accounts/user/change-password/{id}/")
    Call<UserEntity> changePassword(@Header("Authorization") String token,
                                    @Field("new_password") String new_password,
                                    @Field("old_password") String last_password,
                                    @Field("email") String email,
                                    @Path("id") String idUser);

    /*@PUT("accounts/user/{id}/photo/")
    Call<UploadResponse> updatePhoto(@Header("Authorization") String token, @Path("id") String idChild, @Body RequestBody body);
*/

/*

    @POST("register/")
    Call<AccessTokenEntity> registerUser(@Body UserEntity userEntity);



    @FormUrlEncoded
    @PUT("user/update/{id}/")
    Call<UserEntity> editUser(@Header("Authorization") String token, @Field("first_name") String first_name, @Field("last_name") String last_name,
                              @Field("gender") String gender, @Field("celphone") String cellphone,
                              @Field("birthdate") String birthdate, @Path("id") String id);*/

}
