package com.learn2crack.androidmvp.ui.presenter;

import android.support.annotation.NonNull;

import com.learn2crack.androidmvp.data.user.UserSource;
import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.network.NetworkService;
import com.learn2crack.androidmvp.ui.contract.RegisterContract;
import com.learn2crack.androidmvp.util.ErrorHandler;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter implements RegisterContract.Presenter {

    public static final String TAG = RegisterPresenter.class.getSimpleName();

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @NonNull
    private UserSource mUserSource;

    @NonNull
    private RegisterContract.View mView;

    public RegisterPresenter(@NonNull UserSource userSource, @NonNull RegisterContract.View registerView) {

        mUserSource = userSource;
        mView = registerView;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe(boolean isLoading) {

        if (isLoading) {
            mView.showProgress();
            doRxRegister();
        }
    }

    @Override
    public void unsubscribe() {

        if (!mCompositeDisposable.isDisposed()) {

            mCompositeDisposable.clear();
        }
    }

    @Override
    public void register(String name, String email, String password) {

        mView.showProgress();
        UserRequest request = new UserRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);

        doRxRegister(request);
    }

    private void doRxRegister(UserRequest... request) {

        if (request.length > 0) {

            Observable<UserResponse> observable = mUserSource.register(request[0]).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            NetworkService.putObservable(observable.cache(), UserResponse.class);
        }

        Observable<UserResponse> observableCache = (Observable<UserResponse>) NetworkService.getObservable(UserResponse.class);

        mCompositeDisposable.add(observableCache
                .subscribe(response -> {
                            mView.hideProgress();
                            mView.showSuccess(response);
                        },
                        throwable -> {
                            mView.hideProgress();
                            mView.showError(ErrorHandler.handleHTTPError(throwable));
                        })

        );
    }

}
