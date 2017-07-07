package com.xiao.noticeview.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiao.noticeview.R;

import java.util.List;

/**
 * Created by xiaoyanger on 2017/1/14.
 * 轮播公告Veiw
 */
public class NoticeView extends ViewFlipper implements View.OnClickListener {

    private Context mContext;
    private List<String> mNotices;

    public NoticeView(Context context) {
        super(context);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        // 轮播间隔时间为3s
        setFlipInterval(3000);
        // 内边距5dp
        setPadding(dp2px(5f), dp2px(5f), dp2px(5f), dp2px(5f));
        // 设置enter和leave动画
        setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.notify_in));
        setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.notify_out));
    }

    /**
     * 添加需要轮播展示的公告
     *
     * @param notices
     */
    public void addNotice(List<String> notices) {
        removeAllViews();
        mNotices = notices;
        for (int i = 0; i < notices.size(); i++) {
            // 根据公告内容构建一个Tetview
            String notice = notices.get(i);
            TextView textView = new TextView(mContext);
            textView.setSingleLine();
            textView.setText(notice);
            textView.setTextSize(13f);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            // 将公告的位置设置为textView的tag方便点击是回调给用户
            textView.setTag(i);
            textView.setOnClickListener(this);
            // 添加到ViewFlipper
            NoticeView.this.addView(textView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        String notice = mNotices.get(position);
        if (mOnNoticeClickListener != null) {
            mOnNoticeClickListener.onNotieClick(position, notice);
        }
    }

    /**
     * 通知点击监听接口
     */
    public interface OnNoticeClickListener {
        void onNotieClick(int position, String notice);
    }

    private OnNoticeClickListener mOnNoticeClickListener;

    /**
     * 设置通知点击监听器
     *
     * @param onNoticeClickListener 通知点击监听器
     */
    public void setOnNoticeClickListener(OnNoticeClickListener onNoticeClickListener) {
        mOnNoticeClickListener = onNoticeClickListener;
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                mContext.getResources().getDisplayMetrics());
    }
}
