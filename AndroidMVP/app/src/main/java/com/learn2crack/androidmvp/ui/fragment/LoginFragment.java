package com.learn2crack.androidmvp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.learn2crack.androidmvp.ui.contract.LoginContract;
import com.learn2crack.androidmvp.util.Constants;
import com.learn2crack.androidmvp.util.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.learn2crack.androidmvp.util.Validation.validateEmail;
import static com.learn2crack.androidmvp.util.Validation.validateFields;

public class LoginFragment extends Fragment implements LoginContract.View {

    public interface Listener {

        void onRegisterClicked();

        void onLaunchProfile();
    }

    public static final String TAG = LoginFragment.class.getSimpleName();

    private LoginContract.Presenter mPresenter;

    private Listener mListener;

    @BindView(R.id.ti_email)    TextInputLayout mTiEmail;
    @BindView(R.id.ti_password) TextInputLayout mTiPassword;
    @BindView(R.id.et_email)    EditText mEtEmail;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.progress)    ProgressBar mProgressBar;

    private SharedPreferences mSharedPreferences;
    
    private boolean mIsLoading = false;

    private static final String IS_LOADING = "is_loading";

    public static LoginFragment newInstance() {

        return new LoginFragment();
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

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState != null) {

            mIsLoading = savedInstanceState.getBoolean(IS_LOADING);
        }

        return view;
    }

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {

        mPresenter = presenter;
    }

    @OnClick(R.id.tv_register)
    void onRegisterClick() {

        mListener.onRegisterClicked();
    }
    
    @OnClick(R.id.bt_login)
    void onLoginClick() {

        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            mPresenter.login(email, password);
            mIsLoading = true;
        }
    }

    @Override
    public void launchProfile(UserResponse response) {

        mIsLoading = false;

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN,response.getToken());
        editor.putString(Constants.EMAIL,response.getMessage());
        editor.apply();

        mListener.onLaunchProfile();
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
