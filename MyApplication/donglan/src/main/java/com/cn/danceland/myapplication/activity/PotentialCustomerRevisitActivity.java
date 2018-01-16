package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.fragment.PotentialUpcomingMatterFragment;
import com.cn.danceland.myapplication.fragment.RevisitListFragment;
import com.cn.danceland.myapplication.fragment.RevisitListFragment1;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by shy on 2018/1/8 16:00
 * Email:644563767@qq.com
 * 潜客回访
 */


public class PotentialCustomerRevisitActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    public String[] TITLES = new String[]{"最近维护", "本月未维护", "未处理待办事项"};
    public  String[] UPCOMING_CONDITION = new String[]{"未处理待办事项", "已处理待办事项", "全部待办事项"};
    public String[] LIST_TYPE = new String[]{"最近维护", "最晚维护", "健身指数", "关注程度"};
    public String[] LIST_TYPE1 = new String[]{"待办", "已办", "全部"};
    private int untreated_num = 0;
    private ListPopup listPopup;
    private Gson gson = new Gson();
    private int current_page = 0;
    private SimplePagerTitleView simplePagerTitleView;
    private BadgePagerTitleView badgePagerTitleView;
    private MyListPopupViewAdapter myListPopupViewAdapter;
    private String auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_customer_revisit);
        EventBus.getDefault().register(this);
        initView();
        initData();

    }

    private void initData() {
        try {
            find_upcoming_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(IntEvent event) {
        LogUtil.i("" + event.getMsg());
        switch (event.getEventCode()) {
            case 150:
                if (untreated_num > 0) {
                    TextView badgeTextView = (TextView) LayoutInflater.from(this).inflate(R.layout.simple_count_badge_layout, null);
                    badgeTextView.setText("" + untreated_num);
                    badgePagerTitleView.setBadgeView(badgeTextView);
                } else {
                    badgePagerTitleView.setBadgeView(null);
                }

                //   commonNavigatorAdapter.notifyDataSetChanged();
                break;
            case 151:
                try {
                    find_upcoming_list();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }


    private void initView() {

        TextView tv_tiltle=findViewById(R.id.tv_tiltle);
        auth = getIntent().getStringExtra("auth");
        if (TextUtils.equals(auth,"2")){
            tv_tiltle.setText("会员维护");
        }
        findViewById(R.id.iv_back).setOnClickListener(this);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(myViewPagerAdapter);
        initMagicIndicator1();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current_page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        listPopup = new ListPopup(this);
    }


    CommonNavigatorAdapter commonNavigatorAdapter=new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return TITLES == null ? 0 : TITLES.length;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            badgePagerTitleView = new BadgePagerTitleView(context);

            simplePagerTitleView = new ColorTransitionPagerTitleView(context);
            simplePagerTitleView.setText(TITLES[index]);

            simplePagerTitleView.setNormalColor(Color.GRAY);
            simplePagerTitleView.setSelectedColor(Color.BLACK);
            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (index == 0 && current_page == 0) {
                        myListPopupViewAdapter.setData(LIST_TYPE);
                        listPopup.showPopupWindow(v);
                    }


                    if (index == 2 && current_page == 2) {
                        myListPopupViewAdapter.setData(UPCOMING_CONDITION);
                        listPopup.showPopupWindow(v);
                    }
                    mViewPager.setCurrentItem(index);
                    //      badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
                }
            });

            badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
//
//                // setup badge

            if (index == 2) {
                TextView badgeTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                badgeTextView.setText("" + untreated_num);
                badgePagerTitleView.setBadgeView(badgeTextView);
            }

            if (index == 2) {

                badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));

            }

            // don't cancel badge when tab selected
            badgePagerTitleView.setAutoCancelBadge(false);

            if (untreated_num == 0) {
                badgePagerTitleView.setBadgeView(null);
            }

            return badgePagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setColors(Color.parseColor("#40c4ff"));
            return indicator;
        }
    };

    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                break;

            default:
                break;
        }
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Bundle bundle = new Bundle();
            bundle.putString("auth", auth);

            if (arg0 == 0) {
                RevisitListFragment fragment = new RevisitListFragment();
                fragment.setArguments(bundle);
                return fragment;
            } else if (arg0 == 1) {
                RevisitListFragment1 fragment = new RevisitListFragment1();
                fragment.setArguments(bundle);
                return fragment;
            } else {
                PotentialUpcomingMatterFragment fragment = new PotentialUpcomingMatterFragment();
                fragment.setArguments(bundle);
                return fragment;
            }


        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

    }

    class StrBean {
        public String page;
        public String auth ;
        // public String member_id;
    }

    class RequsetSimpleBean1 {
        public boolean success;
        public int data;
        public String errorMsg;

        @Override
        public String toString() {
            return "RequsetSimpleBean{" +
                    "success=" + success +
                    ", data=" + data +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

    /**
     * 查询未待办数量
     *
     * @throws JSONException
     */
    public void find_upcoming_list() throws JSONException {

        StrBean strBean = new StrBean();
        strBean.auth=auth;
        //   strBean.page = pageCount - 1 + "";
        //  strBean.member_id = id;
        String s = gson.toJson(strBean);

        JSONObject jsonObject = new JSONObject(s.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.FIND_NOT_UPCOMINGMATTER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequsetSimpleBean1 requsetSimpleBean = new RequsetSimpleBean1();
                requsetSimpleBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean1.class);
                Message message = Message.obtain();
                message.what = 1;

                //LogUtil.i(requsetSimpleBean.toString());
                if (requsetSimpleBean.isSuccess()) {
                    untreated_num = requsetSimpleBean.data;
                    //handler.sendMessage(message);
                    // initMagicIndicator1();
                    EventBus.getDefault().post(new IntEvent(requsetSimpleBean.data, 150));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showToastShort(volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }


    public class ListPopup extends BasePopupWindow {
        private Context context;

        private View popupView;
        private ListView listView;


        public ListPopup(Activity context) {
            super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setAutoLocatePopup(true);
            bindEvent();

            this.context = context;
        }

        //
        @Override
        protected Animation initShowAnimation() {
            TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -DensityUtils.dp2px(getContext(), 350f), 0);
            translateAnimation.setDuration(450);
            translateAnimation.setInterpolator(new OvershootInterpolator(1));

            return translateAnimation;
        }

        //
        @Override
        protected Animation initExitAnimation() {
            TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -DensityUtils.dp2px(getContext(), 350f));
            translateAnimation.setDuration(450);
            translateAnimation.setInterpolator(new OvershootInterpolator(-4));
            return translateAnimation;
        }


        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {
            popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_list_menu, null);
            return popupView;
        }

        @Override
        public View initAnimaView() {


           // return findViewById(R.id.popup_contianer);
            return null;
        }

        private void bindEvent() {
            if (popupView != null) {
                listView = popupView.findViewById(R.id.listview);
                myListPopupViewAdapter = new MyListPopupViewAdapter(UPCOMING_CONDITION);
                listView.setAdapter(myListPopupViewAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (current_page == 2) {
                            if (i == 0) {//查询未处理
                                EventBus.getDefault().post(new IntEvent(0, 152));
                                TITLES[2]="未处理待办事项";
                            } else if (i == 1) {//查询已处理
                                TITLES[2]="已处理待办事项";
                                EventBus.getDefault().post(new IntEvent(0, 153));
                            } else {//查询全部
                                TITLES[2]="全部待办事项";
                                EventBus.getDefault().post(new IntEvent(0, 154));
                            }

                        }
                        if (current_page==0){

                            if (i == 0) {//最近
                                EventBus.getDefault().post(new IntEvent(0, 162));
                                TITLES[0]="最近维护";
                            } else if (i == 1) {//最晚
                                EventBus.getDefault().post(new IntEvent(0, 163));
                                TITLES[0]="最晚维护";
                            } else if (i==2){//健身指数
                                TITLES[0]="健身指数";
                                EventBus.getDefault().post(new IntEvent(0, 164));
                            } else if (i==3){//关注程度
                                TITLES[0]="关注程度";
                                EventBus.getDefault().post(new IntEvent(0, 165));
                            }


                        }
                        commonNavigatorAdapter.notifyDataSetChanged();


                        listPopup.dismiss();
                    }
                });
            }

        }


    }


    class MyListPopupViewAdapter extends BaseAdapter {
        String[] data = new String[]{};

        public MyListPopupViewAdapter(String[] data) {
            this.data = data;
        }

        public void setData(String[] data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(PotentialCustomerRevisitActivity.this, R.layout.listview_item_popup, null);

            TextView tv_cardname = view.findViewById(R.id.tv_cardname);
//            if (i == 0) {
//                tv_cardname.setText("全部");
//            } else {
//                tv_cardname.setText(cardTypeData.get(i - 1).getName());
//            }
            tv_cardname.setText(data[i]);

            return view;
        }
    }


}