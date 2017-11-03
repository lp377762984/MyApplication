package com.cn.danceland.myapplication.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.SellCardActivity;
import com.cn.danceland.myapplication.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {

    GridView mGridView;
    String[] icon_name = {"健身圈", "私人教练", "会所动态", "在线售卡", "课程表", "会所商城", "意见反馈", "会所活动"};
    int[] icons = {R.drawable.img_jsq, R.drawable.img_srjl, R.drawable.img_hsdt, R.drawable.img_zxsk
            , R.drawable.img_kcb, R.drawable.img_hssc, R.drawable.img_yjfk, R.drawable.img_hshd};

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_shop, null);

        mGridView = v.findViewById(R.id.gridview);
        mGridView.setAdapter(new MyAdapter());
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        LinearLayout ll_top = v.findViewById(R.id.ll_top);
        ll_top.setBackgroundColor(Color.WHITE);
        ll_top.getBackground().setAlpha(80);
        v.findViewById(R.id.ibtn_call).setOnClickListener(this);
        v.findViewById(R.id.ibtn_gps).setOnClickListener(this);


        return v;
    }

    @Override
    public void initDta() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_call:

                showDialog();
                break;
            case R.id.ibtn_gps:

                ToastUtils.showToastShort("显示位置");

                break;
            default:
                break;
        }
    }


    /**
     * 提示
     */
    private void showDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("是否呼叫" + "010-12345678");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                call("010-12345678");
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    /**
     * 调用拨号功能
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }



    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ToastUtils.showToastShort(icon_name[i]);
            switch (i) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3://在线售卡
                    startActivity(new Intent(mActivity, SellCardActivity.class));
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;


                default:
                    break;
            }




        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icon_name.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return icons.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = View.inflate(mActivity, R.layout.gridview_item_shop, null);
            TextView tv_dcs = view.findViewById(R.id.tv_dcs);
            ImageView ibtn = view.findViewById(R.id.ibtn);
            tv_dcs.setText(icon_name[i]);

            ibtn.setBackgroundResource(icons[i]);
            return view;
        }
    }


}
