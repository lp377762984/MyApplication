package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.DrivingRouteOverlay;
import com.cn.danceland.myapplication.utils.LocationService;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MassTransitRouteOverlay;
import com.cn.danceland.myapplication.utils.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/11/1.
 */

public class MapActivity extends Activity {
    MapView mapview;
    PoiSearch poiSearch;
    GeoCoder geoCoder;
    double latitude;//纬度
    double longitude;//经度
    double myLatitude;
    double myLongitude;
    LatLng location1,myLocation;
    PlanNode stNode;
    BaiduMap map;
    RoutePlanSearch routePlanSearch;
    PlanNode enNode;
    String addressstr;
    LatLng startlatLng;
    LatLng finishlatLng,midLng;
    double distance;
    String walkTime,busTime,carTime,timeStr="";
    int lv;
    ImageView img_walk_grey,img_walk_blue,bus_gray,bus_blue,car_gray,car_blue,back_gray;
    List<OverlayOptions> carList,busList,walkList;
    TextView tv_walk,tv_bus,tv_car;
    List<List<MassTransitRouteLine.TransitStep>> allSteps;
    TextView tv_station;
    String linesInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initHost();
        intiView();
        initLines();
        setOnClick();
        showColor("walk");
    }

    private void initHost() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(routePlanListener);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            myLongitude = Double.valueOf(extras.getString("jingdu"));
            myLatitude = Double.valueOf(extras.getString("weidu"));
            latitude = Double.valueOf(extras.getString("shopWeidu"));
            longitude = Double.valueOf(extras.getString("shopJingdu"));
        }

        startlatLng = new LatLng(myLatitude,myLongitude);
        finishlatLng = new LatLng(latitude,longitude);
        stNode = PlanNode.withLocation(startlatLng);
        enNode = PlanNode.withLocation(finishlatLng);

        midLng = new LatLng(myLatitude+(latitude-myLatitude)/2,myLongitude+(longitude-myLongitude)/2);

        distance = DistanceUtil.getDistance(startlatLng, finishlatLng);
        lv = getZoomLevel(distance);
    }

    private void setOnClick() {
        img_walk_grey.setOnClickListener(onclick);
        bus_gray.setOnClickListener(onclick);
        car_gray.setOnClickListener(onclick);
        back_gray.setOnClickListener(onclick);
        tv_walk.setOnClickListener(onclick);
        tv_bus.setOnClickListener(onclick);
        tv_car.setOnClickListener(onclick);
    }

    public void showInfo(){
        tv_station.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        if(allSteps!=null&&allSteps.size()>0){
            for (int i = 0;i<allSteps.size();i++){
                List<MassTransitRouteLine.TransitStep> transitSteps = allSteps.get(i);
                if(transitSteps.get(0).getBusInfo()!=null){
                    String string = transitSteps.get(0).getBusInfo().getName();
                    sb.append(string+"→");
                }
            }
            linesInfo = sb.toString().substring(0,sb.toString().length()-1);
        }
        tv_station.setText(linesInfo);
    }

    private void intiView() {
        mapview = findViewById(R.id.mapview);
        map = mapview.getMap();
        map.setMyLocationEnabled(true);
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(lv));
        map.setMapStatus(MapStatusUpdateFactory.newLatLng(midLng));

        img_walk_grey = findViewById(R.id.img_walk_grey);
        img_walk_blue = findViewById(R.id.img_walk_blue);
        bus_gray = findViewById(R.id.bus_gray);
        bus_blue = findViewById(R.id.bus_blue);
        car_gray = findViewById(R.id.car_gray);
        car_blue = findViewById(R.id.car_blue);

        back_gray = findViewById(R.id.back_gray);


        tv_walk = findViewById(R.id.tv_walk);
        tv_bus = findViewById(R.id.tv_bus);
        tv_car = findViewById(R.id.tv_car);

       tv_station = findViewById(R.id.tv_station);

    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_walk_grey:
                    map.clear();
                    showColor("walk");
                    onFoot();
                    break;
                case R.id.bus_gray:
                    map.clear();
                    showColor("bus");
                    byBus();
                    showInfo();
                    break;
                case R.id.car_gray:
                    map.clear();
                    showColor("car");
                    drive();
                    break;
                case R.id.back_gray:
                    finish();
                    break;
                case R.id.tv_walk:
                    map.clear();
                    showColor("walk");
                    onFoot();
                    break;
                case R.id.tv_bus:
                    map.clear();
                    showColor("bus");
                    byBus();
                    showInfo();
                    break;
                case R.id.tv_car:
                    map.clear();
                    showColor("car");
                    drive();
                    break;

            }
        }
    };

    /**
     * 获取缩放级别
     * @param distance 2点之间距离
     * @return int zoomLevel
     */
    public int getZoomLevel(double distance) {
        int[] zoomLevel = { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6,5, 4, 3 };
        String[] zoomLevelStr = {"10", "20", "50", "100", "200", "500", "1000", "2000", "5000", "10000", "20000", "25000", "50000", "100000",
                                "200000", "500000", "1000000", "2000000"}; // 单位/m
        int mid = (int) (distance/10);
        for (int i = 0; i < zoomLevelStr.length; i++) {
            if (i < zoomLevelStr.length - 1) {
                int left = Integer.valueOf(zoomLevelStr[i]);
                int right = Integer.valueOf(zoomLevelStr[i + 1]);
                if (mid < left) {
                    return zoomLevel[i];
                } else if (mid > left && mid < right) {
                    return zoomLevel[i + 1];
                }
            } else {
                return 3;
            }
        }
        return 18;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapview.onDestroy();
        routePlanSearch.destroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapview.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapview.onPause();
    }

    private void initLines(){
        routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
        routePlanSearch.masstransitSearch(new MassTransitRoutePlanOption().from(stNode).to(enNode));
        routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));

    }


    private void onFoot(){
        map.addOverlays(walkList);
    }
    private void byBus(){
        map.addOverlays(busList);
    }
    private void drive(){
        map.addOverlays(carList);
    }

    OnGetRoutePlanResultListener routePlanListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(map);
            List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();

            if(routeLines!=null&&routeLines.size()>0){
                walkingRouteOverlay.setData(routeLines.get(0));
                int time = routeLines.get(0).getDuration();
                walkTime = getDurationTime(time);
                tv_walk.setText(walkTime);
            }
            walkList = walkingRouteOverlay.getOverlayOptions();
            map.addOverlays(walkList);
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            MassTransitRouteOverlay massRouteOverlay = new MassTransitRouteOverlay(map);
            List<MassTransitRouteLine> routeLines = massTransitRouteResult.getRouteLines();

            if(routeLines!=null&&routeLines.size()>0){
                massRouteOverlay.setData(routeLines.get(0));
                int time = routeLines.get(0).getDuration();

                allSteps = routeLines.get(0).getNewSteps();

                busTime = getDurationTime(time);
                tv_bus.setText(busTime);
            }
            busList = massRouteOverlay.getOverlayOptions();

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(map);
            List<DrivingRouteLine> routeLines = drivingRouteResult.getRouteLines();

            if(routeLines!=null&&routeLines.size()>0){
                drivingRouteOverlay.setData(routeLines.get(0));
                int time = routeLines.get(0).getDuration();
                carTime = getDurationTime(time);
                tv_car.setText(carTime);
            }

            carList = drivingRouteOverlay.getOverlayOptions();
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    /**
    * 获取路线时间
    *
    **/
    public String getDurationTime(int time){

        if(60<time&&time<3600){
            timeStr= time/60+"分钟";
        }else if(time==60){
            timeStr = time/60+"分钟";
        }else if(time<60){
            timeStr = time%60+"秒";
        }else if(time==3600){
            timeStr = time/3600+"小时";
        }else if(time>3600){
            timeStr = time/3600+"小时"+(time%3600)%60+"分钟";
        }
        return timeStr;
    }


    private void showColor(String str){
        if("walk".equals(str)){
            img_walk_grey.setVisibility(View.GONE);
            img_walk_blue.setVisibility(View.VISIBLE);
            bus_gray.setVisibility(View.VISIBLE);
            bus_blue.setVisibility(View.GONE);
            car_gray.setVisibility(View.VISIBLE);
            car_blue.setVisibility(View.GONE);
            tv_walk.setTextColor(Color.rgb(97,197,231));
            tv_car.setTextColor(Color.rgb(153,153,153));
            tv_bus.setTextColor(Color.rgb(153,153,153));
            tv_station.setVisibility(View.GONE);
        }else if("bus".equals(str)){
            img_walk_grey.setVisibility(View.VISIBLE);
            img_walk_blue.setVisibility(View.GONE);
            bus_gray.setVisibility(View.GONE);
            bus_blue.setVisibility(View.VISIBLE);
            car_gray.setVisibility(View.VISIBLE);
            car_blue.setVisibility(View.GONE);
            tv_walk.setTextColor(Color.rgb(153,153,153));
            tv_bus.setTextColor(Color.rgb(97,197,231));
            tv_car.setTextColor(Color.rgb(153,153,153));
        }else if("car".equals(str)){
            img_walk_grey.setVisibility(View.VISIBLE);
            img_walk_blue.setVisibility(View.GONE);
            bus_gray.setVisibility(View.VISIBLE);
            bus_blue.setVisibility(View.GONE);
            car_gray.setVisibility(View.GONE);
            car_blue.setVisibility(View.VISIBLE);
            tv_walk.setTextColor(Color.rgb(153,153,153));
            tv_bus.setTextColor(Color.rgb(153,153,153));
            tv_car.setTextColor(Color.rgb(97,197,231));
            tv_station.setVisibility(View.GONE);
        }

    }

}
