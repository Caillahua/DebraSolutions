/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codev.capturalo.data.local.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codev.capturalo.data.local.persistencecontracts.MainPersistenceContract;

public class MainDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "DanMusic.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ARTIST =
            "CREATE TABLE IF NOT EXISTS " + MainPersistenceContract.ArtistEntity.TABLE_NAME + " (" +
                    MainPersistenceContract.ArtistEntity._ID + TEXT_TYPE + " PRIMARY KEY," +
                    MainPersistenceContract.ArtistEntity.ARTIST_ID + TEXT_TYPE + COMMA_SEP +
                    MainPersistenceContract.ArtistEntity.ARTIST_NAME + TEXT_TYPE + COMMA_SEP +
                    MainPersistenceContract.ArtistEntity.ARTIST_COVER + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_SONG =
            "CREATE TABLE IF NOT EXISTS " + MainPersistenceContract.SongEntity.TABLE_NAME + " (" +
                    MainPersistenceContract.SongEntity._ID + TEXT_TYPE + " PRIMARY KEY, " +
                    MainPersistenceContract.SongEntity.SONG_ID + TEXT_TYPE + COMMA_SEP +
                    MainPersistenceContract.SongEntity.SONG_NAME + TEXT_TYPE + COMMA_SEP +
                    MainPersistenceContract.SongEntity.SONG_COVER + TEXT_TYPE +
                    " )";

    public MainDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ARTIST);
        db.execSQL(SQL_CREATE_SONG);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
