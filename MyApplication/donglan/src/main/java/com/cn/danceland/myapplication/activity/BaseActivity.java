package com.cn.danceland.myapplication.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.UIUtils;
import com.cn.danceland.myapplication.view.LoadingPager;

/**
 * @author wang
 * @version ����ʱ�䣺2015��7��8�� ����11:31:11 ��˵��
 */
public abstract class BaseActivity extends Activity {
	public LoadingPager loadingPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadingPage = new LoadingPager(UIUtils.getContext(),
				R.layout.loadpage_loading, R.layout.loadpage_error,
				R.layout.loadpage_empty) {
			@Override
			protected LoadResult load() {
				return BaseActivity.this.load();
			}
			@Override
			protected View createSuccessView() {
				return BaseActivity.this.createSuccessView();
			}
		};
		loadingPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingPage.show();
			}
		});
		loadingPage.show();
		setContentView(loadingPage);
	}

	/**
	 * ˢ��ҳ�湤��
	 * 
	 * @return
	 */
	protected abstract View createSuccessView();

	/**
	 * ��������� ��ȡ��ǰ״̬
	 * 
	 */
	protected abstract LoadingPager.LoadResult load();

}
