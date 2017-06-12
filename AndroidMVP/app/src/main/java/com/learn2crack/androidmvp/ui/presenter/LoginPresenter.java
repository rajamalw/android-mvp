package com.learn2crack.androidmvp.ui.presenter;

import android.support.annotation.NonNull;

import com.learn2crack.androidmvp.data.user.UserSource;
import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.network.NetworkService;
import com.learn2crack.androidmvp.ui.contract.LoginContract;
import com.learn2crack.androidmvp.util.ErrorHandler;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {
    

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @NonNull
    private UserSource mUserSource;

    @NonNull
    private LoginContract.View mView;

    public LoginPresenter(@NonNull UserSource userSource, @NonNull LoginContract.View loginView) {

        mUserSource = userSource;
        mView = loginView;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe(boolean isLoading) {

        if (isLoading) {
            mView.showProgress();
            doRxLogin();
        }
    }

    @Override
    public void unsubscribe() {

        if (!mCompositeDisposable.isDisposed()) {

            mCompositeDisposable.clear();
        }
    }

    @Override
    public void login(@NonNull String email, @NonNull String password) {

        mView.showProgress();
        UserRequest request = new UserRequest();
        request.setEmail(email);
        request.setPassword(password);

        doRxLogin(request);
    }

    private void doRxLogin(UserRequest... request) {

        if (request.length > 0) {

            Observable<UserResponse> observable = mUserSource.login(request[0])
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            NetworkService.putObservable(observable.cache(), UserResponse.class);
        }

        Observable<UserResponse> observableCache = (Observable<UserResponse>) NetworkService.getObservable(UserResponse.class);

        mCompositeDisposable.add(observableCache
                        .subscribe(loginResponse -> {
                                    mView.hideProgress();
                                    mView.launchProfile(loginResponse);
                                },
                                throwable -> {
                                    mView.hideProgress();
                                    mView.showError(ErrorHandler.handleHTTPError(throwable));
                                })

        );
    }

}
