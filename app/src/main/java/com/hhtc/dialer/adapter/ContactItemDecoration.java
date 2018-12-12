package com.hhtc.dialer.adapter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.hhtc.dialer.R;
import com.hhtc.dialer.main.contacts.ContactModel;
import com.hhtc.dialer.utils.LogUtil;

import java.util.List;
import java.util.Objects;

public class ContactItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "ContactItemDecoration";

    private static final int ITEM_SIZE = 3;
    private static final int ITEM_SIZE_PADDING = 10;
    private static final int TITLE_SIZE = 16;
    private Paint mPaint;
    private int mItemSize;
    private int mItemPadding;
    private TextPaint mTextPaint;
    private Rect mBounds;
    private int mTitleSize;
    private List<ContactModel> models;
    private Context context;

    public ContactItemDecoration(Context context, List<ContactModel> models) {
        this.models = models;
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getColor(R.color.dialer_main_tab_layout_item_normal));
        mPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mItemSize = (int) TypedValue.applyDimension(ITEM_SIZE, TypedValue.COMPLEX_UNIT_DIP, displayMetrics);
        mTitleSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TITLE_SIZE, displayMetrics);
        mItemPadding = mItemSize * ITEM_SIZE_PADDING;
        mBounds = new Rect();
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTitleSize);
        mTextPaint.setAntiAlias(true);
        LogUtil.i(TAG, "ContactItemDecoration: mItemPadding:" + mItemPadding);
        LogUtil.i(TAG, "ContactItemDecoration: mTitleSize:" + mTitleSize);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();

        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            if (Objects.isNull(currentModel(i))) {
                continue;
            }


            //画线规则
            drawLine(canvas, parent, left, right, i);

            if (position == 0) {
                drawTitle(canvas, position, child, params);
            } else if (hasLastModel(position) && !TextUtils.equals(lastModel(position).getClassify(), currentModel(position).getClassify())) {
                drawTitle(canvas, position, child, params);
            }


        }
    }


    /**
     * 画title
     *
     * @param canvas
     * @param position
     * @param child
     * @param params
     */
    private void drawTitle(@NonNull Canvas canvas, int position, View child, RecyclerView.LayoutParams params) {
        mTextPaint.setColor(context.getColor(R.color.dialer_contact_item_name_tips_color));
        mTextPaint.getTextBounds(currentModel(position).getClassify(), 0, currentModel(position).getClassify().length(), mBounds);
        canvas.drawText(currentModel(position).getClassify(), child.getPaddingLeft() + mItemPadding, child.getTop() - params.topMargin - (mTitleSize / 2 - mBounds.height() / 2) + (mItemSize*2), mTextPaint);
    }


    private boolean hasLastModel(int position) {
        return (models.size() > (position - 1)) &&
                (position - 1) >= 0;
    }

    private ContactModel lastModel(int position) {
        return models.get(position - 1);
    }

    private ContactModel currentModel(int position) {
        return models.get(position);
    }

    /**
     * 划线
     *
     * @param canvas
     * @param parent
     * @param left
     * @param right
     * @param i
     */
    private void drawLine(@NonNull Canvas canvas, @NonNull RecyclerView parent, int left, int right, int i) {
        final View child = parent.getChildAt(i);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int top = child.getBottom() + layoutParams.bottomMargin;
        final int bottom = top + mItemSize;
        canvas.drawRect(left + mItemPadding, top, right, bottom, mPaint);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //设置好距离

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (!Objects.isNull(currentModel(position))) {
            if (position == 0) {
                outRect.set(mItemPadding, mItemSize * ITEM_SIZE_PADDING, 0, mItemSize * 2);
            } else if (hasLastModel(position) && TextUtils.equals(lastModel(position).getClassify(), currentModel(position).getClassify())) {
                outRect.set(mItemPadding, mItemSize, 0, mItemSize * 2);
            } else if (hasLastModel(position) && !TextUtils.equals(lastModel(position).getClassify(), currentModel(position).getClassify())) {
                outRect.set(mItemPadding, mItemSize * ITEM_SIZE_PADDING, 0, mItemSize * 2);
            } else {
                outRect.set(mItemPadding, mItemSize, 0, mItemSize * 2);
            }
        }
    }


}