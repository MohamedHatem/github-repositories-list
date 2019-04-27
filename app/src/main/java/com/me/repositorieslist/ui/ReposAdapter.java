package com.me.repositorieslist.ui;

import android.arch.paging.PagedListAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.repositorieslist.R;
import com.me.repositorieslist.databinding.RepoViewItemBinding;
import com.me.repositorieslist.model.Repo;


public class ReposAdapter extends PagedListAdapter<Repo, ReposAdapter.RepoViewHolder> {


    private static DiffUtil.ItemCallback<Repo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Repo>() {

                @Override
                public boolean areItemsTheSame(Repo oldItem, Repo newItem) {
                    return oldItem.fullName.equals(newItem.fullName);
                }

                @Override
                public boolean areContentsTheSame(Repo oldItem, Repo newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public ReposAdapter() {
        super(DIFF_CALLBACK);
    }


    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepoViewItemBinding repoViewItemBinding = RepoViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RepoViewHolder(repoViewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RepoViewItemBinding mDataBinding;

        public RepoViewHolder(RepoViewItemBinding mItemBinding) {
            super(mItemBinding.getRoot());
            mDataBinding = mItemBinding;

            View itemView = mDataBinding.getRoot();
            itemView.setOnClickListener(this);

        }

        public void bind(final Repo repo) {
            if (repo == null) {
                Resources resources = mDataBinding.getRoot().getContext().getResources();
                mDataBinding.repoTitleTv.setText(resources.getString(R.string.loading));
                mDataBinding.repoDecTv.setVisibility(View.GONE);
                mDataBinding.repoLangTv.setVisibility(View.GONE);
                mDataBinding.forksCountTv.setText(resources.getString(R.string.unknown));
                mDataBinding.starsCountTv.setText(resources.getString(R.string.unknown));
            } else {
                mDataBinding.setRepo(repo);
                mDataBinding.executePendingBindings();
            }

        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() > RecyclerView.NO_POSITION) {
                Repo repo = getItem(getAdapterPosition());
                if (repo != null && !TextUtils.isEmpty(repo.url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo.url));
                    mDataBinding.getRoot().getContext().startActivity(intent);
                }
            }
        }
    }


}



