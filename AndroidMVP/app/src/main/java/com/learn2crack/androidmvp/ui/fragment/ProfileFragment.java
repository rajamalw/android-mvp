package com.learn2crack.androidmvp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learn2crack.androidmvp.R;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.ui.activity.ProfileActivity;
import com.learn2crack.androidmvp.ui.contract.ProfileContract;
import com.learn2crack.androidmvp.util.Constants;
import com.learn2crack.androidmvp.util.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    public interface Listener {

        void onChangePasswordClicked();

        void onLogoutClicked();
    }

    public static final String TAG = ProfileFragment.class.getSimpleName();

    private ProfileContract.Presenter mPresenter;

    private Listener mListener;

    @BindView(R.id.tv_name)  TextView mTvName;
    @BindView(R.id.tv_email)  TextView mTvEmail;
    @BindView(R.id.tv_date)  TextView mTvDate;
    @BindView(R.id.progress) ProgressBar mProgressBar;

    private SharedPreferences mSharedPreferences;

    private boolean mIsLoading = false;
    private boolean mIsDataLoaded = false;

    private static final String IS_LOADING = "is_loading";
    private static final String IS_DATA_LOADED = "is_data_loaded";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String DATE = "date";


    public static ProfileFragment newInstance() {

        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ProfileActivity)context;
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
        outState.putBoolean(IS_DATA_LOADED, mIsDataLoaded);
        outState.putString(NAME, mTvName.getText().toString());
        outState.putString(EMAIL, mTvEmail.getText().toString());
        outState.putString(DATE, mTvDate.getText().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState != null) {

            mIsLoading = savedInstanceState.getBoolean(IS_LOADING);
            mIsDataLoaded = savedInstanceState.getBoolean(IS_DATA_LOADED);

            mTvName.setText(savedInstanceState.getString(NAME));
            mTvEmail.setText(savedInstanceState.getString(EMAIL));
            mTvDate.setText(savedInstanceState.getString(DATE));

        }

        return view;
    }

    @Override
    public void setPresenter(@NonNull ProfileContract.Presenter presenter) {

        mPresenter = presenter;
    }

    @OnClick(R.id.btn_change_password)
    void onChangePasswordClick(){

        mListener.onChangePasswordClicked();
    }

    @OnClick(R.id.btn_logout)
    void onLogoutClick() {

        mListener.onLogoutClicked();
    }

    @Override
    public void displayProfile(UserResponse response) {

        mIsLoading = false;
        mIsDataLoaded = true;

        mTvName.setText(response.getName());
        mTvEmail.setText(response.getEmail());
        mTvDate.setText(response.getCreated_at());
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
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public String getToken() {
        return mSharedPreferences.getString(Constants.TOKEN, "");
    }

    @Override
    public String getEmail() {
        return mSharedPreferences.getString(Constants.EMAIL, "");
    }

    @Override
    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }

    @Override
    public boolean isDataLoaded() {
        return mIsDataLoaded;
    }

}
