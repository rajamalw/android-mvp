package com.learn2crack.androidmvp.ui.presenter;

import android.support.annotation.NonNull;

import com.learn2crack.androidmvp.data.user.UserSource;
import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.network.NetworkService;
import com.learn2crack.androidmvp.ui.contract.ChangePasswordContract;
import com.learn2crack.androidmvp.util.ErrorHandler;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    public static final String TAG = ChangePasswordPresenter.class.getSimpleName();

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @NonNull
    private UserSource mUserSource;

    @NonNull
    private ChangePasswordContract.View mView;

    public ChangePasswordPresenter(@NonNull UserSource userSource, @NonNull ChangePasswordContract.View changePasswordView) {

        mUserSource = userSource;
        mView = changePasswordView;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe(boolean isLoading) {

        if (isLoading) {
            mView.showProgress();
            doRxRequest();
        }
    }

    @Override
    public void unsubscribe() {

        if (!mCompositeDisposable.isDisposed()) {

            mCompositeDisposable.clear();
        }
    }

    @Override
    public void changePassword(String token, String email, String oldPassword, String newPassword) {

        mView.showProgress();
        doRxRequest(token, email, oldPassword, newPassword);
    }

    private void doRxRequest(String... args) {


        if (args.length > 0) {

            UserRequest request = new UserRequest();
            request.setEmail(args[1]);
            request.setPassword(args[2]);
            request.setNewPassword(args[3]);

            Observable<UserResponse> observable = mUserSource.changePassword(args[0],request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            NetworkService.putObservable(observable.cache(), UserResponse.class);
        }

        Observable<UserResponse> observableCache = (Observable<UserResponse>) NetworkService.getObservable(UserResponse.class);

        mCompositeDisposable.add(observableCache
                .subscribe(loginResponse -> {
                            mView.hideProgress();
                            mView.displayMessage(loginResponse);
                        },
                        throwable -> {
                            mView.hideProgress();
                            mView.showError(ErrorHandler.handleHTTPError(throwable));
                        })

        );
    }
}
