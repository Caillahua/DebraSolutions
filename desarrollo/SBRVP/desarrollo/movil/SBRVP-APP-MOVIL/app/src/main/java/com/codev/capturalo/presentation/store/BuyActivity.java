package com.codev.capturalo.presentation.store;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.widget.RelativeLayout;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyActivity extends BaseActivity {

    @BindView(R.id.body)
    RelativeLayout body;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        ButterKnife.bind(this);

        //tienda = (TiendaEntity) getIntent().getExtras().getSerializable("tienda");
        // toolbar.setTitle(tienda.getRsocial());

        BuyFragment fragment = (BuyFragment) getSupportFragmentManager()
                .findFragmentById(R.id.body);

        if (fragment == null) {
            fragment = BuyFragment.newInstance(getIntent().getExtras());

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.body);
        }

        // Create the presenter
        //  new LoginPresenter(fragment,this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
