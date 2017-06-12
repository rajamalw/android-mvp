package com.learn2crack.androidmvp.ui.contract;

import com.learn2crack.androidmvp.base.BasePresenter;
import com.learn2crack.androidmvp.base.BaseView;
import com.learn2crack.androidmvp.model.user.UserResponse;

public interface ProfileContract {

    interface View extends BaseView<ProfileContract.Presenter> {

        void displayProfile(UserResponse response);
        void showError(String message);
        void showProgress();
        void hideProgress();
        String getToken();
        String getEmail();
        void setLoading(boolean isLoading);
        boolean isDataLoaded();

    }

    interface Presenter extends BasePresenter {

        void getProfile(String token, String email);
    }
}
