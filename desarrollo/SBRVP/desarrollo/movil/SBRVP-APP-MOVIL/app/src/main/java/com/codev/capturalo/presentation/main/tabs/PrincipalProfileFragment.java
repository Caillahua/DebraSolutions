package com.codev.capturalo.presentation.main.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.presentation.main.ExampleContract;
import com.codev.capturalo.presentation.main.UserFavoriteFragment;
import com.codev.capturalo.presentation.products.ProductsFragment;
import com.codev.capturalo.utils.ProgressDialogCustom;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrincipalProfileFragment extends BaseFragment implements ExampleContract.View {


    @BindView(R.id.iv_user_picture)
    ImageView ivUserPicture;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SessionManager mSessionManager;

    private ExampleContract.Presenter mPresenter;

    private ProgressDialogCustom mProgressDialogCustom;

    public PrincipalProfileFragment() {
        // Requires empty public constructor
    }

    public static PrincipalProfileFragment newInstance() {
        return new PrincipalProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = new SessionManager(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal_user_profile, container, false);
        FrameLayout frameLayout = root.findViewById(R.id.layout_content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View activityView = layoutInflater.inflate(R.layout.layout_tab, null, false);

        View view = layoutInflater.inflate(R.layout.layout_tab, null);

        frameLayout.addView(view);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.black));
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Ingresando...");

        UserEntity userEntity = mSessionManager.getUserEntity();
        //GlideUtils.loadImageCircleTransform(ivUserPicture, userEntity.get(), getActivity());
        tvUserName.setText(userEntity.fullName());

       // setupViewPager(viewPager);
      //  tlUserProfile.setupWithViewPager(vpUserProfile);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new ProductsFragment(), "Búsquedas");
        adapter.addFragment(new UserFavoriteFragment(), "Comprados");
        //viewPagerAdapter.addFragment(new UserFavoriteFragment(), "Favoritos");

        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
