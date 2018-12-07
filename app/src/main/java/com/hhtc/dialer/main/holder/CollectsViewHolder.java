package com.hhtc.dialer.main.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.CollectFavorite;

public class CollectsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView mName;

    private TextView firstChar;

    private ImageView speed;

    private CollectFavorite collectFavorite;

    public CollectsViewHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.name);
        firstChar = itemView.findViewById(R.id.first);
        speed = itemView.findViewById(R.id.speed);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        speed.setOnClickListener(speedAction);
    }

    public void bindData(CollectFavorite collectFavorite) {
        this.collectFavorite = collectFavorite;
        mName.setText(collectFavorite.getName());
        firstChar.setText(String.valueOf(collectFavorite.getName().charAt(0)));
    }

    private View.OnClickListener speedAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
