package com.learn2crack.androidmvp.ui.fragment;

import android.app.DialogFragment;
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
import android.widget.TextView;

import com.learn2crack.androidmvp.R;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.ui.activity.ProfileActivity;
import com.learn2crack.androidmvp.ui.contract.ChangePasswordContract;
import com.learn2crack.androidmvp.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.learn2crack.androidmvp.util.Validation.validateFields;

public class ChangePasswordFragment extends DialogFragment implements ChangePasswordContract.View {

    public interface Listener {
        void onDialogDisplayed();
        void onDialogDismissed();
    }

    public static final String TAG = ChangePasswordFragment.class.getSimpleName();

    private ChangePasswordContract.Presenter mPresenter;

    private Listener mListener;

    @BindView(R.id.ti_old_password) TextInputLayout mTiOldPassword;
    @BindView(R.id.ti_new_password) TextInputLayout mTiNewPassword;
    @BindView(R.id.et_old_password)   EditText mEtOldPassword;
    @BindView(R.id.et_new_password) EditText mEtNewPassword;
    @BindView(R.id.progress)    ProgressBar mProgressBar;
    @BindView(R.id.tv_message)  TextView mTvMessage;

    private SharedPreferences mSharedPreferences;

    private boolean mIsLoading = false;

    private static final String IS_LOADING = "is_loading";

    public static ChangePasswordFragment newInstance() {

        return new ChangePasswordFragment();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ProfileActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener.onDialogDismissed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_LOADING, mIsLoading);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_change_password, container, false);
        ButterKnife.bind(this, view);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState != null) {

            mIsLoading = savedInstanceState.getBoolean(IS_LOADING);

        }

        return view;
    }

    @Override
    public void setPresenter(@NonNull ChangePasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.btn_change_password)
    void onChangePasswordClick() {

        mTvMessage.setVisibility(View.GONE);

        String oldPassword = mEtOldPassword.getText().toString();
        String newPassword = mEtNewPassword.getText().toString();

        int err = 0;

        if (!validateFields(oldPassword)) {

            err++;
            mTiOldPassword.setError(getString(R.string.error_empty_password));
        }

        if (!validateFields(newPassword)) {

            err++;
            mTiNewPassword.setError(getString(R.string.error_empty_password));
        }

        if (err == 0) {

            mPresenter.changePassword(mSharedPreferences.getString(Constants.TOKEN, ""), mSharedPreferences.getString(Constants.EMAIL, ""), oldPassword, newPassword);
            mIsLoading = true;
        }
    }

    @OnClick(R.id.btn_cancel)
    void onCancelClick(){
        dismiss();
    }

    @Override
    public void displayMessage(UserResponse response) {

        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(response.getMessage());
    }

    @Override
    public void showError(String message) {

        mIsLoading = false;
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(message);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
