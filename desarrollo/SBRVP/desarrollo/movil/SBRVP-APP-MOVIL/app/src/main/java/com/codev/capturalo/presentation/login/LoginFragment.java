package com.codev.capturalo.presentation.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.core.BaseFragment;
import com.codev.capturalo.data.model.UserEntity;
import com.codev.capturalo.presentation.main.PrincipalActivity;
import com.codev.capturalo.presentation.register.RegisterActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginFragment extends BaseFragment implements LoginContract.View, Validator.ValidationListener {

    @NotEmpty(message = "Este campo no puede ser vacío", sequence = 1)
    @BindView(R.id.et_email)
    EditText etEmail;

    @NotEmpty(message = "Este campo no puede ser vacío", sequence = 3)
    @BindView(R.id.et_password)
    EditText etPassword;

    private Validator validator;
    private LoginContract.Presenter mPresenter;
   // private CallbackManager callbackManager;
    //private DialogForgotPassword dialogForgotPassword;

    private ProgressDialog progressDialog;


    public LoginFragment() {
        // Requires empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LoginPresenter(this, getActivity());

       /* FacebookSdk.sdkInitialize(getContext().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String access_token_facebook = loginResult.getAccessToken().getToken();
                if (access_token_facebook != null || !access_token_facebook.equals("")) {
                    mPresenter.loginFacebook(access_token_facebook);
                    AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                } else {
                    Toast.makeText(getContext(), "Algo sucedió mal al intentar loguearse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "El login a facebook se ha cancelado, intentelo más tarde", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Error al intentar loguearse", Toast.LENGTH_SHORT).show();
            }
        });*/
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
        View root = inflater.inflate(R.layout.fragment_user_login, container, false);
        ButterKnife.bind(this, root);

      //  dialogForgotPassword = new DialogForgotPassword(getContext(), this);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Ingresando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
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
    public void successLogin(UserEntity userEntity) {
        newActivityClearPreview(getActivity(), null, PrincipalActivity.class);
    }

    @Override
    public void errorLogin(String msg) {
    }

    @Override
    public void showDialogForgotPassword() {
        //dialogForgotPassword.show();
    }

    @Override
    public void showSendEmail(String email) {
        mPresenter.sendEmail(email);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_facebook_login, R.id.tv_forgot_password, R.id.btn_normal_login, R.id.ll_create_user_account, R.id.rl_back_to_welcome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook_login:
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.tv_forgot_password:
                showDialogForgotPassword();
                break;
            case R.id.btn_normal_login:
                validator.validate();
                break;
            case R.id.ll_create_user_account:
                nextActivity(getActivity(), null, RegisterActivity.class, false);
                break;
            case R.id.rl_back_to_welcome:
              //  nextActivity(getActivity(), null, WelcomeActivity.class, true);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
           // callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onValidationSucceeded() {
        mPresenter.loginUser(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages ;)
            if (view instanceof EditText)
                ((EditText) view).setError(message);
        }
    }

}
