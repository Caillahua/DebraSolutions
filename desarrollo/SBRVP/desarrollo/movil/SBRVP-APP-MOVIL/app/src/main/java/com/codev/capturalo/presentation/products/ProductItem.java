package com.codev.capturalo.presentation.products;


import com.codev.capturalo.data.model.ArticuloEntity;

/**
 * Created by kath on 2/06/18.
 */

public interface ProductItem {

    void clickItem(ArticuloEntity articuloEntity);

    void deleteItem(ArticuloEntity articuloEntity, int position);
}
