package com.codev.capturalo.presentation.register;

import com.codev.capturalo.core.BasePresenter;
import com.codev.capturalo.core.BaseView;
import com.codev.capturalo.data.model.UserEntity;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void ShowRegisterSuccessful(UserEntity userEntity);
        void ShowErrorRegister(String msg);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void RegisterUser(String usuario,String contrasenia,String correo, String nombre, String apellido);
    }
}
