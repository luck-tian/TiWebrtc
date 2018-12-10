package com.hhtc.dialer.main.recent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hhtc.dialer.R;

public class HeadViewHolder extends RecyclerView.ViewHolder {

    private TextView time_classification;

    private RecentModel recentModel;

    public HeadViewHolder(@NonNull View itemView) {
        super(itemView);
        time_classification = itemView.findViewById(R.id.time_classification);
    }

    public void bindData(RecentModel recentModel) {
        this.recentModel = recentModel;
        time_classification.setText(recentModel.getTime());
    }

}
