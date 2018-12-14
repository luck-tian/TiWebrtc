package com.hhtc.dialer.main.contacts;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.utils.intentUnits;
import com.hhtc.dialer.view.RemoveView;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RemoveView.SwipeListener {

    private static final String TAG = "ContactViewHolder";
    private TextView contact_name;
    private ImageView contact_type;

    private ContactModel contactModel;

    private RemoveView itemView;

    private Map<Integer, RemoveView> mapLayouts;

    private boolean isDrag;

    private Consumer<ContactModel> consumer;

    public ContactViewHolder(@NonNull View itemView, Map<Integer, RemoveView> mapLayouts, Consumer<ContactModel> consumer) {
        super(itemView);
        contact_name = itemView.findViewById(R.id.contact_name);
        contact_type = itemView.findViewById(R.id.contact_type);
        itemView.findViewById(R.id.item_view).setOnClickListener(this);
        itemView.findViewById(R.id.delete_view).setOnClickListener(this::deleteView);
        this.itemView = (RemoveView) itemView;
        this.itemView.setSwipeListener(this);
        this.mapLayouts = mapLayouts;
        this.consumer = consumer;
    }

    public void bindData(ContactModel contactModel) {
        this.contactModel = contactModel;
        contact_name.setText(contactModel.getDialerContact().getName());
        if (contactModel.getDialerContact().getType() == RecentCallLog.BLOCK_CHAIN) {
            contact_type.setVisibility(View.VISIBLE);
        } else {
            contact_type.setVisibility(View.GONE);
        }

        for (RemoveView vi : mapLayouts.values()) {
            if (Objects.nonNull(vi)) {
                vi.close(true);
            }
        }
    }

    public void deleteView(View v) {
        consumer.accept(contactModel);
        itemView.close(true);
        //删除
        Injection.provideTasksRepository(v.getContext()).deleteContact(contactModel.getDialerContact());
    }

    @Override
    public void onClick(View v) {
        intentUnits.startShowContact((Activity) contact_name.getContext(), contactModel.getDialerContact().getId());
    }

    @Override
    public void onClosed(RemoveView view) {
        itemView.findViewById(R.id.item_view).setClickable(true);
        mapLayouts.remove(view.hashCode());
        isDrag = false;
    }

    @Override
    public void onOpened(RemoveView view) {
        itemView.findViewById(R.id.item_view).setClickable(false);
        mapLayouts.put(view.hashCode(), view);
        isDrag = false;
    }

    @Override
    public void onSlide(RemoveView view, float slideOffset) {
        if (!isDrag) {
            isDrag = true;
            for (RemoveView vi : mapLayouts.values()) {
                if (Objects.nonNull(vi)) {
                    vi.close(true);
                }
            }
        }
    }
}
