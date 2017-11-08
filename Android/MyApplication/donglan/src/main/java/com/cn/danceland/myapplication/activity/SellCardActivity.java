package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

/**
 * Created by shy on 2017/11/2 16:37
 * Email:644563767@qq.com
 */


public class SellCardActivity extends Activity implements View.OnClickListener {

    private String[] names = {"黄金年卡", "白金年卡", "钻石年卡"};
    private ImageView iv_fenlie;
    private LinearLayout ll_fenlie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card);
        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.listview);
        ll_fenlie =
                findViewById(R.id.ll_fenlie);
        ll_fenlie.setOnClickListener(this);
        iv_fenlie = findViewById(R.id.iv_fenlie);
        listView.setAdapter(new MyListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(SellCardActivity.this, SellCardConfirmActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fenlie://分类
                initDroppyMenu(ll_fenlie);
                showDroppyMenu();
                break;


            default:
                break;
        }
    }

    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //         LayoutInflater.from(SellCardActivity.this).inflate( R.layout.listview_item_club_card, null);
            view = LayoutInflater.from(SellCardActivity.this).inflate(R.layout.listview_item_club_card, null);


            return view;
        }
    }

    //弹出下拉框
    protected void showDroppyMenu() {
        droppyMenu.show();

    }

    DroppyMenuPopup droppyMenu;


    //绑定下拉框
    private void initDroppyMenu(View btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(SellCardActivity.this, btn);
        droppyBuilder.addMenuItem(new DroppyMenuItem("全部"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("年卡"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("季卡"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("月卡"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("限时活动"))
                .setOnDismissCallback(new DroppyMenuPopup.OnDismissCallback() {
                    @Override
                    public void call() {

                    }
                })
                .setOnClick(new DroppyClickCallbackInterface() {
                    @Override
                    public void call(View v, int id) {
                        switch (id) {
                            case 0:
                                ToastUtils.showToastShort("全部");
                                break;
                            case 1:
                                ToastUtils.showToastShort("年卡");
                                break;
                            case 2:
                                ToastUtils.showToastShort("季卡");
                                break;
                            case 3:
                                ToastUtils.showToastShort("月卡");
                                break;
                            case 4:
                                ToastUtils.showToastShort("限时活动");
                                break;
                            default:
                        }
                    }
                })
                .setPopupAnimation(new DroppyFadeInAnimation())
                .triggerOnAnchorClick(false);

        droppyMenu = droppyBuilder.build();
    }
}
