package com.learn2crack.androidmvp.ui.contract;

import com.learn2crack.androidmvp.base.BasePresenter;
import com.learn2crack.androidmvp.base.BaseView;
import com.learn2crack.androidmvp.model.user.UserResponse;

public interface RegisterContract {

    interface View extends BaseView<RegisterContract.Presenter> {

        void showSuccess(UserResponse response);
        void showError(String message);
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenter {

        void register(String name, String email, String password);
    }
}
