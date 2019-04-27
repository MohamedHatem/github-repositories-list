package com.me.repositorieslist.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.me.repositorieslist.model.Repo;

import java.util.List;

public class RepoSearchResponse {

    @SerializedName("total_count")
    @Expose
    public Integer totalCount;

    @SerializedName("incomplete_results")
    @Expose
    public Boolean incompleteResults;

    @SerializedName("items")
    @Expose
    public List<Repo> reposList = null;
}
