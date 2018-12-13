package com.hhtc.dialer.main.collects;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.main.DialerFragment;

import java.util.List;

public class CollectsFragment extends DialerFragment {

    private CollectViewModel viewModel;

    private RecyclerView collect_view;

    private CollectsAdapter adapter;

    public static CollectsFragment newInstance() {
        return new CollectsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collects_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        collect_view = view.findViewById(R.id.collect_view);
        adapter = new CollectsAdapter(getContext());
        collect_view.setLayoutManager(adapter.getLayoutManager(getContext()));
        adapter.bindRecycler(collect_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CollectViewModel.class);
        viewModel.getNotify().observe(this, this::onChanged);
        viewModel.loadFavorite();
    }

    private void onChanged(Void iVoid) {
        viewModel.getFavorite().observe(this, this::loadFavoriteSuccess);
    }

    private void loadFavoriteSuccess(List<CollectFavorite> favorites) {
        adapter.setModels(favorites);
    }


}
