package com.codev.capturalo.presentation.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;
import com.codev.capturalo.presentation.main.RecommendedMusicAdapter;
import com.codev.capturalo.utils.GlideUtils;
import com.codev.capturalo.utils.ProgressDialogCustom;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyFragment extends BaseFragment implements MenuItem.OnActionExpandListener, StoreContract.View,
        SearchView.OnQueryTextListener {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.im_producto)
    ImageView imProducto;
    @BindView(R.id.tv_producto)
    TextView tvProducto;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_direccion_store)
    TextView tvDireccionStore;
    @BindView(R.id.tv_cell_store)
    TextView tvCellStore;
    @BindView(R.id.tv_product_caracteristica)
    TextView tvProductCaracteristica;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rv_progress_bar)
    RelativeLayout rvProgressBar;
    @BindView(R.id.container_scroll)
    ScrollView containerScroll;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btnOut)
    Button btnOut;
    private StoreContract.Presenter mPresenter;

    private ProgressDialogCustom mProgressDialogCustom;

    private SessionManager mSessionManager;

    private RecommendedMusicAdapter mRecommendedMusicAdapter;

    private LinearLayoutManager mRecommendedMusicsLinearLayoutManager;

    private ArticuloEntity articuloEntity;
    private TiendaEntity tiendaEntity;
    private String cantidad;

    public BuyFragment() {
        // Requires empty public constructor
    }

    public static BuyFragment newInstance(Bundle bundle) {
        BuyFragment buyFragment = new BuyFragment();
        buyFragment.setArguments(bundle);
        return buyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getContext());
        mPresenter = new StorePresenter(Injection.provideMainRepository(getActivity()), this, getContext());
        articuloEntity = (ArticuloEntity) getArguments().getSerializable("articuloEntity");
        tiendaEntity = (TiendaEntity) getArguments().getSerializable("tiendaEntity");
        cantidad = getArguments().getString("cantidad");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_paid_ticket, container, false);
        ButterKnife.bind(this, root);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Comprando...");


        Log.e("ArticuloEntity Buy", articuloEntity.getDescripcion());
        Log.e("TiendaEntity Buy", tiendaEntity.getRsocial());
        containerScroll.setVisibility(View.GONE);
        rvProgressBar.setVisibility(View.VISIBLE);
        mPresenter.setBuy(Integer.valueOf(mSessionManager.getUserEntity().getIdCliente()),
                tiendaEntity.getIdTienda(),
                articuloEntity.getIdArticulo(), Integer.valueOf(cantidad), 1);
        tvProducto.setText(articuloEntity.getDescripcion());
        double price = articuloEntity.getPrecio() * Integer.valueOf(cantidad);
        tvPrice.setText("S/ " + String.valueOf(price));
        GlideUtils.loadImage(imProducto, articuloEntity.getRutaImagen(), getContext());
        tvDireccionStore.setText(tiendaEntity.getDireccion());
        tvStoreName.setText(tiendaEntity.getRsocial());
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void showStore(TiendaEntity tiendaEntity) {

    }

    @Override
    public void clickItem(ArticuloEntity articuloEntity) {
        Toast.makeText(getContext(), articuloEntity.getDescripcion(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void buySuccess() {
        containerScroll.setVisibility(View.VISIBLE);
        rvProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(StoreContract.Presenter presenter) {
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
