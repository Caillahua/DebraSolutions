package com.codev.capturalo.presentation.products;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codev.capturalo.data.datasource.local.MainLocalDataSource;
import com.codev.capturalo.data.datasource.remote.MainRemoteDataSource;
import com.codev.capturalo.data.repositories.MainRepository;

import static com.google.common.base.Preconditions.checkNotNull;

class Injection {
    public static MainRepository provideMainRepository(@NonNull Context context) {
        checkNotNull(context);
        return MainRepository.getInstance(
                MainRemoteDataSource.getInstance(context),
                MainLocalDataSource.getInstance(context)
        );
    }
}
