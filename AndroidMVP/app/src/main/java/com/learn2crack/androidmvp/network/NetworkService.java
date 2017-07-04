package com.learn2crack.androidmvp.network;

import android.util.Base64;
import android.util.LruCache;

import com.learn2crack.androidmvp.model.user.UserRequest;
import com.learn2crack.androidmvp.util.Constants;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    public static final String TAG = NetworkService.class.getSimpleName();

    private static LruCache<Class<?>, Observable<?>> mObservables = new LruCache<>(10);

    public static Observable<?> getObservable(Class<?> clazz){
        Observable<?> observable = null;

        observable = mObservables.get(clazz);

        return observable;
    }

    public static void putObservable(Observable<?> observable, Class<?> clazz) {

        mObservables.put(clazz, observable);
    }

    public static RESTInterface getRetrofit(){

        OkHttpClient client = OkHttpProvider.getOkHttpInstance();

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(RESTInterface.class);
    }

    public static RESTInterface getRetrofitForLogin(UserRequest request) {

        String credentials = request.getEmail() + ":" + request.getPassword();
        String basic = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.NO_WRAP);
        OkHttpClient client = OkHttpProvider.getOkHttpInstance();
        OkHttpClient.Builder httpClient = client.newBuilder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("Authorization", basic)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());

        });

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RESTInterface.class);
    }

    public static RESTInterface getRetrofitWithAuth(String token) {

        OkHttpClient client = OkHttpProvider.getOkHttpInstance();
        OkHttpClient.Builder httpClient = client.newBuilder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("x-access-token", token)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());
        });

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RESTInterface.class);
    }
}