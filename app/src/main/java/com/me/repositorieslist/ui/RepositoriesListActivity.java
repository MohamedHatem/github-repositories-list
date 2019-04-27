package com.me.repositorieslist.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.me.repositorieslist.R;
import com.me.repositorieslist.databinding.ActivityRepoListBinding;
import com.me.repositorieslist.model.Repo;
import com.me.repositorieslist.model.RepoSearchResult;

import java.util.ArrayList;
import java.util.List;

public class RepositoriesListActivity extends AppCompatActivity {

    private static final String LOG_TAG = RepositoriesListActivity.class.getSimpleName();

    RecyclerView.LayoutManager layoutManager;
    ActivityRepoListBinding binding;

    ReposAdapter reposAdapter;
    List<Repo> fetchedReposList;

    SearchRepositoriesViewModel mViewModel;

    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "Android";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_list);


        fetchedReposList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        binding.reposListRv.setLayoutManager(layoutManager);

        reposAdapter = new ReposAdapter();
        binding.reposListRv.setAdapter(reposAdapter);


        String query = DEFAULT_QUERY;

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(LAST_SEARCH_QUERY, DEFAULT_QUERY);
        }

        mViewModel = ViewModelProviders.of(this,
                Injection.provideRepositoryViewModelFactory(getApplicationContext()))
                .get(SearchRepositoriesViewModel.class);

        mViewModel.getReposListLiveData().observe(this, repos -> {
            if (repos != null) {
                Log.d(LOG_TAG, "observe on [getReposListLiveData]  Repo List size: " + repos.size());
                reposAdapter.submitList(repos);
            }
        });

        mViewModel.searchRepo(query);
        initSearch(query);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SEARCH_QUERY, mViewModel.lastQueryValue());
    }

    private void initSearch(String query) {
        binding.searchQueryEt.setText(query);

        binding.searchQueryEt.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput();
                return true;
            } else {
                return false;
            }
        });

        binding.searchQueryEt.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput();
                return true;
            } else {
                return false;
            }
        });
    }

    private void updateRepoListFromInput() {
        String queryEntered = binding.searchQueryEt.getText().toString().trim();
        if (!TextUtils.isEmpty(queryEntered)) {
            binding.reposListRv.scrollToPosition(0);
            //Posts the query to be searched
            mViewModel.searchRepo(queryEntered);
            //Resets the old list
            reposAdapter.submitList(null);
        }
    }


}
