package com.chj.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * @包名:com.chj.baidumap
 * @类名:HelloWorld
 * @作者:陈火炬
 * @时间:2015-8-17 下午3:57:09
 * 
 * @描述:入门
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class HelloWorld extends Activity
{
	private static final String	TAG			= "HelloWorld";

	private MyBaiduSDKRecevier	baiduSDKRecevier;
	private BaiduMap			baiduMap;
	private MapView				mapView;
	// TODO:
	private double				latitude	= 40.050966;						// 纬度
	private double				longitude	= 116.303128;						// 经度
	private LatLng				Pos			= new LatLng(latitude, longitude);	// 黑马

	// 鼓浪屿: 经度:118.073486,24.452261
	// 河南省 周口市 周口市 周口师范学院 揽月湖 114.690423,33.640877
	// 福建省 厦门市 翔安区 内厝镇 官路村 118.284773,24.6958
	// private double latitude = 24.452261;
	// private double longitude = 118.073486;
	// private LatLng Pos = new LatLng(latitude, longitude);

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
		// 设置地图级别(V2.X 3-19 V1.X 3-18)
		// ① 修改了文件到格式，优化类空间到使用（北京 110M-->15M）
		// ② 增加了级别 3D效果（18 19）

		mapView = (MapView) findViewById(R.id.map_view);

		baiduMap = mapView.getMap();
		// BaiduMap:管理具体到某一个MapView：旋转、移动、缩放、事件...

		// 描述地图状态将要发生的变化,使用工厂类MapStatusUpdateFactory创建
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(19);// 默认到级别12（3-19）
		// 设置地图缩放级别
		baiduMap.setMapStatus(mapStatusUpdate);

		// LatLng latLng = new LatLng(arg0, arg1);// 坐标 经纬度 参数1 纬度 参数2 经度
		MapStatusUpdate mapStatusUpdatePoint = MapStatusUpdateFactory.newLatLng(Pos);
		// 设置中心点,默认是天安门
		baiduMap.setMapStatus(mapStatusUpdatePoint);

		// mapView.showZoomControls(false);// 默认是true 显示缩放按钮
		// mapView.showScaleControl(false);// 默认是true 显示标尺

		// baiduMap.getUiSettings().setCompassEnabled(false);// 默认是true 显示指南针
		// baiduMap.getUiSettings().setCompassPosition(new Point(x, y));// 指南针设置
	}

	/**
	 * 初始化地图
	 * 
	 */
	private void initManager()
	{
		SDKInitializer.initialize(getApplicationContext());// 不能传递Activity，必须是全局到Content

		baiduSDKRecevier = new MyBaiduSDKRecevier();
		// 给广播接收者注册活动
		IntentFilter filter = new IntentFilter();
		filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);// 注册网络错误
		filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);// 注册校验key失败
		registerReceiver(baiduSDKRecevier, filter);

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

	/**
	 * 按键点击事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// 旋转、移动、缩放
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_1:// 放大
				// 放大地图缩放级别 每次放大一个级别
				MapStatusUpdate zoomInStates = MapStatusUpdateFactory.zoomIn();
				baiduMap.setMapStatus(zoomInStates);
				break;
			case KeyEvent.KEYCODE_2:// 缩小
				// 缩小地图缩放级别 每次缩小一个级别
				MapStatusUpdate zoomOutStates = MapStatusUpdateFactory.zoomOut();
				baiduMap.setMapStatus(zoomOutStates);
				break;
			case KeyEvent.KEYCODE_3:// 旋转
				// 以一个点为中心 旋转
				MapStatus mapStatus = baiduMap.getMapStatus();// 获取地图的当前状态
				float rotate = mapStatus.rotate;
				Log.d(TAG, "rotate:" + rotate);
				// 旋转 范围0~360
				MapStatus rotateStatus = new MapStatus.Builder().rotate(rotate + 30).build();
				// 创建新的MapStatus
				MapStatusUpdate rotateStatusUpdate = MapStatusUpdateFactory.newMapStatus(rotateStatus);
				baiduMap.setMapStatus(rotateStatusUpdate);
				break;
			case KeyEvent.KEYCODE_4:// 旋转
				// 以一条直线 为轴 旋转 Overlooking 俯角
				MapStatus mapStatusOver = baiduMap.getMapStatus();
				float overlook = mapStatusOver.overlook;
				Log.d(TAG, "overlook:" + overlook);
				// 0~45
				MapStatus overStatus = new MapStatus.Builder().overlook(overlook - 5).build();
				MapStatusUpdate overStatusUpdate = MapStatusUpdateFactory.newMapStatus(overStatus);
				baiduMap.setMapStatus(overStatusUpdate);
				break;
			case KeyEvent.KEYCODE_5:// 移动
				MapStatusUpdate movestatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(118.09772180000004, 24.4390262));
				// 带动画的更新地图状态 耗时300毫秒
				baiduMap.animateMapStatus(movestatusUpdate);
				break;

			// TODO:以提示框到形式展示地图功能
			case KeyEvent.KEYCODE_MENU:// 菜单键
				AlertDialog.Builder builder = new AlertDialog.Builder(HelloWorld.this);
				builder.setTitle("地图功能");
				String[] items = new String[] { "放大", "缩小", "水平旋转", "竖直旋转", "平移" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								// 放大地图缩放级别 每次放大一个级别
								MapStatusUpdate zoomInStates = MapStatusUpdateFactory.zoomIn();
								baiduMap.setMapStatus(zoomInStates);
								break;
							case 1:
								// 缩小地图缩放级别 每次缩小一个级别
								MapStatusUpdate zoomOutStates = MapStatusUpdateFactory.zoomOut();
								baiduMap.setMapStatus(zoomOutStates);
								break;
							case 2:
								// 以一个点为中心 旋转
								MapStatus mapStatus = baiduMap.getMapStatus();// 获取地图的当前状态
								float rotate = mapStatus.rotate;
								// 旋转 范围0~360
								MapStatus rotateStatus = new MapStatus.Builder().rotate(rotate + 30).build();
								// 创建新的MapStatus
								MapStatusUpdate rotateStatusUpdate = MapStatusUpdateFactory.newMapStatus(rotateStatus);
								baiduMap.setMapStatus(rotateStatusUpdate);
								break;
							case 3:
								// 以一条直线 为轴 旋转 Overlooking 俯角
								MapStatus mapStatusOver = baiduMap.getMapStatus();
								float overlook = mapStatusOver.overlook;
								// 0~45
								MapStatus overStatus = new MapStatus.Builder().overlook(overlook - 5).build();
								MapStatusUpdate overStatusUpdate = MapStatusUpdateFactory.newMapStatus(overStatus);
								baiduMap.setMapStatus(overStatusUpdate);
								break;
							case 4:
								MapStatusUpdate movestatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(118.09772180000004, 24.4390262));
								// 带动画的更新地图状态 耗时300毫秒
								baiduMap.animateMapStatus(movestatusUpdate);
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

	@Override
	protected void onDestroy()
	{
		mapView.onDestroy();
		unregisterReceiver(baiduSDKRecevier);
		super.onDestroy();
	}

	/**
	 * 自定义广播接收者
	 */
	class MyBaiduSDKRecevier extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String result = intent.getAction();
			// 判断网络
			if (result.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR))
			{
				// 网络错误
				Toast.makeText(getApplicationContext(), "无网络", 0).show();
			}
			// 授权验证
			else if (result.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR))
			{
				// key校验失败
				Toast.makeText(getApplicationContext(), "校验失败", 0).show();
			}
		}
	}
}
