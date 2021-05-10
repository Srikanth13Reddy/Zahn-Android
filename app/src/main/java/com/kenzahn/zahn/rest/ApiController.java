package com.kenzahn.zahn.rest;

import com.kenzahn.zahn.database.UpdatePre;
import com.kenzahn.zahn.model.HomeDuckData;
import com.kenzahn.zahn.model.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiController {
    private ApiInterface mApiInterface;
    private ApiInterface mApiInterfac;
    public ApiController(ApiInterface apiInterface)
    {
        this.mApiInterface = apiInterface;
        this.mApiInterfac = apiInterface;
    }
    public interface ApiCallBack
    {
        void OnSuccess(int var1, Object var2);

        void OnErrorResponse(int var1, Object var2);

        void onFailure(int var1, Object var2);
    }

    public final void getBookListDetails2(int userId, double deviceWidth, double deviceHeight, final int RequestCode, final ApiController.ApiCallBack apiCallBack) {
        Call<HomeDuckData> getBookList = this.mApiInterface.getBookListDetails2(userId, false, deviceWidth, deviceHeight);
        getBookList.enqueue(new Callback<HomeDuckData>() {
            @Override
            public void onResponse(Call<HomeDuckData> call, Response<HomeDuckData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiCallBack.OnSuccess(RequestCode,response.body());
                } else {
                    apiCallBack.OnErrorResponse(RequestCode, response.body());
                }
            }

            @Override
            public void onFailure(Call<HomeDuckData> call, Throwable t) {
                apiCallBack.onFailure(RequestCode,t);
            }
        });
    }


    public final void getLogin(String username,  String password, final int RequestCode,  final ApiController.ApiCallBack apiCallBack) {
        Call<LoginModel> loginModelCall = this.mApiInterface.getLogin(username, password);

        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiCallBack.OnSuccess(RequestCode,response.body());
                } else {
                    apiCallBack.OnErrorResponse(RequestCode, response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                apiCallBack.onFailure(RequestCode,t);
            }
        });
    }
    public final void getUpdatePre(UpdatePre jsonObject, final int RequestCode, final ApiController.ApiCallBack apiCallBack) {
        Call<Object> updatePref = this.mApiInterface.getUpdatePref(jsonObject);

        updatePref.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiCallBack.OnSuccess(RequestCode,response.body());
                } else {
                    apiCallBack.OnErrorResponse(RequestCode, response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                apiCallBack.onFailure(RequestCode,t);
            }
        });

    }


}
