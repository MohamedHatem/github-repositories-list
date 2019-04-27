package com.me.repositorieslist.data;

import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.me.repositorieslist.api.GithubServiceClient;
import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.model.RepoSearchResult;
import com.me.repositorieslist.ui.Injection;

import java.util.List;


public class RepoDataSource extends PageKeyedDataSource<Integer, Repo> {


    private static final String LOG_TAG = RepoDataSource.class.getSimpleName();


    public static final int PAGE_SIZE = 10;

    private static final int FIRST_PAGE = 0;


    String inputQuery;

    public RepoDataSource(String aInputQuery) {
        inputQuery = aInputQuery;

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Repo> callback) {

        Log.d(LOG_TAG, "loadInitial");

        GithubServiceClient
                .searchRepos(inputQuery, FIRST_PAGE, PAGE_SIZE, new GithubServiceClient.ApiCallback() {
                    @Override
                    public void onSuccess(List<Repo> reposList) {
                        callback.onResult(reposList, null, FIRST_PAGE + 1);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Repo> callback) {

        Log.d(LOG_TAG, "loadBefore");

        GithubServiceClient
                .searchRepos(inputQuery, params.key, PAGE_SIZE, new GithubServiceClient.ApiCallback() {
            @Override
            public void onSuccess(List<Repo> reposList) {
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (reposList != null) {

                    callback.onResult(reposList, adjacentKey);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Repo> callback) {
        Log.d(LOG_TAG, "loadAfter");

        GithubServiceClient
                .searchRepos(inputQuery, params.key, PAGE_SIZE, new GithubServiceClient.ApiCallback() {
            @Override
            public void onSuccess(List<Repo> reposList) {

                if (reposList != null) {
                    callback.onResult(reposList, params.key + 1);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }
}
