package com.codev.capturalo.presentation.main;

import com.codev.capturalo.core.BasePresenter;
import com.codev.capturalo.core.BaseView;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;

import java.util.ArrayList;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {
        void showRecommendedAlbums(ArrayList<ArticuloEntity> genreEntities);
        void showStore(TiendaEntity tiendaEntity);

        void showRecommendedMusic(ArrayList<ArticuloEntity> genreEntities);

        void clickItem(ArticuloEntity articuloEntity);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadRecommendedAlbums(String value, int idCategoria);
        void loadRecommendedNewAlbums(String value, int idCategoria);
        void getStoreById(int idStore);

        void loadRecommendedMusic(String value, int idCategoria);
    }
}
