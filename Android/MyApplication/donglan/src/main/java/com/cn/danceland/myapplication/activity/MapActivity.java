package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LocationService;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/11/1.
 */

public class MapActivity extends Activity {
    public LocationService mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    MapView mapview;
    PoiSearch poiSearch;
    GeoCoder geoCoder;
    double latitude;
    double longitude;
    double myLatitude;
    double myLongitude;
    LatLng location1,myLocation;
    PlanNode stNode;
    BaiduMap map;
    RoutePlanSearch routePlanSearch;
    PlanNode enNode;
    String addressstr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initHost();
        intiView();
        setOnClick();
        initData();
        //geoCoder.geocode(new GeoCodeOption().city("北京").address("长阳地铁站"));

        onFoot();
    }

    private void initHost() {
        mLocationClient = ((MyApplication) getApplication()).locationClient;
        mLocationClient.registerListener(myListener);
//        geoCoder = GeoCoder.newInstance();
//        geoCoder.setOnGetGeoCodeResultListener(resultListener);

        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(routePlanListener);
        mLocationClient.start();
    }

    OnGetGeoCoderResultListener resultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            latitude = geoCodeResult.getLocation().latitude;
            longitude = geoCodeResult.getLocation().longitude;
            location1 = geoCodeResult.getLocation();
            //stNode = PlanNode.withLocation(location1);
            LogUtil.e("zzf",longitude+"");


//            MyLocationData locData = new MyLocationData.Builder()
//                    .latitude(latitude)
//                    .longitude(longitude).build();
//            map.setMyLocationData(locData);
//            map.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true,BitmapDescriptorFactory.fromResource(R.drawable.zhongdian_icon)));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        }
    };

    private void setOnClick() {
            //poiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    private void initData() {
        //poiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("美食").pageNum(10));

    }

    private void intiView() {
        mapview = findViewById(R.id.mapview);
        map = mapview.getMap();
        map.setMyLocationEnabled(true);

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

    public void onFoot(){
        //PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "西二旗地铁站");
//        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "西二旗地铁站");
//
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "百度科技园");

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                Address address = location.getAddress();
                LatLng latLng = new LatLng(myLatitude, myLongitude);

                LogUtil.e("zzf",myLatitude+"");
                enNode = PlanNode.withLocation(latLng);
                //enNode = PlanNode.withCityNameAndPlaceName("北京","永翌公馆");
                stNode = PlanNode.withCityNameAndPlaceName("北京","天安门");
                if(enNode!=null&&stNode!=null){
                    routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
                }


//                MyLocationData locData = new MyLocationData.Builder()
//                        .latitude(myLatitude)
//                        .longitude(myLongitude).build();
//                map.setMyLocationData(locData);
//                map.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true,BitmapDescriptorFactory.fromResource(R.drawable.qidian_icon)));
//                sb.append(location.getTime());
//                sb.append("\nlocType : ");// 定位类型
//                sb.append(location.getLocType());
//                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
//                sb.append("\nlatitude : ");// 纬度
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");// 经度
//                sb.append(location.getLongitude());
//                sb.append("\nradius : ");// 半径
//                sb.append(location.getRadius());
            }
        }

    }

    OnGetRoutePlanResultListener routePlanListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(map);
            List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();
            walkingRouteOverlay.setData(routeLines.get(0));
            List<OverlayOptions> list = walkingRouteOverlay.getOverlayOptions();
            map.addOverlays(list);
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

}
