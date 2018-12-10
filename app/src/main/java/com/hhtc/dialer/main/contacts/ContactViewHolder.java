package com.hhtc.dialer.main.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.main.recent.RecentModel;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    private TextView contact_name;
    private ImageView contact_type;

    private ContactModle contactModle;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        contact_name = itemView.findViewById(R.id.contact_name);
        contact_type = itemView.findViewById(R.id.contact_type);
    }

    public void bindData(ContactModle contactModle) {
        this.contactModle = contactModle;
        contact_name.setText(contactModle.getDialerContact().getName());
        if (contactModle.getDialerContact().getType() == RecentCallLog.BLOCK_CHAIN) {
            contact_type.setVisibility(View.VISIBLE);
        } else {
            contact_type.setVisibility(View.GONE);
        }
    }

}
