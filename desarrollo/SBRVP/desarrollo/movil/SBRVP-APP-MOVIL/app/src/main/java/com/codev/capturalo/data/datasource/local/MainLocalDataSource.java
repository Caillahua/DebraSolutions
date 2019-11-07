package com.codev.capturalo.data.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.codev.capturalo.data.datasource.MainDataSource;
import com.codev.capturalo.data.local.dbhelper.MainDbHelper;
import com.codev.capturalo.data.local.persistencecontracts.MainPersistenceContract;
import com.codev.capturalo.data.model.ArticuloEntity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luizkawai on 2/08/17.
 */

public class MainLocalDataSource implements MainDataSource {

    private static MainLocalDataSource INSTANCE;

    private MainDbHelper mMainDbHelper;

    // Prevent direct instantiation.
    private MainLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mMainDbHelper = new MainDbHelper(context);
    }

    public static MainLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MainLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getArtist(@NonNull LoadArtistCallback callback) {
        List<ArticuloEntity> artistEntities = new ArrayList<>();
        SQLiteDatabase db = mMainDbHelper.getReadableDatabase();

        String[] projection = {
                MainPersistenceContract.ArtistEntity.ARTIST_ID,
                MainPersistenceContract.ArtistEntity.ARTIST_NAME,
                MainPersistenceContract.ArtistEntity.ARTIST_COVER
        };

        Cursor c = db.query(
                MainPersistenceContract.ArtistEntity.TABLE_NAME,
                projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String artistID = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.ArtistEntity.ARTIST_ID));
                String artistName = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.ArtistEntity.ARTIST_NAME));
                String artistCover = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.ArtistEntity.ARTIST_COVER));

                ArticuloEntity artistEntity = new ArticuloEntity();
                artistEntity.setIdArticulo(Integer.valueOf(artistID));
                artistEntity.setDescripcion(artistName);
                artistEntities.add(artistEntity);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (artistEntities.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onArtistLoaded(artistEntities, null);
        }
    }

    @Override
    public void getArtist(@NonNull String artistId, @NonNull GetArtistCallback callback) {
        SQLiteDatabase db = mMainDbHelper.getReadableDatabase();

        String[] projection = {
                MainPersistenceContract.ArtistEntity.ARTIST_ID,
                MainPersistenceContract.ArtistEntity.ARTIST_NAME,
                MainPersistenceContract.ArtistEntity.ARTIST_COVER
        };


        String selection = MainPersistenceContract.ArtistEntity.ARTIST_ID + " LIKE ?";
        String[] selectionArgs = {artistId};

        Cursor c = db.query(
                MainPersistenceContract.ArtistEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        ArticuloEntity artistEntity = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String artistID = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.ArtistEntity.ARTIST_ID));
            String artistName = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.ArtistEntity.ARTIST_NAME));
            String artistCover = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.ArtistEntity.ARTIST_COVER));

            artistEntity = new ArticuloEntity();
            artistEntity.setIdArticulo(Integer.valueOf(artistID));
            artistEntity.setDescripcion(artistName);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (artistEntity != null) {
            callback.onArtistLoaded(artistEntity);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveArtist(@NonNull ArticuloEntity artistEntity) {
        checkNotNull(artistEntity);
        SQLiteDatabase db = mMainDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MainPersistenceContract.ArtistEntity.ARTIST_ID, artistEntity.getIdArticulo());
        values.put(MainPersistenceContract.ArtistEntity.ARTIST_NAME, artistEntity.getDescripcion());
        values.put(MainPersistenceContract.ArtistEntity.ARTIST_COVER, artistEntity.getRutaImagen());

        db.insert(MainPersistenceContract.ArtistEntity.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void refreshArtist() {

    }

    @Override
    public void deleteAllArtist() {
        SQLiteDatabase db = mMainDbHelper.getWritableDatabase();

        db.delete(MainPersistenceContract.ArtistEntity.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void getSong(@NonNull LoadSongCallback callback) {
        List<ArticuloEntity> songEntities = new ArrayList<>();
        SQLiteDatabase db = mMainDbHelper.getReadableDatabase();

        String[] projection = {
                MainPersistenceContract.SongEntity.SONG_ID,
                MainPersistenceContract.SongEntity.SONG_NAME,
                MainPersistenceContract.SongEntity.SONG_COVER
        };

        Cursor c = db.query(
                MainPersistenceContract.SongEntity.TABLE_NAME,
                projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String songID = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.SongEntity.SONG_ID));
                String songName = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.SongEntity.SONG_NAME));
                String songCover = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.SongEntity.SONG_COVER));

                ArticuloEntity songEntity = new ArticuloEntity();

                songEntity.setIdArticulo(Integer.valueOf(songID));
                songEntity.setDescripcion(songName);
                songEntities.add(songEntity);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (songEntities.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onSongLoaded(songEntities, null);
        }
    }

    @Override
    public void getSong(@NonNull String songId, @NonNull GetSongCallback callback) {
        SQLiteDatabase db = mMainDbHelper.getReadableDatabase();

        String[] projection = {
                MainPersistenceContract.SongEntity.SONG_ID,
                MainPersistenceContract.SongEntity.SONG_NAME,
                MainPersistenceContract.SongEntity.SONG_COVER
        };


        String selection = MainPersistenceContract.SongEntity.SONG_ID + " LIKE ?";
        String[] selectionArgs = {songId};

        Cursor c = db.query(
                MainPersistenceContract.SongEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        ArticuloEntity songEntity = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String songID = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.SongEntity.SONG_ID));
            String songName = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.SongEntity.SONG_NAME));
            String songCover = c.getString(c.getColumnIndexOrThrow(MainPersistenceContract.SongEntity.SONG_COVER));

            songEntity = new ArticuloEntity();
            songEntity.setIdArticulo(Integer.valueOf(songID));
            songEntity.setDescripcion(songName);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (songEntity != null) {
            callback.onSongLoaded(songEntity);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveSong(@NonNull ArticuloEntity songEntity) {
        checkNotNull(songEntity);
        SQLiteDatabase db = mMainDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MainPersistenceContract.SongEntity.SONG_ID, songEntity.getIdArticulo());
        values.put(MainPersistenceContract.SongEntity.SONG_NAME, songEntity.getDescripcion());
        values.put(MainPersistenceContract.SongEntity.SONG_COVER, songEntity.getRutaImagen());

        db.insert(MainPersistenceContract.SongEntity.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void refreshSong() {

    }

    @Override
    public void deleteAllSong() {
        SQLiteDatabase db = mMainDbHelper.getWritableDatabase();

        db.delete(MainPersistenceContract.SongEntity.TABLE_NAME, null, null);

        db.close();
    }
}
