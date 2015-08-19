package com.chj.baidumap.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.chj.baidumap.R;

/**
 * @包名:com.chj.baidumap.base
 * @类名:BaseActivity
 * @作者:陈火炬
 * @时间:2015-8-18 上午9:36:02
 * 
 * @描述:地图基类
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class BaseActivity extends Activity
{
	protected BaiduMap	baiduMap;
	protected MapView	mapView;

	// 黑马40.050966,116.303128
	// 周口师范学院 揽月湖 33.640877,114.690423
	// 老家 24.6958,118.284773
	// 鼓浪屿 24.452261,118.073486
	protected double	latitude	= 24.452261;
	protected double	longitude	= 118.073486;
	protected LatLng	Pos			= new LatLng(latitude, longitude);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉title
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common);

		initManager();
		init();
	}

	/**
	 * 设置地图级别
	 */
	private void init()
	{
		mapView = (MapView) findViewById(R.id.map_view);
		baiduMap = mapView.getMap();

		// 设置地图缩放级别
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);// 默认到级别12（3-19）
		baiduMap.setMapStatus(mapStatusUpdate);

		// 设置中心点,默认是天安门
		MapStatusUpdate mapStatusUpdatePoint = MapStatusUpdateFactory.newLatLng(Pos);
		baiduMap.setMapStatus(mapStatusUpdatePoint);
	}

	/**
	 * 初始化地图
	 * 
	 */
	private void initManager()
	{
		// SDKInitializer.initialize(getApplicationContext());//
		// 不能传递Activity，必须是全局到Content
	}

	@Override
	protected void onResume()
	{
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mapView.onDestroy();
		super.onDestroy();
	}
}
