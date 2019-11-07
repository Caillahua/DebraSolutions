package com.codev.capturalo.presentation.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codev.capturalo.R;
import com.codev.capturalo.data.model.ArticuloEntity;
import com.codev.capturalo.data.model.TiendaEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogCompra extends AlertDialog {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_aceptar)
    TextView tvAceptar;
    @BindView(R.id.cantidad)
    EditText cantidad;
    private String tlf;
    private ArticuloEntity articuloEntity;
    private TiendaEntity tiendaEntity;
    private Activity mActivity;
    private boolean isFirst = true;

    public DialogCompra(Context context, Bundle bundle, Activity activity) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_confirm_compra, null);
        setView(view);
        ButterKnife.bind(this, view);
        this.mActivity = activity;
        tlf = bundle.getString("tlf");
        articuloEntity = (ArticuloEntity) bundle.getSerializable("articuloEntity");
        tiendaEntity = (TiendaEntity) bundle.getSerializable("tiendaEntity");
    }

    @OnClick({R.id.tv_cancel, R.id.tv_aceptar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_aceptar:
                if (isFirst) {
                    isFirst();
                } else {
                    if(cantidad.getText().toString().length() != 0){
                        aceptar();
                    }else{
                        Toast.makeText(mActivity, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    public void isFirst() {
        tvAceptar.setText("COMPRAR");
        cantidad.setVisibility(View.VISIBLE);
        question.setText("¿Cuantos deseas adquirir?");
        isFirst = false;
    }

    public void aceptar() {
        Intent intent = new Intent(mActivity, BuyActivity.class);
        Log.e("ArticuloEntity Dialog", articuloEntity.getDescripcion());
        Log.e("TiendaEntity Dialod", tiendaEntity.getRsocial());
        intent.putExtra("articuloEntity", articuloEntity);
        intent.putExtra("tiendaEntity", tiendaEntity);
        intent.putExtra("cantidad", cantidad.getText().toString());
        mActivity.startActivity(intent);
        dismiss();
        mActivity.finish();
    }
}