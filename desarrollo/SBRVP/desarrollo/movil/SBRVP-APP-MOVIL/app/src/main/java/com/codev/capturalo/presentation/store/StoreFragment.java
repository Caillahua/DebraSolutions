package com.codev.capturalo.presentation.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class StoreFragment extends BaseFragment implements MenuItem.OnActionExpandListener, StoreContract.View,
        SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.im_product)
    ImageView imProduct;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_caracteristicas)
    TextView tvCaracteristicas;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_direccion)
    TextView tvDireccion;
    @BindView(R.id.tv_como_llegar)
    TextView tvComoLlegar;
    @BindView(R.id.tv_cell)
    TextView tvCell;
    @BindView(R.id.btn_buy)
    Button btnBuy;

    private StoreContract.Presenter mPresenter;

    private ProgressDialogCustom mProgressDialogCustom;

    private SessionManager mSessionManager;

    private RecommendedMusicAdapter mRecommendedMusicAdapter;

    private LinearLayoutManager mRecommendedMusicsLinearLayoutManager;

    private ArticuloEntity articuloEntity;
    private TiendaEntity tiendaEntity;

    public StoreFragment() {
        // Requires empty public constructor
    }

    public static StoreFragment newInstance(Bundle bundle) {
        StoreFragment storeFragment = new StoreFragment();
        storeFragment.setArguments(bundle);
        return storeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getContext());
        mPresenter = new StorePresenter(Injection.provideMainRepository(getActivity()), this, getContext());
        articuloEntity = (ArticuloEntity) getArguments().getSerializable("articuloEntity");
        mPresenter.getStoreById(articuloEntity.getIdTienda());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal_store_new, container, false);
        ButterKnife.bind(this, root);

        toolbar.setTitle(R.string.artist_explorer);

        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Obteniendo tienda...");
        tvProductName.setText(articuloEntity.getDescripcion());
        tvPrice.setText("S/ " + String.valueOf(articuloEntity.getPrecio()));
        tvStock.setText(String.valueOf(articuloEntity.getStock()) + " unidades");
        tvCaracteristicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("msg", articuloEntity.getCaracteristicas());
                DialogTerminos dialogTerminos = new DialogTerminos(getContext(), bundle);
                dialogTerminos.show();
            }
        });
        GlideUtils.loadImage(imProduct,articuloEntity.getRutaImagen(), getContext());
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Log.e("ArticuloEntity", articuloEntity.getDescripcion());
                Log.e("TiendaEntity", tiendaEntity.getRsocial());
                bundle.putSerializable("articuloEntity", articuloEntity);
                bundle.putSerializable("tiendaEntity", tiendaEntity);
                DialogCompra dialogCompra = new DialogCompra(getContext(), bundle, getActivity());
                dialogCompra.show();

              /*  Intent intent = new Intent(getActivity(), BuyActivity.class);
                intent.putExtra("articuloEntity", articuloEntity);
                intent.putExtra("tiendaEntity", tiendaEntity);
                startActivity(intent);*/

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
        this.tiendaEntity = tiendaEntity;
        tvStoreName.setText(tiendaEntity.getRsocial());
        tvDireccion.setText(tiendaEntity.getDireccion());
    }

    @Override
    public void clickItem(ArticuloEntity articuloEntity) {
        Toast.makeText(getContext(), articuloEntity.getDescripcion(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void buySuccess() {

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
