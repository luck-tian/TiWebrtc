package com.hhtc.dialer.add;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.view.AddContactInputView;

import java.util.Objects;

public class ContactAddOrEditActivity extends AppCompatActivity {

    public static final String CONTACT_MODEL_ID = "contact_model_id";

    private ImageView contact_img;

    private AddContactInputView name, phone, massage, mail, video;

    private DialerContact dialerContact;

    private AddContactViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add_or_edit);
        initView();
        setData();
    }


    private void initView() {
        contact_img = findViewById(R.id.contact_img);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        massage = findViewById(R.id.massage);
        mail = findViewById(R.id.mail);
        video = findViewById(R.id.video);
    }

    private void setData() {
        Objects.requireNonNull(name.getEditText()).addTextChangedListener(watcher);
        mViewModel = ViewModelProviders.of(this, new AddContactFactory(getApplicationContext())).get(AddContactViewModel.class);
        buildUrl();
    }

    private void buildUrl() {
        Intent intent = getIntent();
        int contact = intent.getIntExtra(CONTACT_MODEL_ID, 0);
        if (contact == 0) {
            dialerContact = new DialerContact();
        } else {
            mViewModel.getContact().observe(this, this::loadDialerContact);
            mViewModel.loadContact(contact);
        }
    }

    private void loadDialerContact(DialerContact contact) {
        Objects.requireNonNull(name.getEditText()).setText(contact.getName());
    }

    private AddContactInputWatcher watcher = new AddContactInputWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                textChanged(s.toString());
            }
        }
    };

    @SuppressLint("StringFormatInvalid")
    private void textChanged(String change) {
        Objects.requireNonNull(phone.getEditText()).setText(String.format(getString(R.string.add_contact_phone_format), change));
        Objects.requireNonNull(massage.getEditText()).setText(String.format(getString(R.string.add_contact_message_format), change));
        Objects.requireNonNull(mail.getEditText()).setText(String.format(getString(R.string.add_contact_mail_format), change));
        Objects.requireNonNull(video.getEditText()).setText(String.format(getString(R.string.add_contact_video_format), change));

        dialerContact.setName(change);
        dialerContact.setTel(change);
        dialerContact.setEmail(change);
        dialerContact.setMessage(change);
        dialerContact.setVideo(change);
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        if (!TextUtils.isEmpty(dialerContact.getName())) {
            mViewModel.save(dialerContact);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.add_contact_input_name_tips, Toast.LENGTH_SHORT).show();
        }

    }
}
