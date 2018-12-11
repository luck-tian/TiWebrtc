package com.hhtc.dialer.main.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.RecentCallLog;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView contact_name;
    private ImageView contact_type;

    private ContactModel contactModle;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        contact_name = itemView.findViewById(R.id.contact_name);
        contact_type = itemView.findViewById(R.id.contact_type);
        itemView.setOnClickListener(this);
    }

    public void bindData(ContactModel contactModle) {
        this.contactModle = contactModle;
        contact_name.setText(contactModle.getDialerContact().getName());
        if (contactModle.getDialerContact().getType() == RecentCallLog.BLOCK_CHAIN) {
            contact_type.setVisibility(View.VISIBLE);
        } else {
            contact_type.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(contact_name.getContext(), contactModle.getDialerContact().getName(), Toast.LENGTH_SHORT).show();
    }
}
