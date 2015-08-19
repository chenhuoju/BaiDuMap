package com.chj.baidumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.chj.baidumap.base.BaseActivity;

/**
 * @包名:com.chj.baidumap
 * @类名:LocationDemo
 * @作者:陈火炬
 * @时间:2015-8-19 上午8:52:42
 * 
 * @描述:定位
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class LocationDemo extends BaseActivity
{
	public LocationClient										mLocationClient;													// 声明LocationClient类
	public BDLocationListener									myListener		= new MyListener();								// 注册监听函数
	private BitmapDescriptor									descriptor;

	// COMPASS 罗盘态，显示定位方向圈，保持定位图标在地图中心
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	COMPASS			= MyLocationConfiguration.LocationMode.COMPASS;
	// FOLLOWING 跟随态，保持定位图标在地图中心
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	FOLLOWING		= MyLocationConfiguration.LocationMode.FOLLOWING;
	// NORMAL 普通态： 更新定位数据时不对地图做任何操作
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	NORMAL			= MyLocationConfiguration.LocationMode.NORMAL;
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	locationMode	= FOLLOWING;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		locate();
	}

	/**
	 * 定位
	 */
	private void locate()
	{
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setNeedDeviceDirect(true);// 返回定位结果，包含手机头的方向
		// option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		// option.setLocationNotify(true);//
		// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		// option.setIgnoreKillProcess(false);//
		// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		// option.SetIgnoreCacheException(false);//
		// 可选，默认false，设置是否收集CRASH信息，默认收集
		mLocationClient.setLocOption(option);

		setConfigeration();
	}

	/**
	 * @参数1: MyLocationConfiguration.LocationMode locationMode 定位图层显示方式
	 * @COMPASS 罗盘态，显示定位方向圈，保持定位图标在地图中心;
	 * @FOLLOWING 跟随态，保持定位图标在地图中心;
	 * @NORMAL 普通态： 更新定位数据时不对地图做任何操作
	 * 
	 * @参数2:boolean enableDirection 是否允许显示方向信息;true和false
	 * 
	 * @参数3:BitmapDescriptor customMarker 用户自定义定位图标
	 */
	private void setConfigeration()
	{
		baiduMap.clear();
		descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		MyLocationConfiguration configeration = new MyLocationConfiguration(locationMode, true, descriptor);
		baiduMap.setMyLocationConfigeration(configeration);// 设置定位显示的模式
		baiduMap.setMyLocationEnabled(true);// 打开定位图层
	}

	@Override
	protected void onStart()
	{
		mLocationClient.start();
		super.onStart();
	}

	@Override
	protected void onPause()
	{
		mLocationClient.stop();
		super.onPause();
	}

	/**
	 * 自定义监听类
	 */
	class MyListener implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation result)
		{
			if (result != null)
			{
				MyLocationData data = new MyLocationData.Builder()
																	.latitude(result.getLatitude())// 纬度坐标
																	.longitude(result.getLongitude())
																	// 经度坐标
																	.build();
				baiduMap.setMyLocationData(data);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_MENU:
				AlertDialog.Builder builder = new AlertDialog.Builder(LocationDemo.this);
				builder.setTitle("定位图层显示方式");
				String[] items = new String[] { "罗盘态", "跟随态", "普通态" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:// 罗盘态
								locationMode = COMPASS;
								setConfigeration();
								break;
							case 1:// 跟随态
								locationMode = FOLLOWING;
								setConfigeration();
								break;
							case 2:// 普通态
								locationMode = NORMAL;
								setConfigeration();
								break;
							default:
								dialog.dismiss();
								break;
						}
					}
				});
				builder.show();
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
