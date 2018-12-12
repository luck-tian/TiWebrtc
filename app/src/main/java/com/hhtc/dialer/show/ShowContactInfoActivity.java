package com.hhtc.dialer.show;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.DialerContact;

public class ShowContactInfoActivity extends AppCompatActivity implements Observer<Void> {

    public static final String LOAD_ID = "load_id_show_info";

    private ShowContactInViewModel viewModel;

    private DialerContact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact_info);
        initViewModel();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ShowContactInViewModel.class);
        viewModel.getNotify().observe(this, this);
        viewModel.loadContact(buildUrlContactId());
    }

    private long buildUrlContactId() {
        return getIntent().getLongExtra(LOAD_ID, -1);
    }


    @Override
    public void onChanged(@Nullable Void aVoid) {
        viewModel.getContactLiveData().observe(this, this::loadSuccess);
    }

    private void loadSuccess(DialerContact contact) {
        this.contact = contact;

    }

    public void actionFinish(View view) {
        finish();
    }

    public void actionEdit(View view) {

    }

    public void showShare(View view) {

    }

    public void showFavorite(View view) {

    }

    public void showBlock(View view) {

    }


    public void phoneAction(View view) {

    }

    public void massageAction(View view) {

    }

    public void mailAction(View view) {

    }

    public void videoAction(View view) {

    }


}
