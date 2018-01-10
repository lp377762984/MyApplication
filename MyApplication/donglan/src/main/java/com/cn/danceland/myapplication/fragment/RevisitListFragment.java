package com.cn.danceland.myapplication.fragment;

import android.view.View;

import com.cn.danceland.myapplication.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by shy on 2018/1/9 09:39
 * Email:644563767@qq.com
 * //回访人员列表
 */


public class RevisitListFragment extends BaseFragment {
    private PullToRefreshListView mListView;
  //  private List<RequestOrderListBean.Data.Content> datalist = new ArrayList<>();
  //  private MyListAatapter myListAatapter;
    private Gson gson = new Gson();
    private int mCurrentPage = 1;//起始请求页
    private boolean isEnd = false;
    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_revist_list, null);

        return v;
    }

    @Override
    public void initDta() {


    }

    @Override
    public void onClick(View view) {

    }
}
