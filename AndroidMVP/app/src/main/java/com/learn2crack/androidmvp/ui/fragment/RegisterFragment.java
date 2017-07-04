package com.learn2crack.androidmvp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.learn2crack.androidmvp.R;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.ui.activity.MainActivity;
import com.learn2crack.androidmvp.ui.contract.RegisterContract;
import com.learn2crack.androidmvp.util.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.learn2crack.androidmvp.util.Validation.validateEmail;
import static com.learn2crack.androidmvp.util.Validation.validateFields;

public class RegisterFragment extends Fragment implements RegisterContract.View {

    public interface Listener {

        void onLoginClicked();
    }

    public static final String TAG = RegisterFragment.class.getSimpleName();

    private RegisterContract.Presenter mPresenter;

    private Listener mListener;

    @BindView(R.id.ti_name)     TextInputLayout mTiName;
    @BindView(R.id.ti_email)    TextInputLayout mTiEmail;
    @BindView(R.id.ti_password) TextInputLayout mTiPassword;
    @BindView(R.id.et_name)     EditText mEtName;
    @BindView(R.id.et_email)    EditText mEtEmail;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.progress)    ProgressBar mProgressBar;

    private boolean mIsLoading = false;

    private static final String IS_LOADING = "is_loading";

    public static RegisterFragment newInstance() {

        return new RegisterFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity)context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(mIsLoading);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_LOADING, mIsLoading);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {

            mIsLoading = savedInstanceState.getBoolean(IS_LOADING);
        }

        return view;
    }

    @Override
    public void setPresenter(@NonNull RegisterContract.Presenter presenter) {

        mPresenter = presenter;
    }

    @OnClick(R.id.tv_login)
    void onLoginClick() {

        mListener.onLoginClicked();
    }

    @OnClick(R.id.btn_register)
    void onRegisterClick() {

        String name = mEtName.getText().toString();
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateFields(name)) {

            err++;
            mTiName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            mPresenter.register(name, email, password);
            mIsLoading = true;
        }
    }

    @Override
    public void showSuccess(UserResponse response) {

        mIsLoading = false;
        Message.showMessage(getView(), response.getMessage());
    }

    @Override
    public void showError(String message) {

        mIsLoading = false;
        Message.showMessage(getView(), message);
    }

    @Override
    public void showProgress() {

        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
