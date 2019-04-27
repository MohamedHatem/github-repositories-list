package com.me.repositorieslist.db;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.me.repositorieslist.model.Repo;

import java.util.List;
import java.util.concurrent.Executor;

public class GithubLocaleCache {


    private static final String LOG_TAG = GithubLocaleCache.class.getSimpleName();

    private RepoDao repoDao;

    private Executor ioExecutor;

    public GithubLocaleCache(RepoDao repoDao, Executor ioExecutor) {
        this.repoDao = repoDao;
        this.ioExecutor = ioExecutor;
    }

    public void insert(List<Repo> reposList/*, InsertCallback insertCallback*/) {
//        ioExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
        Log.d(LOG_TAG, "insert: inserting " + reposList.size() + " repos");
        repoDao.insert(reposList);
//                insertCallback.insertFinished();
//            }
//        });
    }

    public LiveData<List<Repo>> reposByName(String name) {
        return repoDao.reposByName("%" + name.replace(' ', '%') + "%");
    }



    public interface InsertCallback {
        void insertFinished();
    }

}
