package com.me.repositorieslist.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.util.Log;

import com.me.repositorieslist.data.GithubRepository;
import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.model.RepoSearchResult;


public class SearchRepositoriesViewModel extends ViewModel {

    private static final String LOG_TAG = SearchRepositoriesViewModel.class.getSimpleName();

    GithubRepository githubRepo;

    MutableLiveData<String> queryLiveData = new MutableLiveData<String>();

    //Applying transformation to get RepoSearchResult for the given Search Query
    private LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData,
            inputQuery -> githubRepo.search(inputQuery)
    );
    //Applying transformation to get Live PagedList<Repo> from the RepoSearchResult
    private LiveData<PagedList<Repo>> repos = Transformations.switchMap(repoResult,
            RepoSearchResult::getData
    );
    //Applying transformation to get Live Network Errors from the RepoSearchResult
    private LiveData<String> networkErrors = Transformations.switchMap(repoResult,
            RepoSearchResult::getNetworkErrors
    );
    public SearchRepositoriesViewModel(GithubRepository githubRepo) {
        this.githubRepo = githubRepo;

    }

    void searchRepo(String queryString) {
        Log.d(LOG_TAG, "searchRepo: " + queryString);
        queryLiveData.postValue(queryString);
    }

    public LiveData<PagedList<Repo>> getRepos() {
        return repos;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    String lastQueryValue() {
        return queryLiveData.getValue();
    }
}
