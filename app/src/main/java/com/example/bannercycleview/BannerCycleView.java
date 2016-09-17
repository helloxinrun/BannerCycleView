package com.example.bannercycleview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigRun on 2016/6/20.
 */
public class BannerCycleView extends RelativeLayout {
    private Context mContext;
    private ViewPager viewpage;
    private LinearLayout indicator_group;
    private ImageView[] indicators;

    private boolean isAutoCycle = true, isManualCycle = true;
    private long mCycleDelayed = 5000;
    private PagerAdapter bannerCycleAdapter;
    private List<View> views;

    private OnPageClickListener mOnPageClickListener;

    public BannerCycleView(Context context) {
        super(context);
        init(context);
    }

    public BannerCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        views = new ArrayList<View>();
        RelativeLayout banner_cycle_layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.banner_cycle_layout, this, true);
        viewpage = (ViewPager) banner_cycle_layout.findViewById(R.id.viewpage);
        indicator_group = (LinearLayout) banner_cycle_layout.findViewById(R.id.indicator_group);
    }

    /**
     * 设置循环时间
     *
     * @param mCycleDelayed
     */
    public void setmCycleDelayed(long mCycleDelayed) {
        this.mCycleDelayed = mCycleDelayed;
    }

    /**
     * 设置是否循环
     *
     * @param autoCycle
     */
    public void setAutoCycle(boolean autoCycle, boolean manualCycle) {
        this.isAutoCycle = autoCycle;
        this.isManualCycle = manualCycle;
    }

    /**
     * 添加所有view
     *
     * @param views
     */
    public void addAllView(List<View> views) {
        this.views = views;
        for (int i = 0; i < views.size(); i++) {
            final int position = i;
            views.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPageClickListener != null) {
                        mOnPageClickListener.onClick(position);
                    }
                }
            });
        }
        initIndicator();
        if (isAutoCycle || isManualCycle) {
            bannerCycleAdapter = new BannerCycleOpenAdapter(views);
            viewpage.setAdapter(bannerCycleAdapter);
            viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    int nowPosition = position % indicators.length;
                    showIndicators(nowPosition);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            //取中间数来作为起始位置
            int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % views.size());
            viewpage.setCurrentItem(index);
            if (isAutoCycle) {
                startBannerCycle();
            }
        } else {
            bannerCycleAdapter = new BannerCycleCloseAdapter(views);
            viewpage.setAdapter(bannerCycleAdapter);
            viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    int nowPosition = position;
                    showIndicators(nowPosition);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    private void initIndicator() {
        indicators = new ImageView[views.size()]; // 定义指示器数组大小
        for (int i = 0; i < views.size(); i++) {
            // 循环加入指示器,设置指示器的外间距
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.width = 50;
            lp.height = 50;
            lp.setMargins(10, 0, 10, 0);
            indicators[i] = new ImageView(mContext);
            indicators[i].setLayoutParams(lp);
            indicator_group.addView(indicators[i]);
        }
        showIndicators(0);
    }

    private void showIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            if (position == i) {
                indicators[i].setBackgroundResource(R.mipmap.indicators_now);
            } else {
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
        }
    }

    /**
     * 删除所有view
     */
    public void removeAllView() {
        views.clear();
        bannerCycleAdapter.notifyDataSetChanged();
    }

    public void setOnPageClickListener(OnPageClickListener listener) {
        mOnPageClickListener = listener;
    }

    public interface OnPageClickListener {
        void onClick(int position);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (viewpage != null) {
                viewpage.setCurrentItem(viewpage.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(0, mCycleDelayed);
            }
            return false;
        }
    });

    /**
     * 开始图片轮播
     */
    public void startBannerCycle() {
        handler.sendEmptyMessageDelayed(0, mCycleDelayed);
    }

    /**
     * 暂停图片轮播
     */
    public void stopBannerCycle() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (isAutoCycle) {
                startBannerCycle();
            }
        } else {
            if (isAutoCycle) {
                stopBannerCycle();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
