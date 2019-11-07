package com.codev.capturalo.presentation.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codev.capturalo.R;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.utils.GlideUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedNewAlbumAdapter extends RecyclerView.Adapter<RecommendedNewAlbumAdapter.ViewHolder> {



    private Context mContext;
    private ArrayList<ArticuloEntity> mGenreEntities;
    private int count = 0;


    public RecommendedNewAlbumAdapter(Context context, ArrayList<ArticuloEntity> arrayList) {
        this.mContext = context;
        this.mGenreEntities = arrayList;
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
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_article, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ArticuloEntity genreEntity = mGenreEntities.get(position);

        holder.price.setText(String.valueOf("S/ "+genreEntity.getPrecio()));
        holder.nameArticle.setText(String.valueOf(genreEntity.getDescripcion()));
        holder.stock.setText(String.valueOf(genreEntity.getStock()) + " unidades");
        GlideUtils.loadImageCornerTransform(holder.ivAlbumCoverPicture, genreEntity.getRutaImagen(), mContext, 30);

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
        @BindView(R.id.iv_album_cover_picture)
        ImageView ivAlbumCoverPicture;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.stock)
        TextView stock;
        @BindView(R.id.button_vermas)
        LinearLayout buttonVermas;
        @BindView(R.id.album_item_view)
        RelativeLayout albumItemView;
        @BindView(R.id.nameArticle)
        TextView nameArticle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
