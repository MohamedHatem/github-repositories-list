package com.me.repositorieslist.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.me.repositorieslist.model.Repo;

import java.util.List;

@Dao
public interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Repo> posts);

    @Query("SELECT * FROM repos WHERE name LIKE (:queryString) OR description LIKE (:queryString) ORDER BY stars DESC, name ASC")
    DataSource.Factory<Integer, Repo> reposByName(String queryString);

}
