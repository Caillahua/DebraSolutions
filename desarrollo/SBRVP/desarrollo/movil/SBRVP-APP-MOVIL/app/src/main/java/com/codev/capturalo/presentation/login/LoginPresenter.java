package com.codev.capturalo.presentation.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codev.capturalo.data.ServiceFactory;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.data.model.UserId;
import com.codev.capturalo.data.remote.request.LoginRequest;
import com.codev.capturalo.data.remote.request.UserRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mLoginView;
    private final SessionManager mSessionManager;

    public LoginPresenter(@NonNull LoginContract.View mLoginView,
                          Context context) {
        this.mLoginView = checkNotNull(mLoginView, "newsView cannot be null!");
        this.mLoginView.setPresenter(this);
        mSessionManager = new SessionManager(context);
    }

    @Override
    public void start() {
    }

    @Override
    public void loginUser(String username, String password) {
        //AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
        //getProfile(accessTokenEntity);

        LoginRequest loginService =
                ServiceFactory.createService(LoginRequest.class);
        Call<UserId> call = loginService.login(username, password);
        mLoginView.setLoadingIndicator(true);
        call.enqueue(new Callback<UserId>() {
            @Override
            public void onResponse(@NonNull Call<UserId> call, @NonNull Response<UserId> response) {
                if (!mLoginView.isActive()) {
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().getIdUsuario()>1){
                        getProfile(response.body().getIdUsuario());
                    } else {
                        mLoginView.setLoadingIndicator(false);
                        mLoginView.errorLogin("Usuario o contraseña incorrectos");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserId> call, @NonNull Throwable t) {
                mLoginView.setLoadingIndicator(false);
                mLoginView.errorLogin("Ocurrió un error al tratar de ingresar, por favor intente nuevamente");
            }
        });
    }

    @Override
    public void loginFacebook(String token) {
        LoginRequest loginService =
                ServiceFactory.createService(LoginRequest.class);

       /* Call<AccessTokenEntity> call = loginService.loginFacebook(token);
        mLoginView.setLoadingIndicator(true);
        call.enqueue(new Callback<AccessTokenEntity>() {
            @Override
            public void onResponse(@NonNull Call<AccessTokenEntity> call, @NonNull Response<AccessTokenEntity> response) {
                if (!mLoginView.isActive()) {
                    return;
                }
                if (response.isSuccessful()) {
                    getProfile(response.body());
                } else {
                    mLoginView.setLoadingIndicator(false);
                    AccessToken.setCurrentAccessToken(null);
                    mLoginView.errorLogin("Login fallido, puede que el correo vinculado a su " +
                            "facebook ya este registrado ");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessTokenEntity> call, @NonNull Throwable t) {
                if (!mLoginView.isActive()) {
                    return;
                }
                mLoginView.setLoadingIndicator(false);
                AccessToken.setCurrentAccessToken(null);
                mLoginView.errorLogin("Ocurrió un error al entrar, por favor intente nuevamente");
            }
        });*/
    }

    @Override
    public void getProfile(int id) {
       // UserEntity userEntity = new UserEntity();
        //userEntity.setFirst_name("Katherine");
        //userEntity.setEmail("katherine.caillahua@gmail.com");
        //userEntity.setLast_name("Caillahua");
        //openSession(tokenEntity, userEntity);

        UserRequest userRequest =
                ServiceFactory.createService(UserRequest.class);
        Call<UserEntity> call = userRequest.getUserProfile(id);
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(@NonNull Call<UserEntity> call, @NonNull Response<UserEntity> response) {
                if (!mLoginView.isActive()) {
                    return;
                }
                if (response.isSuccessful()) {
                    openSession(response.body());
                } else {
                    mLoginView.setLoadingIndicator(false);
                    mLoginView.errorLogin("Ocurrió un error al cargar su perfil");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserEntity> call, @NonNull Throwable t) {
                if (!mLoginView.isActive()) {
                    return;
                }
                mLoginView.setLoadingIndicator(false);
                mLoginView.errorLogin("Ocurrió un falló al traer datos, comunicarse con su administrador");
            }
        });
    }

    public void openSession(UserEntity userEntity) {
        mSessionManager.openSession();
        mSessionManager.setUser(userEntity);
        mLoginView.setLoadingIndicator(false);
        mLoginView.successLogin(userEntity);
    }

    @Override
    public void sendEmail(String email) {
        UserRequest userRequest = ServiceFactory.createService(UserRequest.class);
        Call<UserEntity> call = userRequest.recovery(email);
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                if (!mLoginView.isActive()) {
                    return;
                }
                if (response.isSuccessful()) {
                    mLoginView.showMessage("Se envió un correo con instrucciones");
                } else {
                    mLoginView.setLoadingIndicator(false);
                    mLoginView.errorLogin("Ocurrió un error al intentar enviar el correo de recoperación");
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                if (!mLoginView.isActive()) {
                    return;
                }
                mLoginView.setLoadingIndicator(false);
                mLoginView.errorLogin("Fallo al traer datos, comunicarse con su administrador");
            }
        });
    }
}
