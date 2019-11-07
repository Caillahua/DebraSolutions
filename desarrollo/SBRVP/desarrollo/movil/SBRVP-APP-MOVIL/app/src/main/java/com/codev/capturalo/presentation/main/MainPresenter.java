package com.codev.capturalo.presentation.main;

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
public class MainPresenter implements MainContract.Presenter, ProductItem {

    private MainRepository mMainRepository;

    private final MainContract.View mView;

    private final SessionManager mSessionManager;

    private boolean mFirstLoad = false;




    public MainPresenter(@NonNull MainContract.View view,
                         Context context) {
        this.mView = view;
        this.mSessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
    }

    public MainPresenter(MainRepository mainRepository,
                         MainContract.View mView,
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
    public void loadRecommendedAlbums(String value, int idCategoria) {
        GetRequest genreRequest = ServiceFactory.createService(GetRequest.class);
        Call<ArrayList<ArticuloEntity>> call = genreRequest.getArticulos(value, idCategoria ,value, 0);

        call.enqueue(new Callback<ArrayList<ArticuloEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticuloEntity>> call, Response<ArrayList<ArticuloEntity>> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {
                    mView.showRecommendedAlbums(response.body());
                } else {
                    mView.showErrorMessage("No se pudo acceder a la información");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArticuloEntity>> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No hay conexión a Internet");
            }
        });

    }


    @Override
    public void loadRecommendedNewAlbums(String value, int idCategoria) {
        GetRequest genreRequest = ServiceFactory.createService(GetRequest.class);
        Call<ArrayList<ArticuloEntity>> call = genreRequest.getArticulos(value, idCategoria ,value, 0);

        call.enqueue(new Callback<ArrayList<ArticuloEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticuloEntity>> call, Response<ArrayList<ArticuloEntity>> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {
                    mView.showRecommendedMusic(response.body());
                } else {
                    mView.showErrorMessage("No se pudo acceder a la información");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArticuloEntity>> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No hay conexión a Internet");
            }
        });

    }

    @Override
    public void loadRecommendedMusic(String value, int idCategoria) {
        mView.setLoadingIndicator(true);

        GetRequest genreRequest = ServiceFactory.createService(GetRequest.class);
        Call<ArrayList<ArticuloEntity>> call = genreRequest.getArticulos(value, idCategoria , value, 0);

        call.enqueue(new Callback<ArrayList<ArticuloEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticuloEntity>> call, Response<ArrayList<ArticuloEntity>> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {
                    mView.showRecommendedMusic(response.body());
                } else {
                    mView.showErrorMessage("No se pudo acceder a la información");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArticuloEntity>> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No hay conexión a Internet");
            }
        });

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
    public void clickItem(ArticuloEntity articuloEntity) {
        mView.clickItem(articuloEntity);
    }

    @Override
    public void deleteItem(ArticuloEntity articuloEntity, int position) {

    }
}
