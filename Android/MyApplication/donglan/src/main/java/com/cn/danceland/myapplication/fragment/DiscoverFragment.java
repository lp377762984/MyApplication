package com.cn.danceland.myapplication.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.AddFriendsActivity;
import com.cn.danceland.myapplication.activity.PublishActivity;
import com.cn.danceland.myapplication.adapter.TabAdapter;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;
import com.viewpagerindicator.TabPageIndicator;

import static android.R.attr.value;

/**
 * A simple {@link Fragment} subclass.
 *
 * 发现页面
 */
public class DiscoverFragment extends BaseFragment implements DroppyMenuPopup.OnDismissCallback, DroppyClickCallbackInterface {

    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private TabAdapter mAdapter;
    private ImageButton iv_photo;


    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_discover, null);
        v.findViewById(R.id.iv_add_friends).setOnClickListener(this);

        iv_photo = v.findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(this);
        mViewPager =  v.findViewById(R.id.id_viewpager);
        mTabPageIndicator =  v.findViewById(R.id.id_indicator);
        mAdapter = new TabAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager, 0);

        return v;
    }

    @Override
    public void initDta() {

    }

    //弹出下拉框
    protected void showDroppyMenu() {
        droppyMenu.show();

    }

    DroppyMenuPopup droppyMenu;

    //绑定下拉框
    private void initDroppyMenu(ImageButton btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(mActivity, btn);
        droppyBuilder.addMenuItem(new DroppyMenuItem("发布动态"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("发布视频"))
                .setOnDismissCallback(this)
                .setOnClick(this)
                .setPopupAnimation(new DroppyFadeInAnimation())
                .triggerOnAnchorClick(false);

        droppyMenu = droppyBuilder.build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_friends://添加好友

                startActivity(new Intent(mActivity, AddFriendsActivity.class));
                break;
            case R.id.iv_photo://发布动态

                initDroppyMenu(iv_photo);
                showDroppyMenu();


                break;
            case value:
                break;
            default:
                break;
        }
    }

    /**
     * 下拉回调
     * @param v
     * @param id
     */
    @Override
    public void call(View v, int id) {


        switch (id) {
            case 0:
                //ToastUtils.showToastShort("发布动态");
                startActivity(new Intent(mActivity, PublishActivity.class));
                break;
            case 1:
                ToastUtils.showToastShort("发布视频");
                break;
            default:

        }


    }

    @Override
    public void call() {

    }
}
