package com.hhtc.dialer.show;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.utils.intentUnits;
import com.hhtc.dialer.view.ContactShowInfoActionView;
import com.hhtc.dialer.view.ContactShowInfoView;

import java.util.Objects;

public class ShowContactInfoActivity extends AppCompatActivity implements Observer<Void> {

    public static final String LOAD_ID = "load_id_show_info";

    private ShowContactInViewModel viewModel;

    private DialerContact contact;

    private ImageView user_photo;

    private TextView username;

    private ContactShowInfoActionView phone, massage, mail, video;

    private ContactShowInfoView favorite_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact_info);
        initViewModel();
    }

    private void initViewModel() {

        user_photo = findViewById(R.id.user_photo);
        username = findViewById(R.id.username);

        favorite_image = findViewById(R.id.favorite_image);
        phone = findViewById(R.id.phone);
        massage = findViewById(R.id.massage);
        mail = findViewById(R.id.mail);
        video = findViewById(R.id.video);

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
        if (!Objects.isNull(contact))
            setData();
    }

    private void setData() {
        username.setText(contact.getName());
        phone.setText(String.format(getString(R.string.add_contact_phone_format), contact.getTel()));
        massage.setText(String.format(getString(R.string.add_contact_message_format), contact.getMessage()));
        mail.setText(String.format(getString(R.string.add_contact_mail_format), contact.getEmail()));
        video.setText(String.format(getString(R.string.add_contact_video_format), contact.getVideo()));

        favorite_image.bindFavorite(contact.isFavorite());
    }

    public void actionFinish(View view) {
        finish();
    }

    public void actionEdit(View view) {
        intentUnits.startAddContact(this, contact.getId());
    }

    public void showShare(View view) {

    }

    public void showFavorite(View view) {
        contact.setFavorite(!contact.isFavorite());
        favorite_image.bindFavorite(contact.isFavorite());
        viewModel.changeFavorite(contact);
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
