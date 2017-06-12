package com.learn2crack.androidmvp.data.user;

import android.support.annotation.NonNull;
import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.model.user.UserResponse;

import io.reactivex.Observable;

public interface UserSource {

    Observable<UserResponse> login(@NonNull UserRequest request);

    Observable<UserResponse> register(@NonNull UserRequest request);

    Observable<UserResponse> getProfile(@NonNull String token, @NonNull String email);

    Observable<UserResponse> changePassword(@NonNull String token, @NonNull UserRequest request);
}
