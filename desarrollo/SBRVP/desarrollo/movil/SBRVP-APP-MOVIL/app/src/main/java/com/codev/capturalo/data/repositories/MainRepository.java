package com.codev.capturalo.data.repositories;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codev.capturalo.data.datasource.MainDataSource;
import com.codev.capturalo.data.model.ArticuloEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luizkawai on 2/08/17.
 */

public class MainRepository implements MainDataSource {

    private static MainRepository INSTANCE = null;

    private final MainDataSource mMainRemoteDataSource;

    private final MainDataSource mMainLocalDataSource;

    private String NEXT_PAGE = null;

    /**
     * Let us use this data to local test
     */
    Map<String, ArticuloEntity> mCachedArtist;
    Map<String, ArticuloEntity> mCachedSong;

    /**
     * Marks the cache as invalid, and force us to update the next request
     */
    boolean mArtistCacheIsDirty = false;
    boolean mSongCacheIsDirty = false;

    private MainRepository(@NonNull MainDataSource mainRemoteDataSource,
                           @NonNull MainDataSource mainLocalDataSource) {

        this.mMainRemoteDataSource = checkNotNull(mainRemoteDataSource);
        this.mMainLocalDataSource = checkNotNull(mainLocalDataSource);
    }

    public static MainRepository getInstance(MainDataSource mainRemoteDataSource,
                                             MainDataSource mainLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MainRepository(mainRemoteDataSource, mainLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getArtist(@NonNull final LoadArtistCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedArtist != null && !mArtistCacheIsDirty) {
            callback.onArtistLoaded(new ArrayList<>(mCachedArtist.values()), NEXT_PAGE);
            return;
        }
        if (mArtistCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getArtistFromRemoteDataSource(new LoadArtistCallback() {
                @Override
                public void onArtistLoaded(List<ArticuloEntity> artistEntity, String next) {
                    NEXT_PAGE = next;
                    callback.onArtistLoaded(artistEntity, next);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        } else {
            // Query the local storage if available. If not, query the network.
            mMainLocalDataSource.getArtist(new LoadArtistCallback() {
                @Override
                public void onArtistLoaded(List<ArticuloEntity> artist, String next) {
                    refreshArtistCache(artist);
                    callback.onArtistLoaded(new ArrayList<>(mCachedArtist.values()), next);
                }

                @Override
                public void onDataNotAvailable() {
                    getArtistFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getArtist(@NonNull final String artistId, @NonNull final GetArtistCallback callback) {
        checkNotNull(artistId);
        checkNotNull(callback);

        ArticuloEntity cachedArtist = getArtistWithId(artistId);

        // Respond immediately with cache if available
        if (cachedArtist != null) {
            callback.onArtistLoaded(cachedArtist);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mMainLocalDataSource.getArtist(artistId, new GetArtistCallback() {
            @Override
            public void onArtistLoaded(ArticuloEntity artist) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedArtist == null) {
                    mCachedArtist = new LinkedHashMap<>();
                }
                mCachedArtist.put(String.valueOf(artist.getIdArticulo()), artist);
                callback.onArtistLoaded(artist);
            }

            @Override
            public void onDataNotAvailable() {
                mMainRemoteDataSource.getArtist(artistId, new GetArtistCallback() {
                    @Override
                    public void onArtistLoaded(ArticuloEntity baby) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedArtist == null) {
                            mCachedArtist = new LinkedHashMap<>();
                        }
                        mCachedArtist.put(String.valueOf(baby.getIdArticulo()), baby);
                        callback.onArtistLoaded(baby);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveArtist(@NonNull ArticuloEntity artist) {
        checkNotNull(artist);
        mMainRemoteDataSource.saveArtist(artist);
        mMainLocalDataSource.saveArtist(artist);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedArtist == null) {
            mCachedArtist = new LinkedHashMap<>();
        }
        mCachedArtist.put(String.valueOf(artist.getIdArticulo()), artist);
    }

    @Override
    public void refreshArtist() {
        mArtistCacheIsDirty = true;
    }

    @Override
    public void deleteAllArtist() {
        mMainRemoteDataSource.deleteAllArtist();
        mMainLocalDataSource.deleteAllArtist();

        if (mCachedArtist == null) {
            mCachedArtist = new LinkedHashMap<>();
        }
        mCachedArtist.clear();
    }

    private void getArtistFromRemoteDataSource(@NonNull final LoadArtistCallback callback) {
        mMainRemoteDataSource.getArtist(new LoadArtistCallback() {
            @Override
            public void onArtistLoaded(List<ArticuloEntity> artist, String next) {
                refreshArtistCache(artist);
                refreshArtistLocalDataSource(artist);
                callback.onArtistLoaded(new ArrayList<>(mCachedArtist.values()), next);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshArtistCache(List<ArticuloEntity> artistEntities) {
        if (mCachedArtist == null) {
            mCachedArtist = new LinkedHashMap<>();
        }
        mCachedArtist.clear();
        for (ArticuloEntity artist : artistEntities) {
            mCachedArtist.put(String.valueOf(artist.getIdArticulo()), artist);
        }
        mArtistCacheIsDirty = false;
    }

    private void refreshArtistLocalDataSource(List<ArticuloEntity> artistEntities) {
        mMainLocalDataSource.deleteAllArtist();
        for (ArticuloEntity artist : artistEntities) {
            mMainLocalDataSource.saveArtist(artist);
        }
    }

    @Nullable
    private ArticuloEntity getArtistWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedArtist == null || mCachedArtist.isEmpty()) {
            return null;
        } else {
            return mCachedArtist.get(id);
        }
    }

    @Override
    public void getSong(@NonNull final LoadSongCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedSong != null && !mSongCacheIsDirty) {
            callback.onSongLoaded(new ArrayList<>(mCachedSong.values()), NEXT_PAGE);
            return;
        }
        if (mSongCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getSongFromRemoteDataSource(new LoadSongCallback() {
                @Override
                public void onSongLoaded(List<ArticuloEntity> songEntity, String next) {
                    NEXT_PAGE = next;
                    callback.onSongLoaded(songEntity, next);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        } else {
            // Query the local storage if available. If not, query the network.
            mMainLocalDataSource.getSong(new LoadSongCallback() {
                @Override
                public void onSongLoaded(List<ArticuloEntity> song, String next) {
                    refreshSongCache(song);
                    callback.onSongLoaded(new ArrayList<>(mCachedSong.values()), next);
                }

                @Override
                public void onDataNotAvailable() {
                    getSongFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getSong(@NonNull final String songId, @NonNull final GetSongCallback callback) {
        checkNotNull(songId);
        checkNotNull(callback);

        ArticuloEntity cachedSong = getSongWithId(songId);

        // Respond immediately with cache if available
        if (cachedSong != null) {
            callback.onSongLoaded(cachedSong);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mMainLocalDataSource.getSong(songId, new GetSongCallback() {
            @Override
            public void onSongLoaded(ArticuloEntity song) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedSong == null) {
                    mCachedSong = new LinkedHashMap<>();
                }
                mCachedSong.put(String.valueOf(song.getIdArticulo()), song);
                callback.onSongLoaded(song);
            }

            @Override
            public void onDataNotAvailable() {
                mMainRemoteDataSource.getSong(songId, new GetSongCallback() {
                    @Override
                    public void onSongLoaded(ArticuloEntity song) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedSong == null) {
                            mCachedSong = new LinkedHashMap<>();
                        }
                        mCachedSong.put(String.valueOf(song.getIdArticulo()), song);
                        callback.onSongLoaded(song);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveSong(@NonNull ArticuloEntity songEntity) {
        checkNotNull(songEntity);
        mMainRemoteDataSource.saveSong(songEntity);
        mMainLocalDataSource.saveSong(songEntity);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedSong == null) {
            mCachedSong = new LinkedHashMap<>();
        }
        mCachedSong.put(String.valueOf(songEntity.getIdArticulo()), songEntity);
    }

    @Override
    public void refreshSong() {
        mSongCacheIsDirty = true;
    }

    @Override
    public void deleteAllSong() {
        mMainRemoteDataSource.deleteAllSong();
        mMainLocalDataSource.deleteAllSong();

        if (mCachedSong == null) {
            mCachedSong = new LinkedHashMap<>();
        }
        mCachedSong.clear();
    }

    private void getSongFromRemoteDataSource(@NonNull final LoadSongCallback callback) {
        mMainRemoteDataSource.getSong(new LoadSongCallback() {
            @Override
            public void onSongLoaded(List<ArticuloEntity> song, String next) {
                refreshSongCache(song);
                refreshSongLocalDataSource(song);
                callback.onSongLoaded(new ArrayList<>(mCachedSong.values()), next);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshSongCache(List<ArticuloEntity> songEntities) {
        if (mCachedSong == null) {
            mCachedSong = new LinkedHashMap<>();
        }
        mCachedSong.clear();
        for (ArticuloEntity song : songEntities) {
            mCachedSong.put(String.valueOf(song.getIdArticulo()), song);
        }
        mSongCacheIsDirty = false;
    }

    private void refreshSongLocalDataSource(List<ArticuloEntity> songEntities) {
        mMainLocalDataSource.deleteAllSong();
        for (ArticuloEntity song : songEntities) {
            mMainLocalDataSource.saveSong(song);
        }
    }

    @Nullable
    private ArticuloEntity getSongWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedSong == null || mCachedSong.isEmpty()) {
            return null;
        } else {
            return mCachedSong.get(id);
        }
    }
}
