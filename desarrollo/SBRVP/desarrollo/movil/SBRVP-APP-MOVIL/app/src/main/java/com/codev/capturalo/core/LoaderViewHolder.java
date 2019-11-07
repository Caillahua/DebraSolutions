package com.codev.capturalo.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.codev.capturalo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
