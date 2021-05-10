package com.kenzahn.zahn.rest;

import retrofit2.Call;
import retrofit2.http.GET;

public class ApiInterfacee {

    @GET("api/json/get/cwlpIanKKW?indent=2")
    Call<String> getBookList() {
        return null;
    }

   /* Call getBookList();

    @GET("api/json/get/cfYfrLIAjm?indent=2")
    Call getBookListDetails();

    @GET("api/FlashCards/GetFlashCardsList/?")
    Call getBookListDetails2(@Query("userName") int var1, @Query("isIos") boolean var2, @Query("width") double var3, @Query("height") double var5);

    @GET("api/AppUsers/GetIsUserValid?")
    Call getLogin(@Query("username")String var1, @Query("password") String var2);

    @POST("api/MobileAppPreference/UpdateMobileAppPreference")
    Call getUpdatePref(@Body UpdatePre var1);*/
}
