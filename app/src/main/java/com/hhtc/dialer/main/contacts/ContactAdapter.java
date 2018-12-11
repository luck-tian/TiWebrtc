package com.hhtc.dialer.main.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.adapter.ContactItemDecoration;
import com.hhtc.dialer.main.holder.EmptyHolder;
import com.hhtc.dialer.view.DialerContactBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DialerContactBarView.IndexPressedListener {

    private static final String TAG = "ContactAdapter";
    public static final int LOADING_TYPE = 0;
    public static final int EMPTY_TYPE = 1;
    public static final int NORMAL_TYPE = 2;

    private int type = LOADING_TYPE;

    private Context context;

    private List<ContactModel> models = new ArrayList<>();

    private ContactItemDecoration contactItemDecoration;

    private LinearLayoutManager linearLayoutManager;

    private TextView tipsView;

    private ActionFloatingButton floatingButton;

    public ContactAdapter(Context context, ActionFloatingButton floatingButton) {
        this.context = context;
        models.add(null);
        this.floatingButton = floatingButton;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case LOADING_TYPE:
                holder = new EmptyHolder(inflater.inflate(R.layout.data_loading_layout, viewGroup, false));
                break;
            case EMPTY_TYPE:
                holder = new EmptyHolder(inflater.inflate(R.layout.contact_data_empty_layout, viewGroup, false));
                break;
            case NORMAL_TYPE:
                holder = new ContactViewHolder(inflater.inflate(R.layout.contact_item_layout, viewGroup, false));
                break;
            default:
                throw new RuntimeException("contact adapter view type unaware!!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ContactViewHolder) {
            ((ContactViewHolder) viewHolder).bindData(models.get(position));
        }
    }


    public void setModels(List<ContactModel> models) {
        if (Objects.isNull(models)||models.isEmpty()) {
            type = EMPTY_TYPE;
        } else {
            type = NORMAL_TYPE;
            this.models.remove(0);
            this.models.addAll(models);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void bindRecycler(RecyclerView recyclerView, LinearLayoutManager linearLayoutManager, DialerContactBarView contact_bar_view, TextView index_bar_tips) {
        this.linearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);
        contactItemDecoration = new ContactItemDecoration(recyclerView.getContext(), models);
        recyclerView.addItemDecoration(contactItemDecoration);
        recyclerView.setAdapter(this);
        contact_bar_view.setListener(this);
        this.tipsView = index_bar_tips;
    }


    private void showTips(String tips) {
        tipsView.setText(tips);
        tipsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onIndexPressed(int index, String text) {
        showTips(text);
        int position = getPosByTag(text);
        if (position != -1) {
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        }
        floatingButton.show();
    }

    private int getPosByTag(String text) {
        int result = -1;
        for (int i = 0; i < models.size(); i++) {
            ContactModel contactModle = models.get(i);
            if (!Objects.isNull(contactModle) && TextUtils.equals(models.get(i).getClassify(), text)) {
                result = i;
            }
        }
        return result;
    }


    @Override
    public void onMotionEventEnd() {
        tipsView.setVisibility(View.GONE);
        floatingButton.hind();
    }


}
