package com.hhtc.dialer.main.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hhtc.dialer.R;
import com.hhtc.dialer.adapter.ContactItemDecoration;
import com.hhtc.dialer.adapter.FavoriteItemDecoration;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.main.holder.CollectsViewHolder;
import com.hhtc.dialer.main.holder.EmptyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int LOADING_TYPE = 0;
    public static final int EMPTY_TYPE = 1;
    public static final int NORMAL_TYPE = 2;

    private int type = LOADING_TYPE;

    private Context context;

    private List<ContactModle> models = new ArrayList<>();

    private ContactItemDecoration contactItemDecoration;

    public ContactAdapter(Context context) {
        this.context = context;
        models.add(null);
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


    public void setModels(List<ContactModle> models) {
        if (Objects.isNull(models)) {
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

    public void bindRecycler(RecyclerView recyclerView) {
        contactItemDecoration = new ContactItemDecoration(recyclerView.getContext(), models);
        recyclerView.addItemDecoration(contactItemDecoration);
        recyclerView.setAdapter(this);
    }

}
