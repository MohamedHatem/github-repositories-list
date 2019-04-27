package com.me.repositorieslist.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.me.repositorieslist.db.GithubLocaleCache;
import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.model.RepoSearchResult;

public class GithubRepository {

    private static final String LOG_TAG = GithubRepository.class.getSimpleName();

    private GithubLocaleCache localCache;
    private static final int DATABASE_PAGE_SIZE = 20;


    public GithubRepository(GithubLocaleCache localCache) {
        this.localCache = localCache;
    }


    public RepoSearchResult search(String query) {
        Log.d(LOG_TAG, "search: New query: " + query);

        // Get data source factory from the local cache
        DataSource.Factory<Integer, Repo> reposByName = localCache.reposByName(query);

        // Construct the boundary callback
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(query, localCache);
        LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();

        // Set the Page size for the Paged list
        PagedList.Config pagedConfig = new PagedList.Config.Builder()
                .setPageSize(DATABASE_PAGE_SIZE)
                .build();

        // Get the Live Paged list
        LiveData<PagedList<Repo>> data = new LivePagedListBuilder<>(reposByName, pagedConfig)
                .setBoundaryCallback(boundaryCallback)
                .build();

        // Get the Search result with the network errorgit pull origin s exposed by the boundary callback
        return new RepoSearchResult(data, networkErrors);
    }


}