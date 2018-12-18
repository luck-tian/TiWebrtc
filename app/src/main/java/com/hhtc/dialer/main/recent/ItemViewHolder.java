package com.hhtc.dialer.main.recent;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.utils.TimeUtils;

/**
 * 通话记录 holder
 */
public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView call_name;
    private TextView call_duration;

    private TextView tel_number;

    private ImageView photo;
    private ImageView call_type;

    private RecentModel recentModel;

    private Context context;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.context = itemView.getContext().getApplicationContext();
        photo = itemView.findViewById(R.id.photo);
        call_name = itemView.findViewById(R.id.call_name);
        call_type = itemView.findViewById(R.id.call_type);
        tel_number = itemView.findViewById(R.id.tel_number);
        call_duration = itemView.findViewById(R.id.call_duration);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bindData(RecentModel recentModel) {
        this.recentModel = recentModel;
        call_name.setText(recentModel.getCallLog().getName());
        tel_number.setText(recentModel.getCallLog().getTel());

        //设置通话类型图标
        switch (recentModel.getCallLog().getCallType()) {
            case RecentCallLog
                    .INCOMING_TYPE:
                //呼入
                call_type.setImageResource(R.drawable.telephone_call_in_icon);
                call_name.setTextColor(context.getColor(R.color.dialer_recent_item_name_color_call));
                break;
            case RecentCallLog
                    .OUTGOING_TYPE:
                //呼出
                call_type.setImageResource(R.drawable.telephone_call_out_icon);
                call_name.setTextColor(context.getColor(R.color.dialer_recent_item_name_color_call));
                break;
            case RecentCallLog
                    .MISSED_TYPE:
                //未接
                call_type.setImageResource(R.drawable.telephone_call_not_answer_icon);
                call_name.setTextColor(context.getColor(R.color.dialer_recent_item_name_color_not_answer));
                break;
        }

        //设置not save
        if (TextUtils.isEmpty(recentModel.getCallLog().getName())) {
            call_name.setText(recentModel.getCallLog().getTel());
            tel_number.setText(R.string.dialer_recent_item_call_not_save);
        } else {
            call_name.setText(recentModel.getCallLog().getName());
            tel_number.setText(recentModel.getCallLog().getTel());
        }

        call_duration.setText(TimeUtils.millis2String(recentModel.getCallLog().getCallTime(),new SimpleDateFormat("mm:ss")));

        if (recentModel.getCallLog().isTradition()) {
            photo.setImageResource(R.drawable.telephone_call_head_portrait_traditional_test);
        } else {
            photo.setImageResource(R.drawable.telephone_call_head_portrait_node_test);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {

        return false;
    }
}
