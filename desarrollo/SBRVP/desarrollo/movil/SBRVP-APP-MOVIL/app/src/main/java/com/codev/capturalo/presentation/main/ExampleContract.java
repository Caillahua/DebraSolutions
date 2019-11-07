package com.codev.capturalo.presentation.main;

import com.codev.capturalo.core.BasePresenter;
import com.codev.capturalo.core.BaseView;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface ExampleContract {

    interface View extends BaseView<Presenter> {


        boolean isActive();
    }

    interface Presenter extends BasePresenter {


    }
}
