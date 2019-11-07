package com.codev.capturalo.presentation.main.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;
import com.codev.capturalo.presentation.main.MainContract;
import com.codev.capturalo.presentation.main.MainPresenter;
import com.codev.capturalo.presentation.main.RecommendedAlbumAdapter;
import com.codev.capturalo.presentation.main.RecommendedNewAlbumAdapter;
import com.codev.capturalo.presentation.products.ProductItem;
import com.codev.capturalo.utils.ProgressDialogCustom;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luizkawai on 26/07/17.
 */
public class PrincipalHomeFragment extends BaseFragment implements MainContract.View {

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.rv_recommended_album)
    RecyclerView rvRecommendedAlbum;
    @BindView(R.id.rv_recommended_music)
    RecyclerView rvRecommendedMusic;
    private MainContract.Presenter mPresenter;
    private SessionManager mSessionManager;
    private ProgressDialogCustom mProgressDialogCustom;

    private RecommendedAlbumAdapter mRecommendedAlbumAdapter;
    private RecommendedNewAlbumAdapter mRecommendedNewAlbumAdapter;

    private LinearLayoutManager mRecommendedAlbumsLinearLayoutManager;
    private LinearLayoutManager mRecommendednewAlbumLinearLayoutManager;

    public PrincipalHomeFragment() {
        // Requires empty public constructor
    }

    public static PrincipalHomeFragment newInstance() {
        return new PrincipalHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPresenter = new MainPresenter(this,getContext());
        mSessionManager = new SessionManager(getContext());
        mPresenter = new MainPresenter(Injection.provideMainRepository(getActivity()), this, getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadRecommendedAlbums(mSessionManager.getValueImage(), 0);
        mPresenter.loadRecommendedNewAlbums("", 5);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal_home, container, false);
        ButterKnife.bind(this, root);
        tvUserName.setText(mSessionManager.getUserEntity().getNombre());
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Ingresando...");

        mRecommendedAlbumAdapter = new RecommendedAlbumAdapter(getContext(), new ArrayList<ArticuloEntity>(), (ProductItem) mPresenter);
        mRecommendedAlbumsLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecommendedAlbumsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecommendedNewAlbumAdapter = new RecommendedNewAlbumAdapter(getContext(), new ArrayList<ArticuloEntity>());
        mRecommendednewAlbumLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecommendednewAlbumLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvRecommendedAlbum.setAdapter(mRecommendedAlbumAdapter);
        rvRecommendedAlbum.setLayoutManager(mRecommendedAlbumsLinearLayoutManager);

        rvRecommendedMusic.setAdapter(mRecommendedNewAlbumAdapter);
        rvRecommendedMusic.setLayoutManager(mRecommendednewAlbumLinearLayoutManager);

    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
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
    public void showRecommendedAlbums(ArrayList<ArticuloEntity> genreEntities) {
        if (rvRecommendedAlbum != null && mRecommendedAlbumAdapter != null){
            mRecommendedAlbumAdapter.setGenreEntities(genreEntities);
            rvRecommendedAlbum.setAdapter(mRecommendedAlbumAdapter);

        }
        //mRecommendedAlbumAdapter.setArtistAlbum(genreEntities);
        //rvRecommendedAlbum.setAdapter(mRecommendedAlbumAdapter);
    }

    @Override
    public void showStore(TiendaEntity tiendaEntity) {

    }

    @Override
    public void showRecommendedMusic(ArrayList<ArticuloEntity> genreEntities) {
        if (rvRecommendedMusic != null && mRecommendedNewAlbumAdapter != null){
            mRecommendedNewAlbumAdapter.setGenreEntities(genreEntities);
            rvRecommendedMusic.setAdapter(mRecommendedNewAlbumAdapter);

        }
        //mRecommendedMusicAdapter.setArtistAlbum(genreEntities);
        //rvRecommendedMusic.setAdapter(mRecommendedMusicAdapter);
    }

    @Override
    public void clickItem(ArticuloEntity articuloEntity) {
        Toast.makeText(getContext(), articuloEntity.getDescripcion(), Toast.LENGTH_SHORT).show();
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
