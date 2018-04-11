package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.PhysicalTestBean;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestion;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionCond;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionRequest;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.ViewPagerNoSlide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/4/8.
 */

public class PhysicalTestActivity extends Activity {

    DongLanTitleView physical_title;
    ViewPagerNoSlide vp_physical;
    private BcaQuestionRequest request;
    private Gson gson;
    List<BcaQuestion> list;
    ArrayList<View> viewList;
    View inflate;
    TextView tv_zhubiaoti;
    List<PhysicalTestBean> datalist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physicaltest);
        initHost();
        initView();
        queryList();
    }

    private void initViewPager() {

        if(list!=null){
            datalist.clear();
            for(int i = 0;i<list.size();i++){
                String test_content = list.get(i).getTest_content();
                PhysicalTestBean physicalTestBean = gson.fromJson(test_content, PhysicalTestBean.class);
                datalist.add(physicalTestBean);
            }
        }

        vp_physical.setAdapter(new PhysicalPagerAdapter(viewList,datalist));

    }

    private void initHost() {
        request = new BcaQuestionRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        list = new ArrayList<>();
        datalist = new ArrayList<>();
    }

    private void initView() {

        physical_title = findViewById(R.id.physical_title);
        physical_title.setTitle("体能测试");

        vp_physical = findViewById(R.id.vp_physical);
        vp_physical.setScroll(false);


        //inflate = LayoutInflater.from(PhysicalTestActivity.this).inflate(R.layout.physical_detail, null);
        //tv_zhubiaoti = inflate.findViewById(R.id.tv_zhubiaoti);

        viewList = new ArrayList<>();

    }


    /**
     * @方法说明:按条件查询问题题干列表
     **/
    public void queryList() {
        BcaQuestionCond cond = new BcaQuestionCond();
        cond.setType(Byte.valueOf("3"));
        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<BcaQuestion>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<BcaQuestion>>>() {
                }.getType());
                list.clear();
                if (result.isSuccess()) {
                    list = result.getData();
                    for(int i = 0;i<list.size();i++){
                        viewList.add(LayoutInflater.from(PhysicalTestActivity.this).inflate(R.layout.physical_detail, null));
                    }
                    if(viewList!=null&&viewList.size()>0){
                        initViewPager();
                    }

                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }

    private class PhysicalPagerAdapter extends PagerAdapter{
        ArrayList<View> viewList;
        List<PhysicalTestBean> datalist;
        PhysicalPagerAdapter(ArrayList<View> viewList,List<PhysicalTestBean> datalist){
            this.viewList = viewList;
            this.datalist = datalist;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub
            View v=viewList.get(position);
            ViewGroup parent = (ViewGroup) v.getParent();
            //Log.i("ViewPaperAdapter", parent.toString());
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(v);
            PhysicalTestBean physicalTestBean = datalist.get(position);

            ImageView img_01 = v.findViewById(R.id.img_01);
            Glide.with(PhysicalTestActivity.this).load(physicalTestBean.getMain_pic_url()).into(img_01);

            TextView  tv_zhubiaoti = v.findViewById(R.id.tv_zhubiaoti);
            tv_zhubiaoti.setText(physicalTestBean.getMain_title());

            TextView tv_fubiaoti = v.findViewById(R.id.tv_fubiaoti);
            tv_fubiaoti.setText(physicalTestBean.getSecond_title());

            TextView tv_buzhou = v.findViewById(R.id.tv_buzhou);
            ImageView img_02 = v.findViewById(R.id.img_02);
            ImageView img_03 = v.findViewById(R.id.img_03);
            ImageView img_04 = v.findViewById(R.id.img_04);
            ImageView[] arrImg = {img_02,img_03,img_04};
            TextView tv_buzhou_01 = v.findViewById(R.id.tv_buzhou_01);
            TextView tv_buzhou_02 = v.findViewById(R.id.tv_buzhou_02);
            TextView tv_buzhou_03 = v.findViewById(R.id.tv_buzhou_03);
            TextView[] arrText = {tv_buzhou_01,tv_buzhou_02,tv_buzhou_03};
            List<PhysicalTestBean.Action_detail> action_detail = physicalTestBean.getAction_detail();
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder stringBuilder1 = new StringBuilder();
            if(action_detail!=null){
                for(int j = 0;j<action_detail.size();j++){
                    stringBuilder.append((j+1)+"."+action_detail.get(j).getNote()+"\n");
                    Glide.with(PhysicalTestActivity.this).load(action_detail.get(j).getPic_url()).into(arrImg[j]);
                    arrText[j].setText(action_detail.get(j).getNote());
                }
                tv_buzhou.setText(stringBuilder.toString());
            }

            TextView tv_zhuyi = v.findViewById(R.id.tv_zhuyi);
            List<String> attention = physicalTestBean.getAttention();
            if(attention!=null){
                for(int n = 0 ;n<attention.size();n++){
                    stringBuilder1.append((n+1)+"."+attention.get(n)+"\n");

                }
                tv_zhuyi.setText(stringBuilder1.toString());
            }




            Button bt_next= v.findViewById(R.id.bt_next);
            bt_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position<viewList.size()-1){
                        vp_physical.setCurrentItem(position+1);
                    }

                }
            });

            return v;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
            container.removeView(viewList.get(position));
        }

    }

}
