package com.codev.capturalo.presentation.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codev.capturalo.R;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.presentation.store.StoreActivity;
import com.codev.capturalo.utils.GlideUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedMusicAdapter extends RecyclerView.Adapter<RecommendedMusicAdapter.ViewHolder> {



    private Context mContext;
    private ArrayList<ArticuloEntity> mGenreEntities;
    private int count = 0;
    private Activity mActivity;

    public RecommendedMusicAdapter(Context context, ArrayList<ArticuloEntity> arrayList, Activity activity) {
        this.mContext = context;
        this.mGenreEntities = arrayList;
        this.mActivity = activity;
    }

    public ArrayList<ArticuloEntity> getGenreEntities() {
        return mGenreEntities;
    }

    public void setGenreEntities(ArrayList<ArticuloEntity> mGenreEntities) {
        this.mGenreEntities = mGenreEntities;
        this.count = mGenreEntities.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_album, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ArticuloEntity genreEntity = mGenreEntities.get(position);

        holder.tvArticleName.setText(String.valueOf(genreEntity.getDescripcion()));
        holder.tvStockArticle.setText(String.valueOf(genreEntity.getStock()) + " unidades");
        holder.tvPrice.setText("S/ " + String.valueOf(genreEntity.getPrecio()));
        GlideUtils.loadImageCornerTransform(holder.ivAlbumCover, genreEntity.getRutaImagen(), mContext, 30);

        holder.containerProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity , StoreActivity.class);
                intent.putExtra("articuloEntity", genreEntity);
                mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mGenreEntities.size();
    }

    private ArrayList<ArticuloEntity> getItems() {
        return mGenreEntities;
    }

    public int geItemsSelected() {
        return count;
    }

    private void setItems(ArrayList<ArticuloEntity> genreEntities) {

        this.mGenreEntities = genreEntities;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_album_cover)
        ImageView ivAlbumCover;
        @BindView(R.id.tv_article_name)
        TextView tvArticleName;
        @BindView(R.id.tv_stock_article)
        TextView tvStockArticle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.item_view)
        LinearLayout itemView;
        @BindView(R.id.container_product)
        LinearLayout containerProduct;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
