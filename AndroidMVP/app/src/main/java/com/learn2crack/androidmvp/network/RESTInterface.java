package com.learn2crack.androidmvp.network;

import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.model.user.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RESTInterface {

    @POST("authenticate")
    Observable<UserResponse> login();

    @POST("users")
    Observable<UserResponse> register(@Body UserRequest request);

    @GET("users/{email}")
    Observable<UserResponse> getProfile(@Path("email") String email);

    @PUT("users/{email}")
    Observable<UserResponse> changePassword(@Path("email") String email, @Body UserRequest user);
}
