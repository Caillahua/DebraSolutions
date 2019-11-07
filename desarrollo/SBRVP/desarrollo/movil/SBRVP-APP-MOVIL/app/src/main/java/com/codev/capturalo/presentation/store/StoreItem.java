package com.codev.capturalo.presentation.store;


import com.codev.capturalo.data.model.ArticuloEntity;

/**
 * Created by kath on 2/06/18.
 */

public interface StoreItem {

    void clickItem(ArticuloEntity articuloEntity);

    void deleteItem(ArticuloEntity articuloEntity, int position);
}
