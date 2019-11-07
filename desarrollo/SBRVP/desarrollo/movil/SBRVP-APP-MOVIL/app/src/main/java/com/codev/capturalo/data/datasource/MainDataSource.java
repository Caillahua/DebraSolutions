package com.codev.capturalo.data.datasource;

import android.support.annotation.NonNull;

import com.codev.capturalo.data.model.ArticuloEntity;

import java.util.List;

/**
 * Created by luizkawai on 2/08/17.
 */

public interface MainDataSource {
    interface LoadArtistCallback {

        void onArtistLoaded(List<ArticuloEntity> artistEntities, String next);

        void onDataNotAvailable();
    }

    interface GetArtistCallback {

        void onArtistLoaded(ArticuloEntity artistEntity);

        void onDataNotAvailable();
    }

    interface LoadSongCallback {

        void onSongLoaded(List<ArticuloEntity> songEntity, String next);

        void onDataNotAvailable();
    }

    interface GetSongCallback {

        void onSongLoaded(ArticuloEntity songEntity);

        void onDataNotAvailable();
    }

    void getArtist(@NonNull LoadArtistCallback callback);

    void getArtist(@NonNull String artistId, @NonNull GetArtistCallback callback);

    void saveArtist(@NonNull ArticuloEntity artistEntity);

    void refreshArtist();

    void deleteAllArtist();

    void getSong(@NonNull LoadSongCallback callback);

    void getSong(@NonNull String songId, @NonNull GetSongCallback callback);

    void saveSong(@NonNull ArticuloEntity songEntity);

    void refreshSong();

    void deleteAllSong();
}
