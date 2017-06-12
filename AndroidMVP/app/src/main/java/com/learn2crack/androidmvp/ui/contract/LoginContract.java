package com.learn2crack.androidmvp.ui.contract;

import com.learn2crack.androidmvp.base.BasePresenter;
import com.learn2crack.androidmvp.base.BaseView;
import com.learn2crack.androidmvp.model.user.UserResponse;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void launchProfile(UserResponse response);
        void showError(String message);
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenter {

        void login(String email, String password);
    }
}
