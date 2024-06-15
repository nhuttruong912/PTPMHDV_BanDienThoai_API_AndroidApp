package com.example.storeapplication.retrofit;

import com.example.storeapplication.model.CartPost;
import com.example.storeapplication.model.CartRoot;
import com.example.storeapplication.model.Color;
import com.example.storeapplication.model.ItemRoot;
import com.example.storeapplication.model.LoginGet;
import com.example.storeapplication.model.LoginPost;
import com.example.storeapplication.model.Phone;
import com.example.storeapplication.model.PhoneDetailRoot;
import com.example.storeapplication.model.StorageRoot;
import com.example.storeapplication.model.UserGet;
import com.example.storeapplication.model.UserPost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    Gson gson = new GsonBuilder().create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl("http://14.225.207.131:8080/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @POST("User/Register")
    Call<UserGet> registerUser(@Body UserPost userPost);

    @POST("User/Login")
    Call<LoginGet> login(@Body LoginPost loginPost);

    @GET("Phone/AllPhones")
    Call<List<Phone>> getAllPhoneActive();

    @GET("Phone/AllPhonesSellingFollowBrand")
    Call<List<PhoneDetailRoot>> getPhoneDetail(@Query("Name") String name);

    @GET("Phone/GetPhoneBuiltInStorages/{phoneId}")
    Call<StorageRoot> getStorage(@Path("phoneId") int phoneId);

    @GET("Phone/{phoneId}/{builtInStorageId}")
    Call<List<Color>> getColor(@Path("phoneId") int phoneId, @Path("builtInStorageId") int builtInStorageId);

//    @POST("Cart/user/{userId}/phoneOption/{phoneOptionId}")
//    Call<CartRoot> postCart(@Path("userId") String userId, @Path("phoneOptionId") int phoneOptionId);
    @POST("Cart/AddItemToCart")
    Call<CartRoot> postCart(@Body CartPost cartPost);
    @GET("Cart/user/{userId}")
    Call<ItemRoot> getCart(@Path("userId") String userId);
}