package com.me.repositorieslist.ui;

import android.content.Context;

import com.me.repositorieslist.data.GithubRepository;
import com.me.repositorieslist.db.GithubLocaleCache;
import com.me.repositorieslist.db.RepoDao;
import com.me.repositorieslist.db.RepoDatabase;

import java.util.concurrent.Executors;

public class Injection {


    public static RepoDao provideRepoDao(Context context) {
        return RepoDatabase.getInstance(context).reposDao();
    }

    public static GithubLocaleCache provideGithubLocaleCache(Context context) {
        return new GithubLocaleCache(provideRepoDao(context), Executors.newSingleThreadExecutor());
    }

    public static GithubRepository provideGithubRepository(Context context) {
        return new GithubRepository(/*provideGithubLocaleCache(context), Executors.newSingleThreadExecutor()*/);
    }

    public static ViewModelFactory provideRepositoryViewModelFactory(Context context) {
        return new ViewModelFactory(provideGithubRepository(context));
    }
}
