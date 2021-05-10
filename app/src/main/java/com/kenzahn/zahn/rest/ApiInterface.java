package com.kenzahn.zahn.rest;

import com.kenzahn.zahn.database.UpdatePre;
import com.kenzahn.zahn.model.HomeDuckData;
import com.kenzahn.zahn.model.LoginModel;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface ApiInterface {
    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/FlashCards/GetFlashCardsList/?")
    Call<HomeDuckData> getBookListDetails2(@Query("userName") int var1, @Query("isIos") boolean var2, @Query("width") double var3, @Query("height") double var5);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/AppUsers/GetIsUserValid?")
    Call<LoginModel> getLogin(@Query("username") String var1, @Query("password")  String var2);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/MobileAppPreference/UpdateMobileAppPreference")
    Call<Object> getUpdatePref(@Body UpdatePre var1);


    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/AppUsers/Login")
    Call<LoginModel> getLogin(@Body UpdatePre var1);
}
