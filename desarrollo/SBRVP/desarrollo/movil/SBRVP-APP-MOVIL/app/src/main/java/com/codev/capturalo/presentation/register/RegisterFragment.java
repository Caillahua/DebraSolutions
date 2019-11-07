package com.codev.capturalo.presentation.register;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.presentation.login.LoginActivity;
import com.codev.capturalo.presentation.main.PrincipalActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterFragment extends BaseFragment implements RegisterContract.View, Validator.ValidationListener {

    @Length(max = 30, message = "Nombre demasiado largo", sequence = 1)
    @NotEmpty(message = "Este campo no puede estar vacío", sequence = 2)
    @BindView(R.id.et_first_name)
    EditText etFirstName;

    @Length(max = 30, message = "Apellido demasiado largo", sequence = 3)
    @NotEmpty(message = "Este campo no puede estar vacío", sequence = 4)
    @BindView(R.id.et_last_name)
    EditText etLastName;

    @NotEmpty(message = "Este campo no puede estar vacío", sequence = 6)
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;

    @NotEmpty(message = "Este campo no puede estar vacío", sequence = 7)
    @Length(max = 50, message = "")
    @Email(message = "Email inválido")
    @BindView(R.id.et_email)
    EditText etEmail;

    @NotEmpty(message = "Este campo no puede estar vacío", sequence = 8)
    @Length(min = 6, max = 30, message = "La contraseña debe ser de al menos 6 dígitos", sequence = 9)
    @BindView(R.id.et_password)
    EditText etPassword;

    //@BindView(R.id.my_phone_input)
    //IntlPhoneInput etPhone;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.ll_return_to_login)
    LinearLayout llReturnToLogin;

    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;

    private RegisterContract.Presenter mPresenter;
    private Validator validator;
    private boolean isLoading = false;
    private ProgressDialog progressDialog;

    public RegisterFragment() {
        // Requires empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_register, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (progressDialog != null) {
            if (active) {
                progressDialog.show();
            } else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }
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
    public void ShowRegisterSuccessful(UserEntity userEntity) {
        newActivityClearPreview(getActivity(), null, PrincipalActivity.class);
        isLoading = false;
    }

    @Override
    public void ShowErrorRegister(String msg) {
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(msg);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_register, R.id.ll_return_to_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (!isLoading)
                    validator.validate();
                break;
            case R.id.ll_return_to_login:
                nextActivity(getActivity(), null, LoginActivity.class, true);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
       // UserEntity userEntity = new UserEntity();
        //userEntity.set(etEmail.getText().toString());
        //userEntity.setPassword(etPassword.getText().toString());
       // userEntity.setNombre(etFirstName.getText().toString());
       // userEntity.setApellido(etLastName.getText().toString());

       // if (etPhone.isValid()){
            //String phone_number = etPhoneNumber.getText().toString();
            //userEntity.set(phone_number);
            //mPresenter.RegisterUser(userEntity);
        /*} else {
            showErrorMessage("Número de teléfono inválido");
        }*/
        mPresenter.RegisterUser(etPhoneNumber.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString(),
                etFirstName.getText().toString(), etLastName.getText().toString());

        isLoading = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                tvText.setText(message);
            }
        }
    }

    @OnClick(R.id.close_view)
    public void onViewClicked() {
        nextActivity(getActivity(), null, LoginActivity.class, true);
    }
}
