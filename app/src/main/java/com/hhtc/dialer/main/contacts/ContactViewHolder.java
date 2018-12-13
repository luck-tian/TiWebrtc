package com.hhtc.dialer.main.contacts;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.utils.intentUnits;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView contact_name;
    private ImageView contact_type;

    private ContactModel contactModel;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        contact_name = itemView.findViewById(R.id.contact_name);
        contact_type = itemView.findViewById(R.id.contact_type);
        itemView.setOnClickListener(this);
    }

    public void bindData(ContactModel contactModel) {
        this.contactModel = contactModel;
        contact_name.setText(contactModel.getDialerContact().getName());
        if (contactModel.getDialerContact().getType() == RecentCallLog.BLOCK_CHAIN) {
            contact_type.setVisibility(View.VISIBLE);
        } else {
            contact_type.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        intentUnits.startShowContact((Activity) contact_name.getContext(), contactModel.getDialerContact().getId());
    }
}
