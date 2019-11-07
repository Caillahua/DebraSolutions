package com.codev.capturalo.data.datasource.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codev.capturalo.data.datasource.MainDataSource;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.ArticuloEntity;

/**
 * Created by luizkawai on 2/08/17.
 */

public class MainRemoteDataSource implements MainDataSource {

    private static MainRemoteDataSource INSTANCE;
    private Context mContext;
    private SessionManager mSessionManager;


    public static MainRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MainRemoteDataSource(context);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private MainRemoteDataSource(Context context) {
        this.mContext = context;
        this.mSessionManager = new SessionManager(context);

    }

    @Override
    public void getArtist(@NonNull final LoadArtistCallback callback) {
       /* ArticuloEntity artistRequest = ServiceFactory.createService(GenreRequest.class);
        Call<TrackEntityHolder<ArticuloEntity>> call = artistRequest.getGenres();
        call.enqueue(new Callback<TrackEntityHolder<ArticuloEntity>>() {
            @Override
            public void onResponse(Call<TrackEntityHolder<ArticuloEntity>> call, Response<TrackEntityHolder<ArticuloEntity>> response) {

                if (response.isSuccessful()) {
                    callback.onArtistLoaded(response.body().getResults(),
                            response.body().getNext());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<TrackEntityHolder<ArticuloEntity>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });*/
    }

    @Override
    public void getArtist(@NonNull String artistId, @NonNull GetArtistCallback callback) {

    }

    @Override
    public void saveArtist(@NonNull ArticuloEntity task) {

    }

    @Override
    public void refreshArtist() {

    }

    @Override
    public void deleteAllArtist() {

    }

    @Override
    public void getSong(@NonNull final LoadSongCallback callback) {
        /*GenreRequest songRequest = ServiceFactory.createService(GenreRequest.class);
        Call<TrackEntityHolder<ArticuloEntity>> call = songRequest.getGenres();
        call.enqueue(new Callback<TrackEntityHolder<ArticuloEntity>>() {
            @Override
            public void onResponse(Call<TrackEntityHolder<ArticuloEntity>> call, Response<TrackEntityHolder<GenreEntity>> response) {

                if (response.isSuccessful()) {
                    callback.onSongLoaded(response.body().getResults(),
                            response.body().getNext());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<TrackEntityHolder<ArticuloEntity>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });*/
    }

    @Override
    public void getSong(@NonNull String songId, @NonNull GetSongCallback callback) {

    }

    @Override
    public void saveSong(@NonNull ArticuloEntity songEntity) {

    }

    @Override
    public void refreshSong() {

    }

    @Override
    public void deleteAllSong() {

    }
}
