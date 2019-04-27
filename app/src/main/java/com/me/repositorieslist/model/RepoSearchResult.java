package com.me.repositorieslist.model;

import android.arch.lifecycle.LiveData;

import java.util.List;


public class RepoSearchResult {
    //LiveData for Search Results
    private final LiveData<List<Repo>> data;
    //LiveData for the Network Errors
    private final LiveData<String> networkErrors;

    public RepoSearchResult(LiveData<List<Repo>> data, LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }

    public LiveData<List<Repo>> getData() {
        return data;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }
}
