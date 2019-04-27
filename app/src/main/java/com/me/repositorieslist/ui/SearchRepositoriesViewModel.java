package com.me.repositorieslist.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import android.util.Log;

import com.me.repositorieslist.data.GithubRepository;
import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.model.RepoSearchResult;



public class SearchRepositoriesViewModel extends ViewModel {

    private static final String LOG_TAG = SearchRepositoriesViewModel.class.getSimpleName();

    GithubRepository githubRepo;

    MutableLiveData<String> queryLiveData = new MutableLiveData<String>();

    private LiveData<PagedList<Repo>> reposListLiveData = Transformations.switchMap(queryLiveData,
            inputQuery -> githubRepo.search(inputQuery)
    );

    public SearchRepositoriesViewModel(GithubRepository githubRepo) {
        this.githubRepo = githubRepo;

    }

    void searchRepo(String queryString) {
        Log.d(LOG_TAG, "searchRepo: " + queryString);
        queryLiveData.postValue(queryString);
    }

    public LiveData<PagedList<Repo>> getReposListLiveData() {
        return reposListLiveData;
    }

    String lastQueryValue() {
        return queryLiveData.getValue();
    }
}
