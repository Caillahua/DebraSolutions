package com.codev.capturalo.presentation.store;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codev.capturalo.data.ServiceFactory;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;
import com.codev.capturalo.data.remote.request.GetRequest;
import com.codev.capturalo.data.repositories.MainRepository;
import com.codev.capturalo.presentation.products.ProductItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizkawai on 26/07/17.
 * MainPresenter for Jamm.me
 */
public class StorePresenter implements StoreContract.Presenter, ProductItem {

    private MainRepository mMainRepository;

    private final StoreContract.View mView;

    private final SessionManager mSessionManager;

    private boolean mFirstLoad = false;




    public StorePresenter(@NonNull StoreContract.View view,
                          Context context) {
        this.mView = view;
        this.mSessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
    }

    public StorePresenter(MainRepository mainRepository,
                          StoreContract.View mView,
                          Context context) {
        
        this.mView = mView;
        mSessionManager = new SessionManager(context);
        this.mMainRepository = mainRepository;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        //loadRecommendedAlbums();
        //loadRecommendedMusic();
    }


    @Override
    public void clickItem(ArticuloEntity articuloEntity) {
        mView.clickItem(articuloEntity);
    }

    @Override
    public void deleteItem(ArticuloEntity articuloEntity, int position) {

    }


    @Override
    public void getStoreById(int idStore) {
        GetRequest genreRequest = ServiceFactory.createService(GetRequest.class);
        Call<ArrayList<TiendaEntity>> call = genreRequest.getTienda(idStore,"","","","");

        call.enqueue(new Callback<ArrayList<TiendaEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<TiendaEntity>> call, Response<ArrayList<TiendaEntity>> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {
                    mView.showStore(response.body().get(0));
                } else {
                    mView.showErrorMessage("No se pudo obtener la información");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TiendaEntity>> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No hay conexión a Internet");
            }
        });
    }

    @Override
    public void setBuy(int idCliente, int idTienda, int idArticulo, int cantidad, int idMedioPago) {
        GetRequest genreRequest = ServiceFactory.createService(GetRequest.class);
        Call<Integer> call = genreRequest.setVenta(idCliente, idMedioPago, idArticulo, idTienda, cantidad);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {
                    if(response.code() == 200){
                        mView.buySuccess();
                    }
                } else {
                    mView.showErrorMessage("No se pudo obtener la información");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No hay conexión a Internet");
            }
        });
    }
}
