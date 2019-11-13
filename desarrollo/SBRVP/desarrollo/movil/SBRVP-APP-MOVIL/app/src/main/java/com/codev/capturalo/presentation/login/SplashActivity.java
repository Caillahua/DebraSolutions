package com.codev.capturalo.presentation.login;

import android.os.Bundle;
import android.os.Handler;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.presentation.main.PrincipalActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(getBaseContext());
            UserEntity userEntity = sessionManager.getUserEntity();

            if (sessionManager.isLogin()) {
                nextActivity(SplashActivity.this, null, PrincipalActivity.class, true);
            } else
                nextActivity(SplashActivity.this, null, LoginActivity.class, true);

            SplashActivity.this.finish();
        }, SPLASH_DISPLAY_LENGTH);
    }

}
