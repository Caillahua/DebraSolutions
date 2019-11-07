package com.codev.capturalo.presentation.login;


import com.codev.capturalo.core.BasePresenter;
import com.codev.capturalo.core.BaseView;
import com.codev.capturalo.data.model.UserEntity;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void successLogin(UserEntity userEntity);

        void errorLogin(String msg);

        void showDialogForgotPassword();

        void showSendEmail(String email);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loginUser(String username, String password);

        void loginFacebook(String token);

        void getProfile(int id);

       // void openSession(AccessTokenEntity token, UserEntity userEntity);

        void sendEmail(String email);
    }
}
