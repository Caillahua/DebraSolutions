package com.codev.capturalo.presentation.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.presentation.main.tabs.PrincipalHomeFragment;
import com.codev.capturalo.presentation.main.tabs.PrincipalProfileFragment;
import com.codev.capturalo.presentation.main.tabs.PrincipalSeekerFragment;
import com.codev.capturalo.presentation.search.SearchActivity;
import com.codev.capturalo.utils.ActivityUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by junior on 01/08/17.
 */

public class PrincipalActivity extends BaseActivity implements OnTabSelectListener {


    private static final int MENU_HOME = 1;
    private static final int MENU_SEARCH = 2;
    private static final int MENU_PROFILE = 3;

    @BindView(R.id.contentContainer_1)
    FrameLayout contentContainer1;
    @BindView(R.id.contentContainer_2)
    FrameLayout contentContainer2;
    @BindView(R.id.contentContainer_3)
    FrameLayout contentContainer3;
    @BindView(R.id.contentContainer_4)
    FrameLayout contentContainer4;
    @BindView(R.id.contentContainer_5)
    FrameLayout contentContainer5;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.bind(this);
        bottomBar.setOnTabSelectListener(this);


        PrincipalHomeFragment fragment = (PrincipalHomeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentContainer_1);

        if (fragment == null) {
            fragment = PrincipalHomeFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.contentContainer_1);
        }

        PrincipalSeekerFragment principalSeekerFragment = (PrincipalSeekerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentContainer_2);

        if (principalSeekerFragment == null) {
            principalSeekerFragment = PrincipalSeekerFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    principalSeekerFragment, R.id.contentContainer_2);
        }

        PrincipalProfileFragment eventsFragment = (PrincipalProfileFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentContainer_3);

        if (eventsFragment == null) {
            eventsFragment = PrincipalProfileFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    eventsFragment, R.id.contentContainer_3);
        }



        selectedFragment(MENU_HOME);
    }


    private void selectedFragment(int option) {

        contentContainer1.setVisibility(option == MENU_HOME ? View.VISIBLE: View.GONE);
        contentContainer2.setVisibility(option == MENU_SEARCH  ? View.VISIBLE: View.GONE);
        contentContainer3.setVisibility(option == MENU_PROFILE  ? View.VISIBLE: View.GONE);

    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                selectedFragment(MENU_HOME);
                break;
            case R.id.tab_search:
                nextActivity(this, null, SearchActivity.class, false);
                selectedFragment(MENU_HOME);
                break;
            case R.id.tab_profile:
                selectedFragment(MENU_PROFILE);
                break;
            default:
                break;
        }
    }
}
