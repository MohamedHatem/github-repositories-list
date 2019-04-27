package com.me.repositorieslist.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.me.repositorieslist.data.GithubRepository;
import com.me.repositorieslist.model.RepoSearchResult;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private Object[] mParams;

    public ViewModelFactory(Object... params) {
        mParams = params;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        try {
            if (modelClass == SearchRepositoriesViewModel.class) {
                return (T) new SearchRepositoriesViewModel((GithubRepository) mParams[0]);
            }
        } catch (Exception e) {
        }
        return super.create(modelClass);
    }
}
