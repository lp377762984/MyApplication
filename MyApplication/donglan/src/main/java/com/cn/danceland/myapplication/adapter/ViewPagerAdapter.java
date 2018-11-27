package com.cn.danceland.myapplication.adapter;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
	public Context context;
	public List<View> pgview;
	public int mCount;

	public ViewPagerAdapter(Context context, List<View> pgview) {
		this.pgview = pgview;
		this.context = context;
		mCount = pgview.size();
	}

	public void setData(List<View> pgview) {
		this.pgview = pgview;
		mCount = pgview.size();
	}

	@Override
	public int getCount() {
		return pgview.size();
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {

		if (position >= pgview.size()) {
			int newPosition = position % pgview.size();
			position = newPosition;
			mCount++;
		}
		if (position < 0) {
			position = -position;
			mCount--;
		}
		try {
			((ViewPager) collection).addView(pgview.get(position), 0);
		} catch (Exception e) {
		}
		return pgview.get(position);
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		int newPosition = 0 ;
		if (position >= pgview.size()) {
			if(pgview.size()!=0){
				newPosition = position % pgview.size();
			}
			position = newPosition;
		}
		if (position < 0) {
			position = -position;
		}else{
			collection.removeView((View)view);
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
