package com.chj.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

/**
 * @包名:com.chj.baidumap
 * @类名:LayerDemo
 * @作者:陈火炬
 * @时间:2015-8-17 下午9:07:30
 * 
 * @描述:图层
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class LayerDemo extends Activity
{
	private MapView		mapView;

	private BaiduMap	baiduMap;

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

		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
	}

	/**
	 * 初始化地图
	 */
	private void initManager()
	{
		SDKInitializer.initialize(getApplicationContext());

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// 底图 交通图 卫星图
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_1:// 底图
				// 设置地图类型 MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图
				baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
				baiduMap.setTrafficEnabled(false);
				break;
			case KeyEvent.KEYCODE_2: // 卫星图
				baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				baiduMap.setTrafficEnabled(false);
				break;
			case KeyEvent.KEYCODE_3:// 交通图
				// 是否打开交通图层
				baiduMap.setTrafficEnabled(true);
				break;
			case KeyEvent.KEYCODE_MENU:// 菜单键
				AlertDialog.Builder builder = new AlertDialog.Builder(LayerDemo.this);
				builder.setTitle("地图类型");
				String[] items = new String[] { "底图", "卫星图", "交通图" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								// 设置地图类型 MAP_TYPE_NORMAL 普通图；
								// MAP_TYPE_SATELLITE 卫星图
								baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
								baiduMap.setTrafficEnabled(false);
								break;
							case 1:
								baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
								baiduMap.setTrafficEnabled(false);
								break;
							case 2:
								// 是否打开交通图层
								baiduMap.setTrafficEnabled(true);
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
