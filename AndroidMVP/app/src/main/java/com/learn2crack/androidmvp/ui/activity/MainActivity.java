package com.learn2crack.androidmvp.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.learn2crack.androidmvp.R;
import com.learn2crack.androidmvp.data.user.UserRepository;
import com.learn2crack.androidmvp.ui.fragment.LoginFragment;
import com.learn2crack.androidmvp.ui.fragment.RegisterFragment;
import com.learn2crack.androidmvp.ui.presenter.LoginPresenter;
import com.learn2crack.androidmvp.ui.presenter.RegisterPresenter;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener, RegisterFragment.Listener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String ACTIVE_FRAGMENT = "active_fragment";

    private LoginFragment mLoginFragment;

    private LoginPresenter mLoginPresenter;

    private RegisterFragment mRegisterFragment;

    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

            loadLoginFragment();

        } else {

            String activeFragment = savedInstanceState.getString(ACTIVE_FRAGMENT);

            if (activeFragment!= null && activeFragment.equals(RegisterFragment.TAG)) {

                loadRegisterFragment();

            } else {

                loadLoginFragment();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRegisterFragment != null && mRegisterFragment.isVisible()) {

            outState.putString(ACTIVE_FRAGMENT, RegisterFragment.TAG);

        } else {

            outState.putString(ACTIVE_FRAGMENT, LoginFragment.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRegisterClicked() {

        Log.d(TAG, "onRegisterClicked: ");

        loadRegisterFragment();
    }

    @Override
    public void onLaunchProfile() {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginClicked() {

        Log.d(TAG, "onLoginClicked: ");

        loadLoginFragment();
    }

    private void loadLoginFragment() {

        mLoginFragment = (LoginFragment) getFragmentManager().findFragmentByTag(LoginFragment.TAG);

        if (mLoginFragment == null) {

            mLoginFragment = LoginFragment.newInstance();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_fragment, mLoginFragment, LoginFragment.TAG);
        ft.commit();

        mLoginPresenter = new LoginPresenter(UserRepository.getInstance(), mLoginFragment);

        mLoginFragment.setPresenter(mLoginPresenter);
    }

    private void loadRegisterFragment() {

        mRegisterFragment = (RegisterFragment) getFragmentManager().findFragmentByTag(RegisterFragment.TAG);

        if (mRegisterFragment == null) {

            mRegisterFragment = RegisterFragment.newInstance();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_fragment, mRegisterFragment, RegisterFragment.TAG);
        ft.commit();

        mRegisterPresenter = new RegisterPresenter(UserRepository.getInstance(), mRegisterFragment);

        mRegisterFragment.setPresenter(mRegisterPresenter);
    }

}
