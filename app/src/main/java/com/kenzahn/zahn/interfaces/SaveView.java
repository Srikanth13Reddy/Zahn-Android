package com.kenzahn.zahn.interfaces;

public interface SaveView {

    void onSaveSucess(String code, String response);
    void onSaveFailure(String error);
}
