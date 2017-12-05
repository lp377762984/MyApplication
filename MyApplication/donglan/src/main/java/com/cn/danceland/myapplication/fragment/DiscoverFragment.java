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
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;
import com.viewpagerindicator.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;

import static android.R.attr.value;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 发现页面
 */
public class DiscoverFragment extends BaseFragment implements DroppyMenuPopup.OnDismissCallback, DroppyClickCallbackInterface {

    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private TabAdapter mAdapter;
    private ImageButton iv_photo;

    public int curentpage = 0;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_discover, null);
        v.findViewById(R.id.iv_add_friends).setOnClickListener(this);

        iv_photo = v.findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(this);
        mViewPager = v.findViewById(R.id.id_viewpager);
        mTabPageIndicator = v.findViewById(R.id.id_indicator);
        mAdapter = new TabAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager, 0);
        mTabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curentpage=position;
                EventBus.getDefault().post(new IntEvent(position, 8901));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            LogUtil.i("fragment获取焦点");
        } else {
            //相当于Fragment的onPause
            LogUtil.i("fragment失去焦点");
        }
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
     *
     * @param v
     * @param id
     */
    @Override
    public void call(View v, int id) {


        switch (id) {
            case 0:
                //ToastUtils.showToastShort("发布动态");
                Intent intent = new Intent(mActivity, PublishActivity.class);
                intent.putExtra("isPhoto", "0");
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(mActivity, PublishActivity.class);
                intent1.putExtra("isPhoto", "1");
                startActivity(intent1);
                //ToastUtils.showToastShort("发布视频");
                break;
            default:

        }


    }

    @Override
    public void call() {

    }
}
