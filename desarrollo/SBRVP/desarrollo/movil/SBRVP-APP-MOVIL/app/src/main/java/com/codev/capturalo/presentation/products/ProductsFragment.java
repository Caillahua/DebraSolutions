package com.codev.capturalo.presentation.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.core.ScrollChildSwipeRefreshLayout;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;
import com.codev.capturalo.presentation.main.MainContract;
import com.codev.capturalo.presentation.main.MainPresenter;
import com.codev.capturalo.presentation.main.RecommendedMusicAdapter;
import com.codev.capturalo.presentation.store.StoreActivity;
import com.codev.capturalo.utils.ProgressDialogCustom;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsFragment extends BaseFragment implements MenuItem.OnActionExpandListener, MainContract.View,
        SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.vw_limiter)
    View vwLimiter;
    @BindView(R.id.noSongs)
    LinearLayout noSongs;
    @BindView(R.id.firstText)
    LinearLayout firsText;
    @BindView(R.id.rv_song_explorer)
    RecyclerView rvSongExplorer;
    @BindView(R.id.ll_rv_container)
    LinearLayout llRvContainer;
    @BindView(R.id.refresh_song_layout)
    ScrollChildSwipeRefreshLayout refreshSongLayout;


    private MainContract.Presenter mPresenter;

    private ProgressDialogCustom mProgressDialogCustom;

    private SessionManager mSessionManager;

    private RecommendedMusicAdapter mRecommendedMusicAdapter;

    private LinearLayoutManager mRecommendedMusicsLinearLayoutManager;

    public ProductsFragment() {
        // Requires empty public constructor
    }

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getContext());
        mPresenter = new MainPresenter(Injection.provideMainRepository(getActivity()), this, getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSessionManager.getValueImage() != null){
            mPresenter.loadRecommendedMusic(mSessionManager.getValueImage(), 0);
        }else {
            mPresenter.loadRecommendedMusic("", 1);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal_store, container, false);
        ButterKnife.bind(this, root);

        toolbar.setTitle(R.string.artist_explorer);

        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Obteniendo productos...");

        mRecommendedMusicAdapter = new RecommendedMusicAdapter(getContext(), new ArrayList<ArticuloEntity>(), getActivity());
        mRecommendedMusicsLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecommendedMusicsLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvSongExplorer.setAdapter(mRecommendedMusicAdapter);
        rvSongExplorer.setLayoutManager(mRecommendedMusicsLinearLayoutManager);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Ingresando...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firsText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public void showRecommendedAlbums(ArrayList<ArticuloEntity> genreEntities) {

    }

    @Override
    public void showStore(TiendaEntity tiendaEntity) {
        Intent intent = new Intent(getActivity(), StoreActivity.class);
        intent.putExtra("tiend", tiendaEntity);
        startActivity(intent);
    }

    @Override
    public void showRecommendedMusic(ArrayList<ArticuloEntity> genreEntities) {
        firsText.setVisibility(View.GONE);
        if (rvSongExplorer != null && mRecommendedMusicAdapter != null){
            if(genreEntities.size() != 0){
                noSongs.setVisibility(View.GONE);
                llRvContainer.setVisibility(View.VISIBLE);
                rvSongExplorer.setVisibility(View.VISIBLE);
                mRecommendedMusicAdapter.setGenreEntities(genreEntities);
               // rvSongExplorer.setAdapter(mRecommendedMusicAdapter);
            }else{
                noSongs.setVisibility(View.VISIBLE);
                llRvContainer.setVisibility(View.GONE);
                rvSongExplorer.setVisibility(View.GONE);

            }

        }
    }

    @Override
    public void clickItem(ArticuloEntity articuloEntity) {
        mPresenter.getStoreById(articuloEntity.getIdTienda());
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
