package com.codev.capturalo.presentation.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codev.capturalo.data.ServiceFactory;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.data.model.UserRegister;
import com.codev.capturalo.data.remote.request.LoginRequest;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mRegisterView;
    private SessionManager mSessionManager;

    public RegisterPresenter(@NonNull RegisterContract.View mRegisterView,
                             Context context) {
        Context context1 = checkNotNull(context, "context cannot be null!");
        this.mRegisterView = checkNotNull(mRegisterView, "view cannot be null!");
        this.mRegisterView.setPresenter(this);
        this.mSessionManager = new SessionManager(context1);
    }

    @Override
    public void start() {

    }

    @Override
    public void RegisterUser(String usuario,String contrasenia,String correo, String nombre, String apellido) {
        LoginRequest userRequest =
                ServiceFactory.createService(LoginRequest.class);

       Call<UserRegister> call = userRequest.registerUserId(usuario,contrasenia,correo,2);

        mRegisterView.setLoadingIndicator(true);
        call.enqueue(new Callback<UserRegister>() {
            @Override
            public void onResponse(@NonNull Call<UserRegister> call, @NonNull Response<UserRegister> response) {
                if (response.isSuccessful()) {
                    registerData(nombre,apellido,response.body().getIdUsuario());
                } else {
                    mRegisterView.setLoadingIndicator(false);
                    mRegisterView.showMessage("Este e-mail ya se encuentra registrado");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserRegister> call, @NonNull Throwable t) {
                mRegisterView.setLoadingIndicator(false);
                mRegisterView.showErrorMessage("Revise su conexión a Internet e intentelo nuevamente");
            }
        });
    }

    public void registerData(String nombre, String apellido, int idUser) {
        LoginRequest userRequest =
                ServiceFactory.createService(LoginRequest.class);

        Call<UserEntity> call = userRequest.registerUserData(nombre,apellido,idUser,"","","","",
                "",0);

        mRegisterView.setLoadingIndicator(true);
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(@NonNull Call<UserEntity> call, @NonNull Response<UserEntity> response) {
                if (response.isSuccessful()) {
                    mRegisterView.showMessage("Registro exitoso");
                    openSession(response.body());

                } else {
                    mRegisterView.setLoadingIndicator(false);
                    mRegisterView.showMessage("Este e-mail ya se encuentra registrado");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserEntity> call, @NonNull Throwable t) {
                mRegisterView.setLoadingIndicator(false);
                mRegisterView.showErrorMessage("Revise su conexión a Internet e intentelo nuevamente");
            }
        });
    }

  /*  private void getProfile(final AccessTokenEntity tokenEntity) {
        UserRequest userRequest =
                ServiceFactory.createService(UserRequest.class);
        Call<UserEntity> call = userRequest.getUserProfile("Token " + tokenEntity.getAccessToken());
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(@NonNull Call<UserEntity> call, @NonNull Response<UserEntity> response) {
                if (response.isSuccessful()) {
                    openSession(tokenEntity, response.body());
                } else {
                    mRegisterView.setLoadingIndicator(false);
                    mRegisterView.ShowErrorRegister("Ocurrió un error al cargar su perfil");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserEntity> call, @NonNull Throwable t) {
                mRegisterView.setLoadingIndicator(false);
                mRegisterView.showErrorMessage("Fallo al traer datos, comunicarse con su administrador");
            }
        });*/
   // }*/

    private void openSession(UserEntity userEntity) {
        mSessionManager.openSession();
        mSessionManager.setUser(userEntity);
        mRegisterView.setLoadingIndicator(false);
        mRegisterView.ShowRegisterSuccessful(userEntity);
    }
}
