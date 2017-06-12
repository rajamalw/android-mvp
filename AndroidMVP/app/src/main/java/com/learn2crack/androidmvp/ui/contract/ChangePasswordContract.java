package com.learn2crack.androidmvp.ui.contract;

import com.learn2crack.androidmvp.base.BasePresenter;
import com.learn2crack.androidmvp.base.BaseView;
import com.learn2crack.androidmvp.model.user.UserResponse;

public interface ChangePasswordContract {

    interface View extends BaseView<ChangePasswordContract.Presenter> {

        void displayMessage(UserResponse response);
        void showError(String message);
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenter {

        void changePassword(String token, String email, String oldPassword, String newPassword);
    }
}
