package com.hhtc.dialer.main.recent;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.main.DialerFragment;

import java.util.List;

public class RecentFragment extends DialerFragment {

    private RecentViewModel mViewModel;

    private RecyclerView recent_view;

    private RecentAdapter adapter;

    public static RecentFragment newInstance() {
        return new RecentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recent_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recent_view = view.findViewById(R.id.recent_view);
        recent_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecentAdapter(getContext());
        adapter.bindRecycler(recent_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecentViewModel.class);
        mViewModel.getCallLog().observe(this, this::onChangedData);
        mViewModel.getNotify().observe(this, this::onNotifyChanged);
        mViewModel.loadCallLog();
    }

    public void onNotifyChanged(@Nullable Void aVoid) {
        mViewModel.getRecentData().observe(this, this::onChangedRecentCall);
    }

    public void onChangedRecentCall(@Nullable List<RecentCallLog> recentCallLogs) {
        mViewModel.analysisRecentCallLog(recentCallLogs);
    }

    private void onChangedData(List<RecentModel> recentCallLogs) {
        adapter.setModels(recentCallLogs);
    }

}
