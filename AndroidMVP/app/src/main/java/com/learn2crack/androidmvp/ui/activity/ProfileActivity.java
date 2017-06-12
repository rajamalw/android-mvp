package com.learn2crack.androidmvp.ui.activity;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.learn2crack.androidmvp.R;
import com.learn2crack.androidmvp.data.user.UserRepository;
import com.learn2crack.androidmvp.ui.fragment.ChangePasswordFragment;
import com.learn2crack.androidmvp.ui.fragment.ProfileFragment;
import com.learn2crack.androidmvp.ui.presenter.ChangePasswordPresenter;
import com.learn2crack.androidmvp.ui.presenter.ProfilePresenter;
import com.learn2crack.androidmvp.util.Constants;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.Listener, ChangePasswordFragment.Listener {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    private ProfileFragment mProfileFragment;

    private ProfilePresenter mProfilePresenter;

    private ChangePasswordFragment mChangePasswordFragment;

    private ChangePasswordPresenter mChangePasswordPresenter;

    private boolean isChangePasswordDialogDisplayed = false;

    private SharedPreferences mSharedPreferences;

    public static final String IS_ACTIVE = "is_active";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSharedPreferences();

        loadProfileFragment();

        if(savedInstanceState !=  null) {

            if (savedInstanceState.getBoolean(IS_ACTIVE)) {

                setDialogFragmentPresenter();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_ACTIVE, isChangePasswordDialogDisplayed);
    }

    @Override
    public void onChangePasswordClicked() {

        mChangePasswordFragment = (ChangePasswordFragment) getFragmentManager().findFragmentByTag(ChangePasswordFragment.TAG);

        if (mChangePasswordFragment == null) {

            mChangePasswordFragment = ChangePasswordFragment.newInstance();
        }

        mChangePasswordFragment.show(getFragmentManager(), ChangePasswordFragment.TAG);
        mChangePasswordPresenter = new ChangePasswordPresenter(UserRepository.getInstance(), mChangePasswordFragment);
        mChangePasswordFragment.setPresenter(mChangePasswordPresenter);
    }

    @Override
    public void onLogoutClicked() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        finish();
    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void loadProfileFragment() {

        mProfileFragment = (ProfileFragment) getFragmentManager().findFragmentByTag(ProfileFragment.TAG);

        if (mProfileFragment == null) {

            mProfileFragment = ProfileFragment.newInstance();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_fragment, mProfileFragment, ProfileFragment.TAG);
        ft.commit();

        mProfilePresenter = new ProfilePresenter(UserRepository.getInstance(), mProfileFragment);
        mProfileFragment.setPresenter(mProfilePresenter);

    }

    @Override
    public void onDialogDisplayed() {

        isChangePasswordDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isChangePasswordDialogDisplayed = false;

    }

    private void setDialogFragmentPresenter(){

        mChangePasswordFragment = (ChangePasswordFragment) getFragmentManager().findFragmentByTag(ChangePasswordFragment.TAG);
        mChangePasswordPresenter = new ChangePasswordPresenter(UserRepository.getInstance(), mChangePasswordFragment);
        mChangePasswordFragment.setPresenter(mChangePasswordPresenter);
    }
}
