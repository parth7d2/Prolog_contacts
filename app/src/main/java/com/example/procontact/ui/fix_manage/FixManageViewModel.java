package com.example.procontact.ui.fix_manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FixManageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FixManageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("These are FixManage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}