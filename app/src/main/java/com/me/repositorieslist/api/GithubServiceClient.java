package com.me.repositorieslist.api;

import android.util.Log;

import com.me.repositorieslist.model.Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubServiceClient {


    private static final String LOG_TAG = GithubServiceClient.class.getSimpleName();

    public static Response<RepoSearchResponse> searchRepos(String query, int page, int itemsPerPage) throws IOException {

        GithubService githubService = ServiceGenerator.createService(GithubService.class);

        return githubService.searchRepos(query, page, itemsPerPage).execute();

    }

    public static void searchRepos(String query, int page, int itemsPerPage,
                                   ApiCallback apiCallback) {

        GithubService githubService = ServiceGenerator.createService(GithubService.class);

        Call<RepoSearchResponse> call = githubService.searchRepos(query, page, itemsPerPage);

        call.enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(Call<RepoSearchResponse> call, Response<RepoSearchResponse> response) {

                Log.d(LOG_TAG, "onResponse: Got response:" + response);

                if (response.isSuccessful()) {
                    List<Repo> reposList = response.body() != null ? response.body().reposList : new ArrayList<>();
                    apiCallback.onSuccess(reposList);

                } else {

                    apiCallback.onError(response.errorBody() != null ? response.errorBody().toString() : "unkown error message");
                }
            }

            @Override
            public void onFailure(Call<RepoSearchResponse> call, Throwable t) {

                Log.d(LOG_TAG, "onFailure: Failed to get data");

                apiCallback.onError(t.getMessage() != null ? t.getMessage() : "unkown error type");
            }
        });
    }

    public interface ApiCallback {

        void onSuccess(List<Repo> reposList);

        void onError(String errorMessage);
    }
}
