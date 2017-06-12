package com.learn2crack.androidmvp.ui.presenter;

import android.support.annotation.NonNull;

import com.learn2crack.androidmvp.data.user.UserSource;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.network.NetworkService;
import com.learn2crack.androidmvp.ui.contract.ProfileContract;
import com.learn2crack.androidmvp.util.ErrorHandler;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProfilePresenter implements ProfileContract.Presenter {

    public static final String TAG = ProfilePresenter.class.getSimpleName();

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @NonNull
    private UserSource mUserSource;

    @NonNull
    private ProfileContract.View mView;

    public ProfilePresenter(@NonNull UserSource userSource, @NonNull ProfileContract.View profileView) {

        mUserSource = userSource;
        mView = profileView;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe(boolean isLoading) {

        if (isLoading) {
            mView.showProgress();
            doRxRequest();
        } else {

            if(!mView.isDataLoaded()) {

                getProfile(mView.getToken(), mView.getEmail());
                mView.setLoading(true);
            }
        }
    }

    @Override
    public void unsubscribe() {

        if (!mCompositeDisposable.isDisposed()) {

            mCompositeDisposable.clear();
        }
    }

    @Override
    public void getProfile(String token, String email) {

        mView.showProgress();

        doRxRequest(token, email);
    }

    private void doRxRequest(String... args) {


        if (args.length > 0) {

            Observable<UserResponse> observable = mUserSource.getProfile(args[0], args[1])
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            NetworkService.putObservable(observable.cache(), UserResponse.class);
        }

        Observable<UserResponse> observableCache = (Observable<UserResponse>) NetworkService.getObservable(UserResponse.class);

        mCompositeDisposable.add(observableCache
                .subscribe(loginResponse -> {
                            mView.hideProgress();
                            mView.displayProfile(loginResponse);
                        },
                        throwable -> {
                            mView.hideProgress();
                            mView.showError(ErrorHandler.handleHTTPError(throwable));
                        })

        );
    }
}
