package com.codev.capturalo.presentation.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.utils.ProgressDialogCustom;

import butterknife.ButterKnife;

public class UserFavoriteFragment extends BaseFragment implements  ExampleContract.View{



    private ExampleContract.Presenter mPresenter;

    private ProgressDialogCustom mProgressDialogCustom;



    public UserFavoriteFragment() {
        // Requires empty public constructor
    }

    public static UserFavoriteFragment newInstance() {
        return new UserFavoriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_favorite, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Ingresando...");


    }


    @Override
    public void setPresenter(ExampleContract.Presenter presenter) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMessage(String msg) {
        ((BaseActivity) getActivity()).showMessage(msg);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivity) getActivity()).showMessageError(message);
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

}
