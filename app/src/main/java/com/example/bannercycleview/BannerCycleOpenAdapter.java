package com.example.bannercycleview;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigRun on 2016/6/21.
 */
public class BannerCycleOpenAdapter extends PagerAdapter {

    private List<View> views;

    public BannerCycleOpenAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //因为instantiateItem中已经有删除功能了所以这里就不用添加了。
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position % views.size());
        if (view.getParent() != null) {
            container.removeView(view); //一个容器中只能放一个view，所以这里之前已经存在的话要先删除
        }
        container.addView(view);
        return view;
    }
}
