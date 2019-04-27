package com.me.repositorieslist.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.me.repositorieslist.api.GithubServiceClient;
import com.me.repositorieslist.db.GithubLocaleCache;
import com.me.repositorieslist.model.Repo;

import java.util.List;

public class RepoBoundaryCallback extends
        PagedList.BoundaryCallback<Repo> implements GithubServiceClient.ApiCallback {

    private static final String LOG_TAG = RepoBoundaryCallback.class.getSimpleName();

    private static final int NETWORK_PAGE_SIZE = 50;
    private String query;
    private GithubLocaleCache localCache;

    private int lastRequestedPage = 1;

    // Avoid triggering multiple requests in the same time
    private boolean isRequestInProgress = false;

    // LiveData of network errors.
    private MutableLiveData<String> networkErrors = new MutableLiveData<>();


    public RepoBoundaryCallback(String query, GithubLocaleCache localCache) {
        this.query = query;
        this.localCache = localCache;
    }

    LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    private void requestAndSaveData(String query) {
        //Exiting if the request is in progress
        if (isRequestInProgress) return;

        //Set to true as we are starting the network request
        isRequestInProgress = true;

        //Calling the client API to retrieve the Repos for the given search query
        GithubServiceClient.searchRepos(query, lastRequestedPage, NETWORK_PAGE_SIZE, this);
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    @Override
    public void onZeroItemsLoaded() {
        Log.d(LOG_TAG, "onZeroItemsLoaded: Started");
        requestAndSaveData(query);
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within {@link PagedList.Config#prefetchDistance} of it.
     * <p>
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    @Override
    public void onItemAtEndLoaded(@NonNull Repo itemAtEnd) {
        Log.d(LOG_TAG, "onItemAtEndLoaded: Started");
        requestAndSaveData(query);
    }

    @Override
    public void onSuccess(List<Repo> reposList) {
        //Inserting records in the database thread
        localCache.insert(reposList, () -> {
            //Updating the last requested page number when the request was successful
            //and the results were inserted successfully
            lastRequestedPage++;
            //Marking the request progress as completed
            isRequestInProgress = false;
        });
    }

    @Override
    public void onError(String errorMessage) {
        //Update the Network error to be shown
        networkErrors.postValue(errorMessage);
        //Mark the request progress as completed
        isRequestInProgress = false;

    }
}
