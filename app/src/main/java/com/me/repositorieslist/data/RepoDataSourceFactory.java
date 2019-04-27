package com.me.repositorieslist.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.util.Log;

import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.ui.RepositoriesListActivity;

public class RepoDataSourceFactory extends DataSource.Factory {

    private static final String LOG_TAG = RepoDataSourceFactory.class.getSimpleName();

    private MutableLiveData<PageKeyedDataSource<Integer, Repo>> repoLiveDataSource = new MutableLiveData<>();


    private String inputQuery;

    public RepoDataSourceFactory(String inputQuery) {
        this.inputQuery = inputQuery;
    }

    @Override
    public DataSource create() {
        Log.d(LOG_TAG, "create: " + inputQuery);

        RepoDataSource repoDataSource = new RepoDataSource(inputQuery);
        repoLiveDataSource.postValue(repoDataSource);
        return repoDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Repo>> getRepoLiveDataSource() {
        return repoLiveDataSource;
    }


}
