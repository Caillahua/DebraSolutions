package com.codev.capturalo.presentation.store;

import com.codev.capturalo.core.BasePresenter;
import com.codev.capturalo.core.BaseView;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface StoreContract {

    interface View extends BaseView<Presenter> {
        void showStore(TiendaEntity tiendaEntity);

        void clickItem(ArticuloEntity articuloEntity);

        void buySuccess();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void getStoreById(int idStore);
        void setBuy(int idCliente, int idTienda, int idArticulo, int cantidad, int idMedioPago );
    }
}
