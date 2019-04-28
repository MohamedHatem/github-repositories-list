package com.me.repositorieslist.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.me.repositorieslist.R;
import com.me.repositorieslist.databinding.ActivityRepoListBinding;


/* TODO :
          1- Why "pagedListAdapter.submitList" is called in "observer on pagedList" callback ?
          2- To understand when and why the pagedListAdapter.submitList is notified?
          3- When the pagedList fetches new items, How it notifys the adapter ? does it calls "pagedListAdapter.submitList" ?
          4- PagedList calls  "pagedListAdapter.submitList" on database-network model ? and doesn't call it on network model ?
 */
public class RepositoriesListActivity extends AppCompatActivity {

    private static final String LOG_TAG = RepositoriesListActivity.class.getSimpleName();

    RecyclerView.LayoutManager layoutManager;
    ActivityRepoListBinding binding;

    ReposAdapter reposAdapter;

    SearchRepositoriesViewModel mViewModel;

    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "Android";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_list);


        layoutManager = new LinearLayoutManager(this);
        binding.reposListRv.setLayoutManager(layoutManager);

        //Set the Empty text with emoji unicode
        binding.emptyList.setText(getString(R.string.no_results, "\uD83D\uDE13"));

        //Get the view model
        mViewModel = ViewModelProviders.of(this, Injection.provideRepositoryViewModelFactory(this))
                .get(SearchRepositoriesViewModel.class);

        initRecyclerView();

        String query = DEFAULT_QUERY;

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(LAST_SEARCH_QUERY, DEFAULT_QUERY);
        }

//        mViewModel.searchRepo(query);
        initSearch(query);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SEARCH_QUERY, mViewModel.lastQueryValue());
    }

    /**
     * Initializes the RecyclerView that loads the list of Repos
     */
    private void initRecyclerView() {
        //Add dividers between RecyclerView's row items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.reposListRv.addItemDecoration(dividerItemDecoration);

        //Initializing Adapter
        initAdapter();
    }

    /**
     * Initializes the Adapter of RecyclerView which is {@link ReposAdapter}
     */
    private void initAdapter() {
        reposAdapter = new ReposAdapter();
        binding.reposListRv.setAdapter(reposAdapter);

        //Subscribing to receive the new PagedList Repos
        mViewModel.getRepos().observe(this, repos -> {
            if (repos != null) {
                Log.d(LOG_TAG, "initAdapter: Repo List size: " + repos.size());
                showEmptyList(repos.size() == 0);
                reposAdapter.submitList(repos);
            }
        });

        //Subscribing to receive the recent Network Errors if any
        mViewModel.getNetworkErrors().observe(this, errorMsg -> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops " + errorMsg, Toast.LENGTH_LONG).show();
        });
    }

    private void showEmptyList(boolean show) {
        if (show) {
            binding.reposListRv.setVisibility(View.GONE);
            binding.emptyList.setVisibility(View.VISIBLE);
        } else {
            binding.reposListRv.setVisibility(View.VISIBLE);
            binding.emptyList.setVisibility(View.GONE);
        }
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
