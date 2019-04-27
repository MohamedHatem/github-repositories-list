package com.me.repositorieslist.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import android.util.Log;

import com.me.repositorieslist.api.GithubService;
import com.me.repositorieslist.api.GithubServiceClient;
import com.me.repositorieslist.api.RepoSearchResponse;
import com.me.repositorieslist.db.GithubLocaleCache;
import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.model.RepoSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Response;

public class GithubRepository {

    private static final String LOG_TAG = GithubRepository.class.getSimpleName();

    //    private GithubLocaleCache localCache;
//    private final Executor executor;
//
//    MutableLiveData<List<Repo>> reposList;
//    MutableLiveData<String> networkErrors;
//    RepoSearchResult repoSearchResult ;
    public GithubRepository(/*GithubLocaleCache localCache,
                            Executor executor*/) {
//        this.localCache = localCache;
//        this.executor = executor;
//        reposList = new MutableLiveData<>();
//        networkErrors = new MutableLiveData<>();
//        repoSearchResult = new RepoSearchResult(reposList, networkErrors);
    }

//    public RepoSearchResult getRepoSearchResult() {
//        return repoSearchResult;
//    }

    public LiveData<PagedList<Repo>> search(String query) {
        Log.d(LOG_TAG, "search: " + query);
//        Log.d(LOG_TAG, "search query =[" + query + "repos]");
//        GithubServiceClient.searchRepos(query, page, itemsPerPage, apiCallback);
//        refreshSearch(query, page, itemsPerPage);

        RepoDataSourceFactory repoDataSourceFactory = new RepoDataSourceFactory(query);

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(RepoDataSource.PAGE_SIZE).build();

        //Building the paged list
        LiveData<PagedList<Repo>> data =
                (new LivePagedListBuilder(repoDataSourceFactory, pagedListConfig))
                        .build();
        return data;
    }

//    private void refreshSearch(String query, int page, int itemsPerPage) {
//        executor.execute(() -> {
//
//            Log.d(LOG_TAG, "refreshSearch reposList null or size = 0 : network");
//
//            try {
//                Response<RepoSearchResponse> response = GithubServiceClient.searchRepos(query, page, itemsPerPage);
//                if (response.isSuccessful()) {
////                    localCache.insert(response.body() != null ? response.body().reposList : new ArrayList<>());
//                    if (response.body() != null) reposList.postValue(response.body().reposList);
//                    else reposList.postValue(new ArrayList<>());
//
//                } else {
//                    networkErrors.postValue(response.errorBody() != null ? response.errorBody().toString() : "unkown error message");
//                }
//            } catch (IOException t) {
//                networkErrors.postValue(t.getMessage() != null ? t.getMessage() : "unkown error type");
//            }
//
//        });
//    }

}