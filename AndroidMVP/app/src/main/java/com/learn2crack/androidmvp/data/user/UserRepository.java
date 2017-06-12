package com.learn2crack.androidmvp.data.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.model.user.UserResponse;
import com.learn2crack.androidmvp.network.NetworkService;

import io.reactivex.Observable;

public class UserRepository implements UserSource {

    @Nullable
    private static UserRepository INSTANCE = null;

    private UserRepository() {

    }

    public static UserRepository getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {

        INSTANCE = null;
    }

    @Override
    public Observable<UserResponse> login(@NonNull UserRequest request) {

        return NetworkService.getRetrofitForLogin(request).login();
    }

    @Override
    public Observable<UserResponse> register(@NonNull UserRequest request) {

        return NetworkService.getRetrofit().register(request);
    }

    @Override
    public Observable<UserResponse> getProfile(@NonNull String token, @NonNull String email) {

        return NetworkService.getRetrofitWithAuth(token).getProfile(email);
    }

    @Override
    public Observable<UserResponse> changePassword(@NonNull String token, @NonNull UserRequest request) {

        return NetworkService.getRetrofitWithAuth(token).changePassword(request.getEmail(), request);
    }
}
