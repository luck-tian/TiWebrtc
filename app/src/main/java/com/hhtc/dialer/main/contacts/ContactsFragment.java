package com.hhtc.dialer.main.contacts;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.main.DialerFragment;
import com.hhtc.dialer.main.FloatingViewModel;
import com.hhtc.dialer.view.DialerContactBarView;

import java.util.List;
import java.util.Objects;


public class ContactsFragment extends DialerFragment implements ActionFloatingButton {

    private static final String TAG = "ContactsFragment";
    private ContactsViewModel mViewModel;

    private RecyclerView content_contact;

    private ContactAdapter adapter;

    private DialerContactBarView contact_bar_view;

    private TextView index_bar_tips;

    private FloatingViewModel mSharedViewModel;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content_contact = view.findViewById(R.id.content_contact);
        contact_bar_view = view.findViewById(R.id.contact_bar_view);
        index_bar_tips = view.findViewById(R.id.index_bar_tips);
        adapter = new ContactAdapter(getContext(), this);
        adapter.bindRecycler(content_contact, new LinearLayoutManager(getContext()), contact_bar_view, index_bar_tips);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new ContactFactory(this)).get(ContactsViewModel.class);
        mSharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(FloatingViewModel.class);
        mViewModel.getContacts().observe(this, this::onChangedData);
        mViewModel.loadContact();
    }


    private void onChangedData(List<ContactModel> dialerContacts) {
        adapter.setModels(dialerContacts);
    }

    @Override
    public void onPagerSelectMove() {
        adapter.itemSwipeClose();
    }

    @Override
    public void show() {
        mSharedViewModel.setAction(true);
    }

    @Override
    public void hind() {
        mSharedViewModel.setAction(false);
    }
}
