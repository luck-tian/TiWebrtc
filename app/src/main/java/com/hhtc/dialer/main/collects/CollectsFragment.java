package com.hhtc.dialer.main.collects;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhtc.dialer.R;
import com.hhtc.dialer.main.DialerFragment;

public class CollectsFragment extends DialerFragment {

    private ContactsViewModel viewModel;

    public static CollectsFragment newInstance() {
        return new CollectsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collects_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel=ViewModelProviders.of(this).get(ContactsViewModel.class);
        // TODO: Use the ViewModel
    }

}
